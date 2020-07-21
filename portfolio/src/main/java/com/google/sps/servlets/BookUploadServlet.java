package com.google.sps.servlets;

import com.google.common.collect.ImmutableMap;
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
public class BookUploadServlet extends HttpServlet {
  private final BookUploadUtility bookUploadUtility = new BookUploadUtility();
  private Map<Integer, Book> bookList;

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
    String failedBooks = "\n Could not upload: ";
    int successes = 0;
    for (Book book : bookList.values()) {
      try {
        bookUploadUtility.mergeUploadBook(book.title(), book);
        successes++;
      } catch (Exception e) {
        failedBooks += "\n\t" + book.title() + " because of " + e + ".";
      }
    }
    String proportion = "Uploaded " + successes + " out of " + bookList.values().size() + " books.";
    response.setContentType("application/json");
    if (failedBooks.equals("Could not upload: ")) {
      response.getWriter().println("Data Uploaded!");
    } else {
      response.getWriter().println(proportion + failedBooks);
    }
  }
}

