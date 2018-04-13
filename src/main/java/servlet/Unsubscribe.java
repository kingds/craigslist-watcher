package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import craigslist.Search;
import craigslist.SearchManager;
import util.Errors;

/**
 * Servlet for unsubscribing from a search.
 */
@WebServlet("/Unsubscribe")
public class Unsubscribe extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * {@inheritDoc}
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Retrieve the search from the search manager
    String uuid = request.getParameter("uuid");
    SearchManager searchManager = SearchManager.getInstance();
    Search search = searchManager.getSearch(uuid);
    
    // Remove the search if it exists, show an error page otherwise
    if (search != null) {
      searchManager.removeSearch(uuid);
      request.getRequestDispatcher("unsubscribesuccess.jsp").forward(request, response);
      return;
    } else {
      request.setAttribute("error", Errors.UUID_NOT_FOUND);
      request.getRequestDispatcher("error.jsp").forward(request, response);
      ;
      return;
    }
  }

}
