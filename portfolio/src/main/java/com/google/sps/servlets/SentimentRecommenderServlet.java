package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.data.BookReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sentimentRecommendationById")
public class SentimentRecommenderServlet extends HttpServlet {
  private Map<Book, List<Book>> hardCodedMappings;
  private BookDao bookDao;
  private static final List<String> ISBN_LIST = Arrays.asList("9780062310712", "9780062348654",
      "9780062498557", "9780141363981", "9780316341646", "9780393609103", "9780525506348",
      "9780525555377", "9780735212206", "9780735224315", "9780765387486", "9780812985405",
      "9781250095251", "9781442468405", "9781473582231", "9781501160783", "9781501174827",
      "9781524714703", "9781619634497", "9781681195803");

  @Override
  public void init() {
    bookDao = new BookDaoDatastore();
    makeHardCodedMappings();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    response.setContentType("application/json");
    Book book = bookDao.getEntity(request.getParameter("isbn"));
    response.getWriter().println(gson.toJson(hardCodedMappings.get(book)));
  }

  private void makeHardCodedMappings() {
    hardCodedMappings = new HashMap<Book, List<Book>>();

    // placeholder random values
    for (String isbn : ISBN_LIST) {
      Book myBook = bookDao.getEntity(isbn);
      hardCodedMappings.put(myBook, getRandomBooks(myBook, 3));
    }

    // actual example for demo purpose:
    hardCodedMappings.put(bookDao.getEntity("9781501174827"),
        Arrays.asList(bookDao.getEntity("9780735224315"), bookDao.getEntity("9781524714703"),
            bookDao.getEntity("9781250095251")));
  }

  private List<Book> getRandomBooks(Book myBook, int n) {
    List<Book> books = new ArrayList<Book>();
    Set<Book> uniqueBooks = new HashSet<Book>();
    Random rand = new Random();

    uniqueBooks.add(myBook);
    while (uniqueBooks.size() <= n) {
      uniqueBooks.add(bookDao.getEntity(ISBN_LIST.get(rand.nextInt(20))));
    }
    uniqueBooks.remove(myBook);
    for (Book book : uniqueBooks) {
      books.add(book);
    }
    return books;
  }
}
