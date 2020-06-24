package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
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
  private Collection<String> test;

  @Override
  public void init() throws ServletException {
    books = new ArrayList<>();
    readBooks();
  }

  private void readBooks() throws ServletException {
    String path = "/WEB-INF/"
        + "20_books.csv";
    try (Scanner scanner =
             new Scanner(getServletContext().getResourceAsStream(path)).useDelimiter("\\Z")) {
      String content = scanner.next().replaceAll("[\\r\\n]+", "");
      String[] lines = content.split("NEXTBOOK"); // lines[i] represents one row of the file

      String current_title = "";
      Book.Builder current_builder = Book.builder().title("null");

      for (int i = 1; i < lines.length; i++) {
        String[] cells = lines[i].split(",");
        String title = cells[0];
        String genre = cells[8];
        String review = cells[13];
        if (current_title.equals(title)) {
          // add review:
          current_builder.addReview(review);
        } else {
          // close old book:
          if (i != 1) {
            Book book = current_builder.build();
            books.add(book);
          }
          // start building new book
          current_builder = Book.builder().title(title).genre(genre).addReview(review);
          current_title = title;
        }
      }
      Book book = current_builder.build();
      books.add(book);
    } catch (Exception e) {
      throw new ServletException("Error reading CSV file", e);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response){
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(books);
    response.getWriter().println(json);
  }
}
