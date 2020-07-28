package com.google.sps;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.servlets.AdminBookUploadServlet;
import com.google.sps.servlets.BookServlet;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
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
  private final AdminBookUploadServlet uploadServlet = new AdminBookUploadServlet();
  private final BookServlet bookServlet = new BookServlet();
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
    try{
      uploadServlet.doGet(request,response);
    }catch(IOException e){
        Assert.fail();
    }
  }

  @After
  public void tearDown() {
    helper.tearDown();
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
    Assert.assertTrue(stringWriter.toString().contains("A Court of Wings and Ruin"));
  }

//   @Test
//   public void updateExistingReview() throws Exception {
//     StringWriter stringWriter = new StringWriter();
//     PrintWriter printWriter = new PrintWriter(stringWriter);
//     Mockito.when(response.getWriter()).thenReturn(printWriter);

//     Review review1 = Review.create(REVIEW_1, ISBN, EMAIL);
//     reviewDao.uploadNew(review1);

//     Review review2 = Review.create(REVIEW_2, ISBN, EMAIL);
//     reviewDao.updateReview(review2);

//     bookServlet.doGet(request, response);

//     Assert.assertTrue(stringWriter.toString().contains("Devotion to a cause"));
//   }

//   @Test
//   public void userLoggedOut() throws Exception {
//     helper.setEnvIsLoggedIn(false);

//     StringWriter stringWriter = new StringWriter();
//     PrintWriter printWriter = new PrintWriter(stringWriter);
//     Mockito.when(response.getWriter()).thenReturn(printWriter);

//     bookServlet.doGet(request, response);

//     Assert.assertTrue(stringWriter.toString().isEmpty());
//   }
}