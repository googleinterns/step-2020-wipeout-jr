package com.google.sps;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import com.google.sps.data.ReviewDao;
import com.google.sps.data.ReviewDaoDatastore;
import com.google.sps.servlets.UserReviewServlet;
import java.io.File;
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
 * Test class for UserReviewServlet
 */
@RunWith(JUnit4.class)
public final class UserReviewServletTest extends Mockito {
  private ReviewDaoDatastore reviewDao = new ReviewDaoDatastore();
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private final UserReviewServlet servlet = new UserReviewServlet();
  private static final String EMAIL = "unknown@email.com1";
  private static final String ISBN = "1111111111111";
  private static final String REVIEW_1 =
      "I am sick to death of books like this that are nothing but overblown cash-guzzlers";
  private static final String REVIEW_2 =
      "Devotion to a cause to a vision of the future might ultimately be a crack in the armor of a small town.";
  private static final String DOMAIN = "test.com";

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalUserServiceTestConfig(), new LocalDatastoreServiceTestConfig())
                                                    .setEnvIsLoggedIn(true)
                                                    .setEnvEmail(EMAIL)
                                                    .setEnvAuthDomain(DOMAIN);

  @Before
  public void setUp() {
    helper.setUp();
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

    servlet.doGet(request, response);

    verify(response).setContentType("application/json");
  }

  @Test
  public void uploadNewReview() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    Review review = Review.create(REVIEW_1, ISBN, EMAIL);
    reviewDao.uploadNew(review);

    servlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().contains("cash-guzzlers"));
  }

  @Test
  public void updateExistingReview() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    Review review1 = Review.create(REVIEW_1, ISBN, EMAIL);
    reviewDao.uploadNew(review1);

    Review review2 = Review.create(REVIEW_2, ISBN, EMAIL);
    reviewDao.updateReview(review2);

    servlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().contains("Devotion to a cause"));
  }

  @Test
  public void userLoggedOut() throws Exception {
    helper.setEnvIsLoggedIn(false);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().isEmpty());
  }
}
