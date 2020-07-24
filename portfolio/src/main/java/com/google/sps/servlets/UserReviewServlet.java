// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.sps.data.Review;
import com.google.sps.data.ReviewDaoDatastore;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* Servlet gets all of a user's reviews by email
*/
@WebServlet("/User-review")
public class UserReviewServlet extends HttpServlet {
  private ReviewDaoDatastore reviewDao = new ReviewDaoDatastore();
  private Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
        String email = userService.getCurrentUser().getEmail(); // user email is used as id
        ImmutableSet<Review> userReviews = reviewDao.getAllByEmail(email);
        response.getWriter().println(gson.toJson(userReviews));
    }
  }
}
