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
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** This servlet takes fields (specified in @Code{NAME_OF_FIELDS}) from the jQuery,
using the Fetch API, where they are uploaded to Google Cloud Datastore.*/
@WebServlet("/data-upload")
public class UploadToDatastore extends HttpServlet {
  private static final String ENTITY_KIND = "Book";
  private static final String[] NAME_OF_FIELDS = {"title", "genre", "categories", "author",
      "language", "description", "infoLink", "pageCount", "publishedDate", "publisher",
      "maturityRating"};
  private static final String PAGE_REDIRECT = "/index.html";
  private static final String TIMESTAMP_PROP = "timeStamp";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long timeStamp = System.currentTimeMillis();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity bookEntity = new Entity(ENTITY_KIND);
    for (String field : NAME_OF_FIELDS) {
      bookEntity.setProperty(field, request.getParameter(field));
    }
    bookEntity.setProperty(TIMESTAMP_PROP, timeStamp);

    datastore.put(bookEntity);
    System.out.println("Book added");

    response.setContentType("text/html;");
    response.sendRedirect(PAGE_REDIRECT);
  }
}

