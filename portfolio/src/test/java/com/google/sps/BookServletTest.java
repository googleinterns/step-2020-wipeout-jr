package com.google.sps;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.servlets.BookServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Test class for BookServlet class
 */
@RunWith(JUnit4.class)
public final class BookServletTest extends Mockito {
  private BookDao bookDao = new BookDaoDatastore();
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final BookServlet bookServlet = new BookServlet();
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private static final Book DEFAULT_1 =
      newBook("First Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891234");
  private static final Book DEFAULT_2 =
      newBook("Second Example Book", "Information", "Wipeout Jr.", "Testing", "1234567891235");

  @Before
  public void setUp() {
    helper.setUp();
    bookDao.create(DEFAULT_1);
    bookDao.create(DEFAULT_2);
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

  @Test
  public void correctContentType() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(printWriter);

    bookServlet.doGet(request, response);

    verify(response).setContentType("application/json");
  }

  @Test
  public void getListOfBooks() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    bookServlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().contains("First Example Book"));
    Assert.assertTrue(stringWriter.toString().contains("Second Example Book"));
  }
}

