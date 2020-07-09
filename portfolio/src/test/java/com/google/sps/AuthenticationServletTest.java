package com.google.sps;

import com.google.sps.servlets.AuthenticationServlet;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Test class for AuthenticationServlet
 */
@RunWith(JUnit4.class)
public final class AuthenticationServletTest extends Mockito{

  @Test
  public void correctContentType() throws Exception{
      HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        new AuthenticationServlet().doGet(request, response);

        Assert.assertEquals("text/html", "text/html");
  }
}