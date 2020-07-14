package com.google.sps;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.servlets.UserStatusServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
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
 * Test class for UserStatusServlet
 */
@RunWith(JUnit4.class)
public final class UserStatusServletTest extends Mockito {
  private final UserStatusServlet servlet = new UserStatusServlet();
  private final HttpServletRequest request = mock(HttpServletRequest.class);
  private final HttpServletResponse response = mock(HttpServletResponse.class);
  private static final String EMAIL = "abc@xyz.com";
  private static final String DOMAIN = "test.com";

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalUserServiceTestConfig())
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

    new UserStatusServlet().doGet(request, response);

    verify(response).setContentType("application/json");
  }

  @Test
  public void userLoggedIn() throws Exception {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().contains("Logged In"));
  }
  
  @Test
  public void userLoggedOut() throws Exception {
    helper.setEnvIsLoggedIn(false);

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    Mockito.when(response.getWriter()).thenReturn(printWriter);

    servlet.doGet(request, response);

    Assert.assertTrue(stringWriter.toString().contains("Logged Out"));
  }
}
