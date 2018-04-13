package craigslist;

import java.util.Date;
import java.util.List;

/**
 * Interface for a class which will fetch results from Craigslist.
 */
public interface ResultFetcher {

  /**
   * Get all available results.
   * 
   * @return list of results
   */
  public List<Result> getAllResults();

  /**
   * Get the most recent n results.
   * 
   * @param maxResults maximum number of results to fetch
   * @return list of results
   */
  public List<Result> getResults(int maxResults);

  /**
   * Get all available results since a specified date.
   * 
   * @param date last date for results
   * @return list of results
   */
  public List<Result> getResultsSinceDate(Date date);

  /**
   * Get the most recent n results posted after a specified date
   * 
   * @param date last date for results
   * @param maxResults maximum number of results to fetch
   * @return list of results
   */
  public List<Result> getResultsSinceDate(Date date, int maxResults);

}
