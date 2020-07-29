package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
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
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType("application/json");
    BookDao bookDao = new BookDaoDatastore();
    List<Book> bookList = bookDao.getBookList();
    GenreRecommender rec = new GenreRecommender(new ArrayList(bookList));

    Book book = bookDao.getEntity(request.getParameter("isbn"));
    List<Book> recommendedBooks = rec.getTopNMatches(book, 3);
    String json = gson.toJson(recommendedBooks);
    response.getWriter().println(json);
  }
}
