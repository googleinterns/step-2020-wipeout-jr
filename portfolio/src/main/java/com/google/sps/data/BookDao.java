package com.google.sps.data;
import com.google.sps.data.Book;
import java.util.List;

// Given an instance of Book, persists it in the datastore.
public interface BookDao {
  void create(Book book);
  Book getEntity(String isbn);
  List<Book> getBookList();
  void update(Book book);
  void delete(String isbn);
}

