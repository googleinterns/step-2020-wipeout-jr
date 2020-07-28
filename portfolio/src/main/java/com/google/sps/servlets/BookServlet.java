package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns a list of the last 20 book objects uploaded to the Datastore.
 * Used to get the list of books for the homepage.
 */
@WebServlet("/books")
public class BookServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    BookDao bookDao = new BookDaoDatastore();
    List<Book> bookList = bookDao.getBookList();
    String json = gson.toJson(bookList);
    response.getWriter().println(json);
  }
}

