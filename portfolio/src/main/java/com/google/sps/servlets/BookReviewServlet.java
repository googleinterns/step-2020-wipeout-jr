package com.google.sps.servlets;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.sps.data.Review;
import com.google.sps.data.ReviewDao;
import com.google.sps.data.ReviewDaoDatastore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Gets the reviews associated with a particular book
@WebServlet("/reviews")
public class BookReviewServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType("application/json");
    ReviewDao reviewDao = new ReviewDaoDatastore();
    ImmutableSet<Review> reviews = reviewDao.getAllByISBN(request.getParameter("isbn"));
    String json = gson.toJson(reviews);
    response.getWriter().println(json);
  }
}
