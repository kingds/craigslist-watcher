package util;

/**
 * Class for a custom exception.
 */
public class CraigslistException extends Exception {
  
  /** Error object **/
  private Errors error_;
  
  
  /**
   * Create an exception.
   * 
   * @param error error
   */
  public CraigslistException(Errors error) {
    error_ = error;
  }
  
  /**
   * {@inheritDoc}
   */
  public String getMessage() {
    return error_.getMessage();
  }
  
  /**
   * Get the error.
   * 
   * @return error
   */
  public Errors getError() {
    return error_;
  }

}
