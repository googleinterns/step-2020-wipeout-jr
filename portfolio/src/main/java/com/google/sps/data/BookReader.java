package com.google.sps.data;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

/**
 * Class that takes in an InputStream as the constructor parameter
 * and populates a list of Books with title, genre, and reviews.
 */
public class BookReader {
  private final File file;
  public BookReader(String path) {
    this.file = new File(path);
  }

  public ImmutableMap<Integer, Book> makeBookList() throws IOException {
    ImmutableMap.Builder<Integer, Book> allBooks = new Builder<Integer, Book>();
    try (Scanner scanner = new Scanner(file, "utf-8").useDelimiter("\\Z")) {
      String content = scanner.next().replaceAll("[\\r\\n]+", "");
      String[] lines = content.split("NEXTBOOK"); // lines[i] represents one row of the file

      String current_title = "";
      Book.Builder current_builder = Book.builder().title("null");
      int currentId = 0;

      for (int i = 1; i < lines.length; i++) {
        String[] cells = lines[i].split(",");
        String title = cells[0];
        Set<String> genre = getGenreSet(cells[8]);
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
      throw new IOException("Error reading CSV file", ex);
    }
    return allBooks.build();
  }

  /**
   * Helper method that takes in the list of genres as a ";"-separated string
   * and returns a set of genres
   *
   * @param genreString: ";"-separated string of genres
   * @return ImmutableSet of genres
   */
  private ImmutableSet<String> getGenreSet(String genreString) {
    ImmutableSet.Builder<String> genres = new ImmutableSet.Builder<String>();
    for (String genre : genreString.toLowerCase().split(";")) {
      genres.add(genre);
    }
    return genres.build();
  }
}
