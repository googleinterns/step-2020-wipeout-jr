package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
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
    books = readBooks();
  }

  private HashMap<Integer, Book> readBooks() throws ServletException {
    HashMap<Integer, Book> allBooks = new HashMap<Integer, Book>();
    String path = "/WEB-INF/"
        + "20_books.csv";
    try (Scanner scanner =
             new Scanner(getServletContext().getResourceAsStream(path)).useDelimiter("\\Z")) {
      String content = scanner.next().replaceAll("[\\r\\n]+", "");
      String[] lines = content.split("NEXTBOOK"); // lines[i] represents one row of the file

      String current_title = "";
      Book.Builder current_builder = Book.builder().title("null");
      int currentId = 0;

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
            allBooks.put(currentId++, book);
          }
          // start building new book
          current_builder = Book.builder().title(title).genre(genre).addReview(review);
          current_title = title;
        }
      }
      Book book = current_builder.build();
      allBooks.put(currentId, book);
    } catch (Exception e) {
      throw new ServletException("Error reading CSV file", e);
    }
    return allBooks;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(books);
    response.getWriter().println(json);
  }
}
