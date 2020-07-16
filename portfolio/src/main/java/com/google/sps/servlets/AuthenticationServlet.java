package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.User;
import com.google.sps.data.UserDao;
import com.google.sps.data.UserDaoDatastore;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthenticationServlet allows users to log in and out using their email
 */
@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {
  private UserDaoDatastore userStorage = new UserDaoDatastore();

  @Override
  public void init() {
    UserDaoDatastore userStorage = new UserDaoDatastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    String urlToRedirectTo = "/";

    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      // If user has not set a nickname, redirect to nickname page
      String nickname = getUserNickname(userService.getCurrentUser().getEmail());

      if (nickname.equals("")) {
        response.sendRedirect("/user-info");
        return;
      }

      String userEmail = userService.getCurrentUser().getEmail();
      String logoutUrl = userService.createLogoutURL(urlToRedirectTo);

      response.getWriter().println("<p>Hello " + userEmail + "!</p>");
      response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");

    } else {
      String loginUrl = userService.createLoginURL(urlToRedirectTo);

      response.getWriter().println("<p>Hello stranger.</p>");
      response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
    }
  }

  /** Returns the nickname of the user with id, or null if the user has not set a nickname. */
  private String getUserNickname(String id) {
    Optional<User> user = userStorage.get(id);
    if (user.isPresent()) {
      return user.get().nickname();
    }
    return "";
  }
}
