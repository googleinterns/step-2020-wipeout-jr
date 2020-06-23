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
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/datastore-books")
public class BookDatastore extends HttpServlet {
  static final String GENRE_PARAM = "genre-input";
  static final String AUTH_PARAM = "author-input";
  static final String TITLE_PARAM = "title-input";
  static final int BOOK_AMOUNT = 20;

  static final String GENRE_PROP = "genre";
  static final String AUTH_PROP = "author";
  static final String TITLE_PROP = "title";
  static final String TIMESTAMP_PROP = "timeStamp";

  static final String ENTITY_KIND = "Book";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query(ENTITY_KIND).addSort(TIMESTAMP_PROP, SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    int numberLoaded = 0;

    ArrayList<Book> intermLibrary = new ArrayList<Book>();
    for (Entity entity : results.asIterable()) {
      if (numberLoaded < BOOK_AMOUNT) {
        long id = entity.getKey().getId();
        String author = (String) entity.getProperty(AUTH_PROP);
        String genre = (String) entity.getProperty(GENRE_PROP);
        String title = (String) entity.getProperty(TITLE_PROP);
        long timeStamp = (long) entity.getProperty(TIMESTAMP_PROP);

        Book book = new Book(id, title, author, genre, timeStamp);
        intermLibrary.add(book);
        numberLoaded++;
      } else {
        break;
      }
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(intermLibrary));
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String title = request.getParameter(TITLE_PARAM);
    String author = request.getParameter(AUTH_PARAM);
    String genre = request.getParameter(GENRE_PARAM);
    long timeStamp = System.currentTimeMillis();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity bookEntity = new Entity(ENTITY_KIND);
    bookEntity.setProperty(TITLE_PROP, title);
    bookEntity.setProperty(AUTH_PROP, author);
    bookEntity.setProperty(GENRE_PROP, genre);
    bookEntity.setProperty(TIMESTAMP_PROP, timeStamp);

    datastore.put(bookEntity);

    response.setContentType("text/html;");
    response.sendRedirect("/home.html");
  }
}

