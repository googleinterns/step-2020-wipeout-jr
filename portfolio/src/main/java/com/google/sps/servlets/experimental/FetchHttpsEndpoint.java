package com.google.sps.servlets.experimental;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import java.net.URL;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test-https")
public class FetchHttpsEndpoint extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		URLFetchService urlFetchService = URLFetchServiceFactory.getURLFetchService();
		FetchOptions fetchOptions = FetchOptions.Builder.withDefaults();
		fetchOptions.validateCertificate(); // This is needed since the URL is https.

		HTTPRequest httpRequest = new HTTPRequest(new URL("https://www.googleapis.com/books/v1/volumes?country=US&q=Harry"),
                                    HTTPMethod.GET, fetchOptions);
		HTTPResponse httpResponse = urlFetchService.fetch(httpRequest);
		int responseCode = httpResponse.getResponseCode();
        
        response.getWriter().println("ResponseCode: " + responseCode);
        response.getWriter().println("ResponseContent: " + new String(httpResponse.getContent()));
  }
}
