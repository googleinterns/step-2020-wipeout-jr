package com.google.sps.data;
import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import java.util.Set;

public interface ReviewDao {
  void loadAll(Book book);
  void loadNew(Review review);
  ImmutableSet<Review> getAll(Book book);
}
