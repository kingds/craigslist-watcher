package craigslist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Class for fetching Craigslist results from an RSS feed.
 */
public final class RSSResultFetcher implements ResultFetcher {
  
  /** Search object **/
  private final Search search_;

  /**
   * Constructor for an RSS result fetcher.
   * 
   * @param search search object
   */
  public RSSResultFetcher(Search search) {
    search_ = search;
  }

  /**
   * {@inheritDoc}
   */
  public List<Result> getAllResults() {
    return getResultsSinceDate(null, 0);
  }

  /**
   * {@inheritDoc}
   */
  public List<Result> getResults(int maxResults) {
    return getResultsSinceDate(null, maxResults);
  }

  /**
   * {@inheritDoc}
   */
  public List<Result> getResultsSinceDate(Date date) {
    return getResultsSinceDate(date, 0);
  }

  /**
   * {@inheritDoc}
   */
  public List<Result> getResultsSinceDate(Date sinceDate, int maxResults) {
    // Create the list of results
    List<Result> results = new ArrayList<Result>();

    // If date is null, set date to start of time
    if (sinceDate == null) {
      sinceDate = new Date(0);
    }

    // Parse the feed into an XML document
    Document document;
    try {
      DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      document = builder.parse(search_.getRssUri().toString());
    } catch (Exception e) {
      return null;
    }

    // Get all of the items
    NodeList itemNodes = document.getElementsByTagName("item");
    for (int i = 0; i < itemNodes.getLength(); i++) {
      // Check if the maximum number of results has been reached 
      if (maxResults > 0 && i == maxResults) {
        break;
      }
      
      // Get the element, and parse the relevant data
      Element itemElement = (Element) itemNodes.item(i);
      Result resultItem;
      Date resultDate;
      try {
        String title = getFirstChildElement(itemElement, "title").getTextContent();
        String link = getFirstChildElement(itemElement, "link").getTextContent();
        String description = getFirstChildElement(itemElement, "description").getTextContent();
        String dateString = getFirstChildElement(itemElement, "dc:date").getTextContent();
        resultDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(dateString);
        resultItem = new Result(title, link, description, resultDate);
      } catch (Exception e) {
        // Item does not fit the expected result format
        continue;
      }

      // Stop parsing results if the result date is before the specified since date
      if (!resultDate.after(sinceDate)) {
        break;
      }

      // Add the result to the list
      results.add(resultItem);
    }

    // Return null if no results were found
    if (results.size() == 0) {
      return null;
    }

    return results;
  }

  /**
   * Get the first child element in an element with a specified tag name.
   * 
   * @param element parent element
   * @param tagName tag name
   * @return first child element with matching tag name
   */
  private Element getFirstChildElement(Element element, String tagName) throws Exception {
    // Get the list of child nodes with the matching tag name
    NodeList childNodes = element.getElementsByTagName(tagName);

    // Throw an exception if no matching child elements are found
    if (childNodes.getLength() == 0) {
      throw new Exception("No matching children found.");
    }

    return (Element) childNodes.item(0);
  }

}
