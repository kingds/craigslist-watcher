package util;

/**
 * Enumerator of possible errors.
 */
public enum Errors {
  /** Invalid email **/
  INVALID_EMAIL("You entered an invalid email address."),

  /** Invalid frequency **/
  INVALID_FREQUENCY("The specified search frequency is not valid."),
  
  /** Invalid search **/
  INVALID_SEARCH("The search URL you entered is not valid."),

  /** Invalid name **/
  INVALID_NAME("The name you entered is not valid."),

  /** Unknown error **/
  UNKNOWN_ERROR("An unknown error occurred."),

  /** UUID not found **/
  UUID_NOT_FOUND("No saved search could be found with the specified ID.");

  /** Error message **/
  private final String message_;

  /**
   * Private enum constructor.
   * 
   * @param message error message
   */
  private Errors(String message) {
    message_ = message;
  }

  /**
   * Get the error message.
   * 
   * @return error message
   */
  public String getMessage() {
    return message_;
  }

}
