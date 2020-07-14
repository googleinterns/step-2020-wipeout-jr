package com.google.sps;

import com.google.common.collect.ImmutableList;
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
  // ONE GENRE BOOKS:
  private static final Book AUTOBIOGRAPHY_BOOK =
      createBook("book about me", new String[] {"autobiography"});
  private static final Book BIOGRAPHY_BOOK =
      createBook("book about him", new String[] {"biography"});
  private static final Book CRIME_BOOK = createBook("book about death", new String[] {"crime"});
  private static final Book HORROR_BOOK = createBook("book about ghosts", new String[] {"horror"});

  // TWO GENRE BOOKS:
  private static final Book HORROR_CRIME_BOOK =
      createBook("ghost murder book", new String[] {"horror", "crime"});
  private static final Book BIOGRAPHY_HUMOR_BOOK =
      createBook("funny guy I knew", new String[] {"biography", "humor"});
  private static final Book CRIME_HUMOR_BOOK =
      createBook("dark death", new String[] {"crime", "humor"});
  private static final Book EPIC_MYTHOLOGY_BOOK =
      createBook("gods and legends", new String[] {"epic", "mythology"});
  private static final Book EPIC_MYTHOLOGY_BOOK_2 =
      createBook("gods vs legends", new String[] {"epic", "mythology"});
  private static final Book EPIC_MYTHOLOGY_BOOK_3 =
      createBook("gods become legends", new String[] {"epic", "mythology"});

  @Test
  public void getGenresTests() {
    List<Book> bookList = new ArrayList<Book>();
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
    List<Book> bookList = new ArrayList<Book>();
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

  @Test
  public void getTopNMatchesTest() {
    List<Book> bookList = new ArrayList<Book>();
    Collections.addAll(bookList, AUTOBIOGRAPHY_BOOK, BIOGRAPHY_BOOK, CRIME_BOOK, HORROR_BOOK, 
        CRIME_HUMOR_BOOK, EPIC_MYTHOLOGY_BOOK,
        HORROR_CRIME_BOOK, BIOGRAPHY_HUMOR_BOOK, 
        EPIC_MYTHOLOGY_BOOK_2, EPIC_MYTHOLOGY_BOOK_3);
    GenreRecommender rec = new GenreRecommender(bookList);

    // regular case:
    Assert.assertEquals((Arrays.asList(BIOGRAPHY_HUMOR_BOOK)),
        rec.getTopNMatches(BIOGRAPHY_BOOK, 1));
    Assert.assertEquals((Arrays.asList(CRIME_BOOK, HORROR_BOOK, CRIME_HUMOR_BOOK)),
        rec.getTopNMatches(HORROR_CRIME_BOOK, 3));
    Assert.assertEquals((Arrays.asList(CRIME_HUMOR_BOOK, HORROR_CRIME_BOOK)),
        rec.getTopNMatches(CRIME_BOOK, 2));
    Assert.assertEquals((Arrays.asList(EPIC_MYTHOLOGY_BOOK_3, EPIC_MYTHOLOGY_BOOK_2)),
        rec.getTopNMatches(EPIC_MYTHOLOGY_BOOK, 2));

    Assert.assertEquals(BIOGRAPHY_HUMOR_BOOK,
        rec.getTopNMatches(BIOGRAPHY_BOOK, 3).get(0));
    Assert.assertEquals(CRIME_BOOK,
        rec.getTopNMatches(HORROR_CRIME_BOOK, 7).get(0));

    // N >= # of books
    Assert.assertEquals(1,
        rec.getTopNMatches(BIOGRAPHY_BOOK, 15).size());
    Assert.assertEquals(2,
        rec.getTopNMatches(EPIC_MYTHOLOGY_BOOK, 9).size());
    
    // N <= 0
    Assert.assertEquals(ImmutableList.of(),
        rec.getTopNMatches(BIOGRAPHY_BOOK, 0));
    Assert.assertEquals(ImmutableList.of(),
        rec.getTopNMatches(BIOGRAPHY_BOOK, -78));

  }

  private static Book createBook(String title, String[] genres) {
    return Book.builder()
        .title(title)
        .genre(new HashSet<>(Arrays.asList(genres)))
        .addReview("N/A")
        .build();
  }
}
