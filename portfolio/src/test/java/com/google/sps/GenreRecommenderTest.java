package com.google.sps;

import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import com.google.sps.data.GenreRecommender;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This is a test class for BookReader.java
 * It only tests the basic functionality for reading from a CSV file.
 * It does not check that the results match those from the Books API.
 */
@RunWith(JUnit4.class)
public final class GenreRecommenderTest {

  private Map<Integer, Book> bookList;

  @Test
  public void basicSanityTests() {
    try {
      BookReader reader = new BookReader("src/test/java/com/google/sps/testFile.csv");
      bookList = reader.makeBookList();
    } catch (Exception ex) {
      Assert.fail("Could not open file. " + ex);
    }
    GenreRecommender rec = new GenreRecommender(new ArrayList<>(bookList.values()));
    Book gatsby = bookList.get(0);
    Book harryPotter = bookList.get(1);
    Book harryPotter2 = bookList.get(2);
    Book emptyBook = Book.builder().title("blank book").genre(new HashSet<>()).addReview("no review").build();

    // Check getGenres with valid and invalid inputs:
    Assert.assertEquals(rec.getGenres(gatsby), 
                        ImmutableSet.copyOf(new HashSet<>(Arrays.asList("fiction", "tragedy"))));
    Assert.assertEquals(rec.getGenres(harryPotter), 
                        ImmutableSet.copyOf(new HashSet<>(Arrays.asList("fantasy"))));
    Assert.assertEquals(rec.getGenres(emptyBook), ImmutableSet.of());

    // Check getBooksWithExactGenres with valid and invalid inputs:
    Assert.assertEquals(rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("fiction", "tragedy"))),
                        new HashSet<>(Arrays.asList(gatsby)));
    Assert.assertEquals(rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("fantasy"))),
                        new HashSet<>(Arrays.asList(harryPotter, harryPotter2)));
    Assert.assertEquals(rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("tragedy"))),
                        ImmutableSet.of());
  }
}
