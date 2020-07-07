package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/singleBookById")
public class SingleBookServlet extends HttpServlet {
  Map<Integer, Book> bookList;
  @Override
  public void init() throws ServletException {
    try {
      BookReader reader = new BookReader(getServletContext().getRealPath("/WEB-INF/20_books.csv"));
      bookList = reader.makeBookList();
    } catch (Exception ex) {
      throw new ServletException("Error reading CSV file", ex);
    }
  }
  private static String toJSON(Book book) {
    Gson gson = new Gson();
    return gson.toJson(book);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int bookId = Integer.parseInt(request.getParameter("id"));
    Book book = bookList.get(bookId);

    String jsonBook = toJSON(book);
    response.setContentType("application/json;");
    response.getWriter().println(jsonBook);
  }
}
