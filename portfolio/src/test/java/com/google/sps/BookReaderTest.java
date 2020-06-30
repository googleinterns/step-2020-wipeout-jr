package com.google.sps;

import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import java.io.File; 
import java.io.FileInputStream;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** This is a test class for BookReader.java
  * It only tests the basic functionality for reading from a CSV file.
  * It does not check that the results match those from the Books API.
*/
@RunWith(JUnit4.class)
public final class BookReaderTest {
  private final String BOOK_1 = "The Great Gatsby";
  private final String BOOK_2 = "Harry Potter and the Philosopher's Stone";
  private Map<Integer, Book> bookList;

  @Test
  public void BasicSanityTests() {
    try{
        File csv = new File("src/test/java/com/google/sps/testFile.csv");
        BookReader reader = new BookReader(new FileInputStream(csv));
        bookList = reader.makeBookList();
    } catch(Exception ex) {
        Assert.fail("Could not open file. " + ex);
    }
    String book1 = bookList.get(0).title();
    String book2 = bookList.get(1).title();
    // Check that titles match:
    Assert.assertEquals(BOOK_1, book1);
    Assert.assertEquals(BOOK_2, book2);
    // Check that # of reviews match:
    Assert.assertEquals(bookList.get(0).reviews().size(), 2);
    Assert.assertEquals(bookList.get(1).reviews().size(), 1);
  }
}
