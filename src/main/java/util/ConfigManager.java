package util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;

/**
 * Singleton class for getting configuration information.
 */
public class ConfigManager {

  /** Instance of the config manager **/
  private static ConfigManager instance_;

  /** Config object **/
  private Config config_;

  /** Context path **/
  private String contextPath_;

  /**
   * Initialize the config manager on start.
   */
  @PostConstruct
  public void init() {
    // Set the instance
    instance_ = this;

    // Read the config file
    Reader reader;
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      InputStream configStream = classLoader.getResourceAsStream("config.json");
      reader = new InputStreamReader(configStream);
    } catch (Exception e) {
      throw new RuntimeException("Unable to load config file.");
    }

    // Parse the JSON file using Gson
    Gson gson = new Gson();
    config_ = gson.fromJson(reader, Config.class);

    // Check that all values are valid
    try {
      config_.checkValues();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the singleton instance of the config manager.
   * 
   * @return config manager instance
   */
  public static ConfigManager getInstance() {
    if (instance_ == null) {
      synchronized (ConfigManager.class) {
        if (instance_ == null) {
          instance_ = new ConfigManager();
          instance_.init();
        }
      }
    }
    return instance_;
  }

  /**
   * Get the username for sending emails.
   * 
   * @return username
   */
  public String getEmailUsername() {
    return config_.email_username;
  }

  /**
   * Get the password for sending emails.
   * 
   * @return password
   */
  public String getEmailPassword() {
    return config_.email_password;
  }

  /**
   * Get the name to use for sending emails.
   * 
   * @return name
   */
  public String getEmailName() {
    return config_.email_name;
  }

  /**
   * Get the filepath for the directory where search information is saved.
   * 
   * @return filepath
   */
  public String getSavedSearchDirectory() {
    return config_.saved_search_directory;
  }

  /**
   * Get the base URL for the page.
   * 
   * @return base URL string
   */
  public String getBaseURL() {
    return config_.base_url;
  }

  /**
   * Get the maximum number of searches per minute.
   * 
   * @return maximum number of searches per minute
   */
  public int getMaxSearchesPerMinute() {
    return config_.max_searches_per_minute;
  }

  /**
   * Set the context path.
   * 
   * @param contextPath context path
   */
  public void setContextPath(String contextPath) {
    contextPath_ = contextPath;
  }

  /**
   * Get the context path.
   * 
   * @return context path
   */
  public String getContextPath() {
    return contextPath_;
  }

  /**
   * Simple serializable class for config data.
   */
  private class Config {

    /** Email username **/
    public String email_username;

    /** Email password **/
    public String email_password;

    /** Email name **/
    public String email_name;

    /** Saved search directory **/
    public String saved_search_directory;

    /** Base URL **/
    public String base_url;

    /** Max searches per minute **/
    public Integer max_searches_per_minute;

    /**
     * Check that the values have been initialized properly, and throw an exception
     * if not.
     * 
     * @throws Exception if an unacceptable value is found.
     */
    public void checkValues() throws Exception {
      // Check the username
      if (email_username == null) {
        throw new Exception("email_username cannot be null.");
      }

      // Check the password
      if (email_password == null) {
        throw new Exception("email_password cannot be null.");
      }

      // Check the email name
      if (email_name == null) {
        throw new Exception("email_name cannot be null.");
      }

      // Check the saved search directory
      if (saved_search_directory == null) {
        throw new Exception("saved_search_directory cannot be null.");
      } else if (!new File(saved_search_directory).exists()) {
        throw new Exception(saved_search_directory + " does not exist.");
      }

      // Check the base URL
      if (base_url == null) {
        throw new Exception("base_url cannot be null.");
      }
      // TODO Validate URI

      // Check the max searches per second
      if (max_searches_per_minute == null) {
        throw new Exception("max_searches_per_minute cannot be null.");
      }

    }
  }
}
