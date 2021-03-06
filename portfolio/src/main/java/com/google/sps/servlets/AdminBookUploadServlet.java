package com.google.sps.servlets;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import com.google.sps.data.BookUploadUtility;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Initiate upload books into the books datastore
@WebServlet("/admin-book-upload")
public class AdminBookUploadServlet extends HttpServlet {
  private final BookUploadUtility bookUploadUtility = new BookUploadUtility();
  private static final String UPLOAD_ERROR_MSG = "\n Could not upload: ";
  private Map<Integer, Book> bookList;
  private Gson gson = new Gson();

  @Override
  public void init() throws ServletException {
    try {
      BookReader reader = new BookReader(getServletContext().getRealPath("/WEB-INF/20_books.csv"));
      bookList = reader.makeBookList();
    } catch (Exception ex) {
      throw new ServletException("Error reading CSV file", ex);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // if not working locally, try deleting local_db.bin folder in appengine generated files
    String failedBooks = UPLOAD_ERROR_MSG;
    int successes = 0;
    for (Book book : bookList.values()) {
      try {
        bookUploadUtility.mergeUploadBook(book.title(), book);
        successes++;
      } catch (Exception e) {
        failedBooks += "\n\t" + book.title() + " because of " + e + ".";
      }
    }
    String proportionMsg =
        "Uploaded " + successes + " out of " + bookList.values().size() + " books.";
    response.setContentType("application/json");
    if (failedBooks.equals(UPLOAD_ERROR_MSG)) {
      response.getWriter().println(gson.toJson("Data Uploaded!"));
    } else {
      response.getWriter().println(gson.toJson(proportionMsg + failedBooks));
    }
  }
}
