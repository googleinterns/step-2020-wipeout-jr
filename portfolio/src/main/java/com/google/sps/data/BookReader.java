package com.google.sps.data;
import com.google.common.collect.ImmutableMap; 
import com.google.sps.data.Book;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;
import javax.servlet.ServletException;

/**
 * Class that takes in a CSV FileName as the constructor parameter
 * and populates a list of Books with title, genre, and reviews.
 */
public class BookReader {
  String path;
  public BookReader(String path) {
    this.path = path;
  }

  public ImmutableMap<Integer, Book> makeBookList() throws ServletException{
    HashMap<Integer, Book> allBooks = new HashMap<Integer, Book>();
    try (Scanner scanner = new Scanner(new File(path)).useDelimiter("\\Z")) {
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
    } catch (Exception ex) {
      throw new ServletException("Error reading CSV file", ex);
    }
    return ImmutableMap.copyOf(allBooks);
  }
}
