package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import craigslist.Search;
import craigslist.SearchFrequency;
import craigslist.SearchManager;
import email.Email;
import util.CraigslistException;
import util.Errors;

/**
 * Servlet for creating new search queries.
 */
@WebServlet("/NewSearch")
public class NewSearch extends HttpServlet {

  /**
   * {@inheritDoc}
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Get the search string and validate it
    String searchURL = request.getParameter("searchUrl");
    try {
      validateSearch(searchURL);
    } catch (CraigslistException e) {
      request.setAttribute("error", e.getError());
      request.getRequestDispatcher("error.jsp").forward(request, response);
      return;
    }

    // Respond with the new search page
    request.getRequestDispatcher("newsearch.jsp").forward(request, response);
  }

  /**
   * {@inheritDoc}
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Get the parameters from the request
    String searchURL = request.getParameter("searchUrl");
    String email = request.getParameter("email");
    String frequencyString = request.getParameter("frequency");
    String name = request.getParameter("name");

    // Validate the attributes and get the frequency
    SearchFrequency frequency;
    try {
      validateSearch(searchURL);
      validateEmail(email);
      frequency = getFrequency(frequencyString);
      validateName(name);
    } catch (CraigslistException e) {
      request.setAttribute("error", e.getError());
      request.getRequestDispatcher("error.jsp").forward(request, response);
      return;
    }

    // Create the search
    Search search = new Search(searchURL, email, frequency, name);

    // Add the search
    SearchManager searchManager = SearchManager.getInstance();
    searchManager.addSearch(search);

    // Respond with the enrolled page
    request.getRequestDispatcher("enrollsuccess.jsp").forward(request, response);
  }

  /**
   * Validate the search string.
   * 
   * @param search search URL string
   * @throws CraigslistException
   */
  private void validateSearch(String search) throws CraigslistException {
    if (!Search.isSearchValid(search)) {
      throw new CraigslistException(Errors.INVALID_SEARCH);
    }
  }

  /**
   * Validate the email address.
   * 
   * @param email email address string
   * @throws CraigslistException
   */
  private void validateEmail(String email) throws CraigslistException {
    if (!Email.isAddressValid(email)) {
      throw new CraigslistException(Errors.INVALID_EMAIL);
    }
  }

  /**
   * Validate the search name.
   * 
   * @param name search name
   * @throws CraigslistException
   */
  private void validateName(String name) throws CraigslistException {
    if (!Search.isNameValid(name)) {
      throw new CraigslistException(Errors.INVALID_NAME);
    }
  }

  /**
   * Get the search frequency.
   * 
   * @param frequencyString search frequency string
   * @return search frequency
   * @throws CraigslistException
   */
  private SearchFrequency getFrequency(String frequencyString) throws CraigslistException {
    // Check for null value
    if (frequencyString == null) {
      throw new CraigslistException(Errors.INVALID_FREQUENCY);
    }

    // Create the frequency object
    SearchFrequency frequency;
    if (frequencyString.equals("minute")) {
      frequency = SearchFrequency.MINUTE;
    } else if (frequencyString.equals("ten_minutes")) {
      frequency = SearchFrequency.TEN_MINUTES;
    } else if (frequencyString.equals("thirty_minutes")) {
      frequency = SearchFrequency.THIRTY_MINUTES;
    } else if (frequencyString.equals("hour")) {
      frequency = SearchFrequency.HOUR;
    } else if (frequencyString.equals("day")) {
      frequency = SearchFrequency.DAY;
    } else if (frequencyString.equals("week")) {
      frequency = SearchFrequency.WEEK;
    } else {
      throw new CraigslistException(Errors.INVALID_FREQUENCY);
    }

    return frequency;
  }

}
