package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns book titles and reviews as a JSON array, e.g. [{"title": Othello, "reviews": {"Nice",
 * "Bad"}}]
 */
@WebServlet("/book-data")
public class BookServlet extends HttpServlet {
  private Collection<Book> books;

  @Override
  public void init() throws ServletException {
    BookReader reader = new BookReader(System.getProperty("user.home") + "/step-2020-wipeout-jr/portfolio/src/main/webapp/WEB-INF/20_books.csv");
    books = reader.makeBookList();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(books);
    response.getWriter().println(json);
  }
}
