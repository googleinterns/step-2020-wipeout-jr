package com.google.sps.data;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import java.util.Set;
import com.google.sps.data.Book;
import com.google.sps.data.Review;

public interface ReviewDao {
  void loadAll(Book book);
  void loadNew(Review review);
  Set<Review> getAll(Book book);
}
