package craigslist;

import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import email.Email;
import util.ConfigManager;

/**
 * Class representing a Craigslist search.
 * 
 * @author Daniel King
 */
public class Search implements Runnable{

  /** URL search string **/
  private final String searchURL_;

  /** Email address to send results **/
  private final String email_;

  /** Frequency with which to conduct searches **/
  private final SearchFrequency frequency_;

  /** Name for the search **/
  private final String name_;

  /** Date when the last search was completed **/
  private Date lastSearchDate_;

  /** Date when the last result was posted **/
  private Date lastResultDate_;

  /** Unique identifier **/
  private final String uuid_;
  
  /**
   * Constructor for a search object.
   * 
   * @param searchURL url for the search
   * @param email email address for results
   * @param frequency frequency with which to conduct the search
   */
  public Search(String searchURL, String email, SearchFrequency frequency, String name) {
    searchURL_ = searchURL;
    email_ = email;
    frequency_ = frequency;
    name_ = name;
    lastSearchDate_ = new Date();
    uuid_ = UUID.randomUUID().toString().replaceAll("-", "");
  }

  /**
   * Load a saved search from a file.
   * 
   * @param filepath filepath for the saved search
   */
  public static Search fromFile(String filepath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
      //TODO Fix JSON parsing
      String jsonString = reader.readLine();
      Gson gson = new GsonBuilder().create();
      Search search = gson.fromJson(jsonString, Search.class);
      return search;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Get the URL for the search.
   * 
   * @return search URL in string form
   */
  public String getSearchURL() {
    return searchURL_.toString();
  }

  /**
   * Get the email associated with the search.
   * 
   * @return email address string
   */
  public String getEmailAddress() {
    return email_;
  }

  /**
   * Get the search frequency.
   * 
   * @return search frequency
   */
  public SearchFrequency getFrequency() {
    return frequency_;
  }

  /**
   * Get the name of the search
   * 
   * @return name of the search
   */
  public String getName() {
    return name_;
  }

  /**
   * Get the UUID for the search.
   * 
   * @return UUID string
   */
  public String getUUID() {
    return uuid_;
  }

  /**
   * Get the date that the search was last run.
   * 
   * @return last search date
   */
  public Date getLastSearchDate() {
    return lastSearchDate_;
  }

  /**
   * Get the date of the result which was found most recently.
   * 
   * @return last result date
   */
  public Date getLastResultDate() {
    return lastResultDate_;
  }
  
  /**
   * Get the number of seconds until the search should be run.
   * 
   * @return number of seconds until the search should be run
   */
  public long getSecondsUntilRun() {
    long millisUntilRun = lastSearchDate_.getTime() + frequency_.getMillis() - new Date().getTime();
    if (millisUntilRun <= 0) {
      return 0;
    } else {
      return millisUntilRun / 1000;
    }
  }

  /**
   * {@inheritDoc}
   */
  public void run() {
    sendNewResults();
  }
  
  /**
   * Get new results and email them to the user.
   */
  public void sendNewResults() {
    // Wait until the rate limiter allows a new search
    RateLimiter.getInstance().waitToRun();
    
    // Get new results
    updateSearchDate();
    List<Result> results = getNewResults();
    updateLastResultDate(results);
    
    // Email the results
    if (results != null) {
      Email.sendResultsEmail(this, results);
    }
  }

  /**
   * Get initial results and email them to the user.
   */
  public void sendInitialResults() {
    // Wait until the rate limiter allows a new search
    RateLimiter.getInstance().waitToRun();
    
    // Get the 10 most recent results
    updateSearchDate();
    List<Result> results = getResults(10);
    updateLastResultDate(results);

    // Send the enrollment email
    Email.sendEnrollmentEmail(this, results);
  }

  /**
   * Save the search to a file.
   */
  public void save() {
    // Save the search with the UUID as the filename
    String outputFilepath = ConfigManager.getInstance().getSavedSearchDirectory() + "/" + uuid_ + ".json";
    try (Writer writer = new FileWriter(outputFilepath)) {
      Gson gson = new GsonBuilder().create();
      gson.toJson(this, writer);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }

  /**
   * Remove the saved file, and stop the search from running.
   */
  public void remove() {
    // Delete the saved file for the search
    String saveDirectory = ConfigManager.getInstance().getSavedSearchDirectory();
    String savedFilepath = saveDirectory + "/" + uuid_ + ".json";
    File savedFile = new File(savedFilepath);
    savedFile.delete();
  }

  /**
   * Check whether the search URL string is valid.
   * 
   * @param searchURL search URL string
   * @return true if the search is valid, false otherwise
   */
  public static boolean isSearchValid(String searchURL) {
    //TODO Make sure string is a valid Craigslist search
    // Create a URL from the search string
    try {
      new URI(searchURL);
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Check whether the search name is valid.
   * 
   * @param name search name
   * @return true if the name is valid, false otherwise
   */
  public static boolean isNameValid(String name) {
    if (name == null || name.length() == 0) {
      return false;
    }
    return true;
  }

  /**
   * Get the URI for the RSS feed associated with the search.
   * 
   * @return RSS URI, or null if no URI can be created
   */
  public URI getRssUri() {
    try {
      // Add the RSS parameter to the search URL, with the appropriate character
      if (searchURL_.contains("?")) {
        return new URI(searchURL_ + "&format=rss");
      } else {
        return new URI(searchURL_ + "?format=rss");
      }
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Get the most recent results for the search.
   * 
   * @param resultCount maximum number of results to retrieve
   * @return list of results
   */
  private List<Result> getResults(int resultCount) {
    ResultFetcher resultFetcher = new RSSResultFetcher(this);
    List<Result> results = resultFetcher.getResults(resultCount);
    return results;
  }

  /**
   * Get search results since the last result was found.
   * 
   * @return list of results
   */
  private List<Result> getNewResults() {
    ResultFetcher resultFetcher = new RSSResultFetcher(this);
    List<Result> results = resultFetcher.getResultsSinceDate(lastResultDate_);
    return results;
  }

  /**
   * Update the search date, and save the search file.
   */
  private void updateSearchDate() {
    lastSearchDate_ = new Date();
    save();
  }

  /**
   * Update the last result date, and save the search file.
   * 
   * @param results list of most recent results
   */
  private void updateLastResultDate(List<Result> results) {
    if (results == null || results.size() == 0) {
      return;
    }
    lastResultDate_ = results.get(0).getDate();
    save();
  }

}
