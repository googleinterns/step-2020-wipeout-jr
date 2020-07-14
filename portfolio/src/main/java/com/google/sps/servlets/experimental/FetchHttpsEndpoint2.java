package com.google.sps.servlets.experimental;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/test-https2")
public class FetchHttpsEndpoint2 extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String query = "Harry";
    String url = String.format("https://www.googleapis.com/books/v1/volumes?country=US&q=%s", query);
    // this link contains different data than the book's selfLink
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer httpResponse = new StringBuffer(); // will contain the large string of information
    while ((inputLine = in.readLine()) != null) {
      httpResponse.append(inputLine);
    }
    in.close();
        
    response.getWriter().println("ResponseCode: " + responseCode);
    response.getWriter().println("ResponseContent: " + httpResponse.toString());
  }
}
