package servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import craigslist.RateLimiter;
import craigslist.SearchManager;
import util.ConfigManager;

/**
 * Class for ensuring that singletons starting and stopping singletons based on
 * servlet context events.
 */
@WebListener
public class LifecycleListener implements ServletContextListener {

  /**
   * {@inheritDoc}
   */
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    // Initialize all the singleton classes
    SearchManager.getInstance();
    RateLimiter.getInstance();
    ConfigManager configManager = ConfigManager.getInstance();
    configManager.setContextPath(servletContextEvent.getServletContext().getContextPath());
  }

  /**
   * {@inheritDoc}
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    // Shut down the search manager
    SearchManager.getInstance().shutdown();
  }

}
