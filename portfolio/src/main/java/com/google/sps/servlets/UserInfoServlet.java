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
import com.google.gson.Gson;
import com.google.sps.data.User;
import com.google.sps.data.UserDao;
import com.google.sps.data.UserDaoDatastore;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user-info")
public class UserInfoServlet extends HttpServlet {
  private UserDaoDatastore userStorage = new UserDaoDatastore();
  private Gson gson = new Gson();

  @Override
  public void init() {
    UserDaoDatastore userStorage = new UserDaoDatastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
        String email = userService.getCurrentUser().getEmail(); // user email is used as id
        Optional<User> user = userStorage.get(email);
        if (user.isPresent()) { //if the user has been saved to datastore(has already set a nickname)
            response.getWriter().println(gson.toJson(user.get()));
        } else {
            User newUser = User.create(email,""); // placeholder user where the nickname is an empty string
            response.getWriter().println(gson.toJson(newUser));
        }

    } else {
      response.getWriter().println(gson.toJson("Logged Out"));
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    String nickname = request.getParameter("nickname");
    String email = userService.getCurrentUser().getEmail(); // user email is used as id

    //check that input is non-empty
    if (nickname.isEmpty()) {
        response.sendRedirect("/");
        return;
    }

    User newUser = User.create(email, nickname);
    userStorage.upload(newUser);

    response.sendRedirect("/#!/profile-page");
  }

  /**
   * Returns the nickname of the user with given email, or empty String if the user has not set a
   * nickname.
   */
  private String getUserNickname(String email) {
    Optional<User> user = userStorage.get(email);

    if (user.isPresent()) {
      return user.get().nickname();
    }
    return "";
  }
}
