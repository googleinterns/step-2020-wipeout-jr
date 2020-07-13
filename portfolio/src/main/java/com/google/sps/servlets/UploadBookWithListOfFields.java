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
import com.google.appengine.api.datastore.Text;
import com.google.sps.data.Book;
import com.google.sps.data.BookAPI;
import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.BookReader;
import com.google.sps.data.FullBook;
import com.google.sps.data.RequestJson;
import java.io.IOException;
import java.lang.Class;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
This servlet takes fields (specified in @Code{NAME_OF_FIELDS}) from the jQuery,
using the Fetch API, where they are uploaded to Google Cloud Datastore.
*/
@WebServlet("/data-upload")
public class UploadBookWithListOfFields extends HttpServlet {
  private static final String ENTITY_KIND = "Book";
  private static final String PAGE_REDIRECT = "/index.html";
  private static final String TIMESTAMP_PROP = "timeStamp";

  private Map<Integer, Book> bookList;

  @Override
  public void init() throws ServletException {
    try {
      BookReader reader = new BookReader(getServletContext().getRealPath("/WEB-INF/20_books.csv"));
      bookList = reader.makeBookList();
    } catch (Exception ex) {
      throw new ServletException("Error reading CSV file", ex);
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    for (Map.Entry<Integer, Book> bookEntry : bookList.entrySet()) {
      BookAPI BookAPI = new BookAPI();
      ArrayList<FullBook> bookResultList =
          BookAPI.search(bookEntry.getValue().title(), 1); // how many results you want
      //try{
        FullBook topResult = bookResultList.get(0);
              //results are soley from the Book API, need to integrate with the Bookreader!!!

        long timeStamp = System.currentTimeMillis();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity bookEntity = new Entity(ENTITY_KIND);
        for (Field f : topResult.getClass().getDeclaredFields()) {
            String field = f.getName();
            try {
            String fieldType = f.getType().getTypeName();
            if (fieldType.equals("java.lang.String")) {
                String tempValue = topResult.getStringField(field);
                if (tempValue.getBytes().length >= 1500) {
                bookEntity.setProperty(field, new Text(tempValue));
                } else {
                bookEntity.setProperty(field, tempValue);
                }
            } else if (fieldType.equals("java.util.ArrayList")) {
                bookEntity.setProperty(field, topResult.getArrayField(field));
            } else {
                bookEntity.setProperty(field, "Undefined");
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        datastore.put(bookEntity);
      //}
    //   catch(NullPointerException e){
    //       e.printStackTrace();
    //   }
    }

    response.setContentType("application/json");
    response.getWriter().println("Data uploaded!");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    long timeStamp = System.currentTimeMillis();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity bookEntity = new Entity(ENTITY_KIND);
    for (BookFieldsEnum field : BookFieldsEnum.values()) {
      String jsProperty = field.getJSProperty();
      String newProperty = request.getParameter(jsProperty);
      /*if null we still want the property to exist even if empty because it will make retrieving
       * properties easier */
      if (newProperty == null) {
        bookEntity.setProperty(jsProperty, "Undefined");
      } else if (newProperty.getBytes().length >= 1500) {
        bookEntity.setProperty(jsProperty, new Text(newProperty));
      } else {
        bookEntity.setProperty(jsProperty, newProperty);
      }
    }
    bookEntity.setProperty(TIMESTAMP_PROP, timeStamp);

    datastore.put(bookEntity);

    response.setContentType("text/html;");
    response.sendRedirect(PAGE_REDIRECT);
  }
}

