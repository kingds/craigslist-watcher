package craigslist;

/**
 * Enumerator of various search frequencies.
 */
public enum SearchFrequency {
  /** Ten seconds **/
  TEN_SECONDS(10000, "ten seconds"),

  /** One minute **/
  MINUTE(60000, "one minute"),

  /** Ten minutes **/
  TEN_MINUTES(600000, "ten minutes"),

  /** Thirty minutes **/
  THIRTY_MINUTES(1800000, "thirty minutes"),

  /** One hour **/
  HOUR(3600000, "one hour"),

  /** One day **/
  DAY(86400000, "one day"),

  /** One week **/
  WEEK(604800000, "one week");

  /** Frequency in milliseconds **/
  private final long millis_;

  /** Description of the frequency **/
  private final String description_;

  /**
   * Constructor for a search frequency object.
   * 
   * @param millis frequency in milliseconds
   * @param description description of the frequency
   */
  private SearchFrequency(long millis, String description) {
    millis_ = millis;
    description_ = description;
  }

  /**
   * Get the description.
   * 
   * @return description
   */
  public String getDescription() {
    return description_;
  }

  /**
   * Get the duration between searches in milliseconds.
   * 
   * @return frequency in milliseconds
   */
  public long getMillis() {
    return millis_;
  }
}
