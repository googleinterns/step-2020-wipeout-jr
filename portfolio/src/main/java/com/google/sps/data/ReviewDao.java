package com.google.sps.data;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import java.util.Set;

public interface ReviewDao {
  /**
   * Takes in a book and uploads all associated reviews
   * to Datastore with user set to default values
   *
   * @param book: Book object whose reviews are to be uploaded
   */
  void uploadAll(Book book)throws Exception;

  /**
   * Takes in a review and uploads to Datastore.
   * Throws exception if a review with the same book
   * by the same  user already exists.
   *
   * @param review: Review object to be uploaded
   */
  void uploadNew(Review review) throws Exception;

  /**
   * Takes in a review and updates in Datastore.
   * Throws exception if a review with the same book
   * by the same user does not exist.
   *
   * @param review: Review object to be updated
   */
  void updateReview(Review review) throws Exception;

  /**
   * Takes in a book's ISBN value and returns a set of
   * all Reviews associated with that book in Datastore.
   *
   * @param isbn: Book's ISBN whose reviews are to be returned
   * @return ImmutableSet of reviews associated with the book
   */
  ImmutableSet<Review> getAllByISBN(String isbn);

  /**
   * Takes in a user's email and returns a set of
   * all Reviews associated with that user in Datastore.
   *
   * @param isbn: user's email whose reviews are to be returned
   * @return ImmutableSet of reviews associated with the user
   */
  ImmutableSet<Review> getAllByEmail(String email);
}
