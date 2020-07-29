package com.google.sps;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Book;
import com.google.sps.data.BookEntityParser;
import java.lang.Exception;
import java.lang.IllegalAccessException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
   This is a test class for BookEntityParserTest.java
    Tests the implementations ability to convert
    Entities into a Books with the correct fields
*/

@RunWith(JUnit4.class)
public final class BookEntityParserTest {
  private static final String ENTITY_KIND = "Test Book";
  private static final String TIME_STAMP = "timeStamp";
  private static final String TITLE = "title";
  private static final String GENRE = "genre";
  private static final String CATEGORIES = "categories";
  private static final String AUTHORS = "authors";
  private static final String LANGUAGE = "language";
  private static final String DESCRIPTION = "description";
  private static final String INFO_LINK = "infoLink";
  private static final String PAGE_COUNT = "pageCount";
  private static final String PUBLISHED_DATE = "publishedDate";
  private static final String PUBLISHER = "publisher";
  private static final String MATURITY_RATING = "maturityRating";
  private static final String THUMBNAIL = "thumbnail";
  private static final String IMAGE_LINKS = "imageLinks";
  private static final String ISBN = "ISBN_13";

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());
  private static final Book BOOK_1 =
      newBook("First Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891234");
  private static final Book BOOK_2 =
      newBook("Second Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891235");
  private static Entity ENTITY_1;
  private static Entity ENTITY_2;

  @Before
  public void setUp() {
    helper.setUp();
    ENTITY_1 =
        newEntity("First Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891234");
    ENTITY_2 =
        newEntity("Second Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891235");
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  private static Book newBook(
      String title, String category, String author, String genre, String isbn) {
    ArrayList<String> authors = new ArrayList<String>();
    authors.add(author);
    ArrayList<String> categories = new ArrayList<String>();
    categories.add(category);
    Set<String> genres = new HashSet<String>();
    genres.add(genre);
    Book.Builder builder = Book.builder()
                               .title(title)
                               .authors(authors)
                               .categories(categories)
                               .genre(genres)
                               .isbn(isbn);
    Book expected = builder.build();
    return expected;
  }

  private static Entity newEntity(
      String title, String category, String author, String genre, String isbn) {
    ArrayList<String> authors = new ArrayList<String>();
    authors.add(author);
    ArrayList<String> categories = new ArrayList<String>();
    categories.add(category);
    ArrayList<String> genres = new ArrayList<String>();
    genres.add(genre);

    Entity bookEntity = new Entity(KeyFactory.createKey(ENTITY_KIND, isbn));
    bookEntity.setProperty(TITLE, title);
    bookEntity.setProperty(GENRE, genres);
    bookEntity.setProperty(CATEGORIES, categories);
    bookEntity.setProperty(AUTHORS, authors);
    bookEntity.setProperty(ISBN, isbn);

    return bookEntity;
  }

  @Test
  public void parseBook1() {
    Book expected = BOOK_1;
    Book actual = BookEntityParser.parseBook(ENTITY_1);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseBook2() {
    Book expected = BOOK_2;
    Book actual = BookEntityParser.parseBook(ENTITY_2);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseEntity1() {
    Entity expected = ENTITY_1;
    Entity actual =
        BookEntityParser.parseEntity(BOOK_1, KeyFactory.createKey(ENTITY_KIND, BOOK_1.isbn()));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void parseEntity2() {
    Entity expected = ENTITY_2;
    Entity actual =
        BookEntityParser.parseEntity(BOOK_2, KeyFactory.createKey(ENTITY_KIND, BOOK_2.isbn()));
    Assert.assertEquals(expected, actual);
  }
}

