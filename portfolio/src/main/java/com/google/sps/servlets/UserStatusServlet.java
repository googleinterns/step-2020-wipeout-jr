package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.UserDaoDatastore;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserStatusServlet gets the current logged in/logged out status of the user
 */
@WebServlet("/user-status")
public class UserStatusServlet extends HttpServlet {
  private String toJson(String status) {
    Gson gson = new Gson();
    return gson.toJson(status);
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
        UserDaoDatastore storage = new UserDaoDatastore();
        storage.upload(); //uploads the user's info to the datastore
      response.getWriter().println(toJson("Logged In"));

    } else {
      response.getWriter().println(toJson("Logged Out"));
    }
  }
}
