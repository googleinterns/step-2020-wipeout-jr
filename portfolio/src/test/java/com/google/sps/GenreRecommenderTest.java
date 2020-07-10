package com.google.sps;

import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import com.google.sps.data.BookReader;
import com.google.sps.data.GenreRecommender;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
  private List<Book> bookList;

  // ONE GENRE BOOKS:
  private static final Book AUTOBIOGRAPHY_BOOK =
      Book.builder()
          .title("book about me")
          .genre(new HashSet<>(Arrays.asList("autobiography")))
          .addReview("N/A")
          .build();
  private static final Book BIOGRAPHY_BOOK = Book.builder()
                                                 .title("book about him")
                                                 .genre(new HashSet<>(Arrays.asList("biography")))
                                                 .addReview("N/A")
                                                 .build();
  private static final Book CRIME_BOOK = Book.builder()
                                             .title("book about death")
                                             .genre(new HashSet<>(Arrays.asList("crime")))
                                             .addReview("N/A")
                                             .build();
  private static final Book HORROR_BOOK = Book.builder()
                                              .title("book about ghosts")
                                              .genre(new HashSet<>(Arrays.asList("horror")))
                                              .addReview("N/A")
                                              .build();

  // TWO GENRE BOOKS:
  private static final Book HORROR_CRIME_BOOK =
      Book.builder()
          .title("ghost murder book")
          .genre(new HashSet<>(Arrays.asList("horror", "crime")))
          .addReview("N/A")
          .build();
  private static final Book BIOGRAPHY_HUMOR_BOOK =
      Book.builder()
          .title("funny guy I knew")
          .genre(new HashSet<>(Arrays.asList("biography", "humor")))
          .addReview("N/A")
          .build();
  private static final Book CRIME_HUMOR_BOOK =
      Book.builder()
          .title("dark death")
          .genre(new HashSet<>(Arrays.asList("crime", "humor")))
          .addReview("N/A")
          .build();
  private static final Book EPIC_MYTHOLOGY_BOOK =
      Book.builder()
          .title("gods and legends")
          .genre(new HashSet<>(Arrays.asList("epic", "mythology")))
          .addReview("N/A")
          .build();
  private static final Book EPIC_MYTHOLOGY_BOOK_2 =
      Book.builder()
          .title("gods vs legends")
          .genre(new HashSet<>(Arrays.asList("epic", "mythology")))
          .addReview("N/A")
          .build();
  private static final Book EPIC_MYTHOLOGY_BOOK_3 =
      Book.builder()
          .title("gods become legends")
          .genre(new HashSet<>(Arrays.asList("epic", "mythology")))
          .addReview("N/A")
          .build();

  @Test
  public void getGenresTests() {
    bookList = new ArrayList<Book>();
    Collections.addAll(
        bookList, AUTOBIOGRAPHY_BOOK, BIOGRAPHY_BOOK, CRIME_HUMOR_BOOK, EPIC_MYTHOLOGY_BOOK);
    GenreRecommender rec = new GenreRecommender(bookList);

    Assert.assertEquals(
        new HashSet<>(Arrays.asList("autobiography")), rec.getGenres(AUTOBIOGRAPHY_BOOK));
    Assert.assertEquals(new HashSet<>(Arrays.asList("biography")), rec.getGenres(BIOGRAPHY_BOOK));

    Assert.assertEquals(
        new HashSet<>(Arrays.asList("crime", "humor")), rec.getGenres(CRIME_HUMOR_BOOK));
    Assert.assertEquals(
        new HashSet<>(Arrays.asList("epic", "mythology")), rec.getGenres(EPIC_MYTHOLOGY_BOOK));

    Assert.assertEquals(ImmutableSet.of(), rec.getGenres(HORROR_CRIME_BOOK));
    Assert.assertEquals(ImmutableSet.of(), rec.getGenres(BIOGRAPHY_HUMOR_BOOK));
  }

  @Test
  public void getBooksWithExactGenresTests() {
    bookList = new ArrayList<Book>();
    Collections.addAll(bookList, CRIME_BOOK, HORROR_BOOK, CRIME_HUMOR_BOOK, EPIC_MYTHOLOGY_BOOK,
        HORROR_CRIME_BOOK, BIOGRAPHY_HUMOR_BOOK, EPIC_MYTHOLOGY_BOOK_2, EPIC_MYTHOLOGY_BOOK_3);
    GenreRecommender rec = new GenreRecommender(bookList);

    Assert.assertEquals(new HashSet<>(Arrays.asList(CRIME_BOOK)),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("crime"))));
    Assert.assertEquals(new HashSet<>(Arrays.asList(HORROR_BOOK)),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("horror"))));

    Assert.assertEquals(new HashSet<>(Arrays.asList(CRIME_HUMOR_BOOK)),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("humor", "crime"))));
    Assert.assertEquals(new HashSet<>(Arrays.asList(HORROR_CRIME_BOOK)),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("horror", "crime"))));

    Assert.assertEquals(new HashSet<>(Arrays.asList(
                            EPIC_MYTHOLOGY_BOOK, EPIC_MYTHOLOGY_BOOK_2, EPIC_MYTHOLOGY_BOOK_3)),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("epic", "mythology"))));

    Assert.assertEquals(ImmutableSet.of(),
        rec.getBooksWithExactGenres(new HashSet<>(Arrays.asList("epic", "crime"))));
  }
}
