package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import com.google.sps.data.GenreRecommender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/bookRecommendationById")
public class RecommendBookServlet extends HttpServlet {
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
  private static String toJSON(List<Book> books) {
    Gson gson = new Gson();
    return gson.toJson(books);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int bookId = Integer.parseInt(request.getParameter("id"));
    Book book = bookList.get(bookId);
    GenreRecommender rec = new GenreRecommender(new ArrayList(bookList.values()));
    List<Book> recommendedBooks = rec.getTopNMatches(book, 3);

    String jsonBooks = toJSON(recommendedBooks);
    response.setContentType("application/json;");
    response.getWriter().println(jsonBooks);
  }
}
