package craigslist;

import java.util.Date;

/**
 * Class representing a Craigslist post.
 */
public class Result {

	/** Title of the posting **/
	private String title_;

	/** Link to the result **/
	private String link_;

	/** Description of the posting **/
	private String description_;

	/** Date the result was posted **/
	private Date date_;

	/**
	 * Constructor for a result object.
	 * 
	 * @param title title of the posting
	 * @param link link to the result
	 * @param description description of the posting
	 * @param date date the result was posted
	 */
	public Result(String title, String link, String description, Date date) {
		title_ = title;
		link_ = link;
		description_ = description;
		date_ = date;
	}

	/**
	 * Get the title of the posting.
	 * 
	 * @return title of the posting
	 */
	public String getTitle() {
		return title_;
	}

	/**
	 * Get the link to the posting.
	 * 
	 * @return link to the posting
	 */
	public String getLink() {
		return link_;
	}

	/**
	 * Get the description of the posting.
	 * 
	 * @return description of the posting
	 */
	public String getDescription() {
		return description_;
	}

	/**
	 * Get the date the result was posted.
	 * 
	 * @return date the result was posted
	 */
	public Date getDate() {
		return date_;
	}

}
