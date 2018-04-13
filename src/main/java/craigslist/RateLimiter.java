package craigslist;

import java.util.concurrent.Semaphore;

import javax.annotation.PostConstruct;

import util.ConfigManager;

/**
 * Singleton class for limiting the rate of searches.
 */
public class RateLimiter {

  /** Singleton instance **/
  private static RateLimiter instance_;

  /** Semaphore for issuing permits **/
  private Semaphore semaphore_;

  /** Maximum number of searches per minute **/
  private int maxSearchesPerMinute_;

  /**
   * Post-construct method for initializing the singleton.
   */
  @PostConstruct
  public void init() {
    instance_ = this;
    maxSearchesPerMinute_ = ConfigManager.getInstance().getMaxSearchesPerMinute();
    semaphore_ = new Semaphore(1);
  }

  /**
   * Get the instance of the singleton.
   * 
   * @return singleton instance
   */
  public static RateLimiter getInstance() {
    if (instance_ == null) {
      synchronized (RateLimiter.class) {
        if (instance_ == null) {
          instance_ = new RateLimiter();
          instance_.init();
        }
      }
    }
    return instance_;
  }

  /**
   * Wait until permission is granted to run.
   */
  public void waitToRun() {
    // Acquire the semaphore
    try {
      semaphore_.acquire();
    } catch (InterruptedException e) {
    }

    // Start a new thread which will wait the required amount of time before
    // releasing the semaphore
    new Thread(() -> {
      try {
        Thread.sleep(60000 / maxSearchesPerMinute_);
      } catch (InterruptedException e) {
      }
      semaphore_.release();
    }).start();
  }
}
