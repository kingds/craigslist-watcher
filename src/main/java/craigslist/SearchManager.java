package craigslist;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import util.ConfigManager;

/**
 * Singleton class for managing, queueing, and running searches.
 */
public class SearchManager {

  /** Singleton instance of the search manager **/
  private static SearchManager instance_;

  /** Map of searches, organized by UUID **/
  private Map<String, Search> searches_;

  /** Map of scheduled searches and futures **/
  private Map<Search, ScheduledFuture<?>> scheduledSearches_;

  /** Executor for running searches periodically **/
  private ScheduledThreadPoolExecutor scheduler_;

  /**
   * Post-construct method for initializing the singleton.
   */
  @PostConstruct
  public void init() {
    instance_ = this;
    searches_ = new ConcurrentHashMap<String, Search>();
    scheduledSearches_ = new ConcurrentHashMap<Search, ScheduledFuture<?>>();
    scheduler_ = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(20);
    scheduler_.setRemoveOnCancelPolicy(true);
    
    // Make sure the config manager is initialized
    ConfigManager.getInstance();

    // Load all saved searches
    loadSavedSearches();

    // Start the waiting process for all searches
    for (Search search : searches_.values()) {
      scheduleSearch(search);
    }
  }
  
  /**
   * Pre-destroy method for canceling scheduled searches.
   */
  @PreDestroy
  public void shutdown() {
    // Cancel all scheduled searches
    for (ScheduledFuture<?> scheduledSearch : scheduledSearches_.values()) {
      scheduledSearch.cancel(false);
    }
    
    // Stop the scheduler
    scheduler_.shutdownNow();
  }

  /**
   * Get the singleton instance of the search manager.
   * 
   * @return search manager instance
   */
  public static SearchManager getInstance() {
    if (instance_ == null) {
      synchronized (SearchManager.class) {
        if (instance_ == null) {
          instance_ = new SearchManager();
          instance_.init();
        }
      }
    }
    return instance_;
  }

  /**
   * Add a search to the collection.
   * 
   * @param search search
   */
  public void addSearch(Search search) {
    // Add the search to the map
    searches_.put(search.getUUID(), search);

    // Save the search
    search.save();

    // Start a thread for running the initial search and sending the enrollment
    // email
    new Thread(() -> {
      try {
        search.sendInitialResults();
      } finally {
        scheduleSearch(search);
      }
    }).start();
  }

  /**
   * Remove a search from the collection.
   * 
   * @param uuid identifier for the search
   */
  public void removeSearch(String uuid) {
    // Remove the search from the map
    Search search = searches_.remove(uuid);

    // Delete the file for the search
    if (search != null) {
      ScheduledFuture<?> futureSearch = scheduledSearches_.remove(search);
      if (futureSearch != null) {
        futureSearch.cancel(false);
      }
      search.remove();
    }
  }

  /**
   * Retrieve a search from the collection.
   * 
   * @param uuid identifier for the search
   * @return search
   */
  public Search getSearch(String uuid) {
    return searches_.get(uuid);
  }

  /**
   * Load all saved searches.
   */
  private void loadSavedSearches() {
    // Clear the list of searches
    searches_.clear();

    // Get all of the saved searches
    String savedSearchDirectory = ConfigManager.getInstance().getSavedSearchDirectory();
    File searchDirectory = new File(savedSearchDirectory);
    File[] savedSearches = searchDirectory.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".json");
      }
    });

    // Parse each search file to create a search, and add it to the collection
    for (File savedSearch : savedSearches) {
      try {
        Search search = Search.fromFile(savedSearch.getAbsolutePath());
        searches_.put(search.getUUID(), search);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Schedule a search to run periodically.
   * 
   * @param search search
   */
  private void scheduleSearch(Search search) {
    long secondsUntilRun = search.getSecondsUntilRun();
    long frequencySeconds = search.getFrequency().getMillis() / 1000;
    scheduledSearches_.put(search, scheduler_.scheduleAtFixedRate(search, secondsUntilRun, frequencySeconds, TimeUnit.SECONDS));
  }

}
