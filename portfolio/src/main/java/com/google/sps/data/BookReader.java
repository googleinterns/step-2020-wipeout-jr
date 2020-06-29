package com.google.sps.data;
import com.google.sps.data.Book;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File; 
import java.util.Scanner;

/**
 * Class that takes in a CSV FileName as the constructor parameter
 * and populates a list of Books with title, genre, and reviews.
 */
public class BookReader {
  String path;
  public BookReader(String path) {
    this.path = path;
  }




  public ArrayList<Book> makeBookList(){
    ArrayList<Book> listOfBooks = new ArrayList<Book>();
      try (Scanner scanner = new Scanner(new File(path)).useDelimiter("\\Z")){
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
            listOfBooks.add(book);
          }
          // start building new book
          current_builder = Book.builder().title(title).genre(genre).addReview(review);
          current_title = title;
        }
      }
      Book book = current_builder.build();
      listOfBooks.add(book);
    } catch(IOException ex){
        System.out.println (ex.toString());
        System.out.println("Could not find file " + path);
    }
    return listOfBooks;
  }
}