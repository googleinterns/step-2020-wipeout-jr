package com.google.sps.data;
import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import java.util.Set;

public interface ReviewDao {
  void uploadAll(Book book);
  void uploadNew(Review review);
  ImmutableSet<Review> getAllByISBN(String isbn);
  ImmutableSet<Review> getAllByEmail(String email);
}
