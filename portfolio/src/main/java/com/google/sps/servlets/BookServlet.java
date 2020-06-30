package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns book titles and reviews as a JSON hashmap, with IDs, e.g. {4:[{"title": Othello,
 * "reviews": {"Nice", "Bad"}}]}
 */
@WebServlet("/book-data")
public class BookServlet extends HttpServlet {
  private Map<Integer, Book> books;

  @Override
  public void init() throws ServletException {

    BookReader reader = new BookReader(getServletContext().getResourceAsStream("/WEB-INF/20_books.csv"));
    books = reader.makeBookList();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(books);
    response.getWriter().println(json);
  }
}
