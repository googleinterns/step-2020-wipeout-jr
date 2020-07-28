package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Gets information for one book from Datastore.
 * Book detail component calls this servlet and provides
 * a book's ISBN to load on book detail page.
 */
@WebServlet("/book")
public class SingleBookServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    BookDao bookDao = new BookDaoDatastore();
    Book book = bookDao.getEntity(request.getParameter("isbn"));
    String json = gson.toJson(book);
    response.getWriter().println(json);
  }
}

