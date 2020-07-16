package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import com.google.sps.data.User;
import java.util.Set;

public class ReviewDaoDatastore implements ReviewDao {
  private DatastoreService datastore;
  private static final String ENTITY_KIND = "Review";
  private static final String BOOK_PROPERTY = "Book";
  private static final String FULLTEXT_PROPERTY = "FullText";
  private static final String USER_PROPERTY = "User";

  public ReviewDaoDatastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Takes in a book and uploads all associated reviews
   * to Datastore with user set to default values
   *
   * @param book: Book object whose reviews are to be uploaded
   */
  @Override
  public void uploadAll(Book book) {
    User defaultUser = User.create("unknown email", "GoodReads User");
    // loop through all the reviews of a book
    for (String review : book.reviews()) {
      Entity reviewEntity = new Entity(ENTITY_KIND);
      reviewEntity.setProperty(BOOK_PROPERTY, book);
      reviewEntity.setProperty(FULLTEXT_PROPERTY, review);
      reviewEntity.setProperty(USER_PROPERTY, defaultUser);
      datastore.put(reviewEntity);
    }
  }

  /**
   * Takes in a review and uploads to Datastore
   *
   * @param review: Review object to be uploaded
   */
  @Override
  public void uploadNew(Review review) {
    Entity reviewEntity = new Entity(ENTITY_KIND);
    reviewEntity.setProperty(BOOK_PROPERTY, review.book());
    reviewEntity.setProperty(FULLTEXT_PROPERTY, review.fullText());
    reviewEntity.setProperty(USER_PROPERTY, review.user());
    datastore.put(reviewEntity);
  }

  /**
   * Takes in a book and returns a set of all Reviews associated
   * with that book in Datastore
   *
   * @param book: Book object whose reviews are to be returned
   * @return ImmutableSet of reviews associated with the book
   */
  @Override
  public ImmutableSet<Review> getAll(Book book) {
    ImmutableSet.Builder<Review> reviews = new ImmutableSet.Builder<Review>();

    Filter bookFilter = new FilterPredicate(BOOK_PROPERTY, FilterOperator.EQUAL, book);
    Query query = new Query(ENTITY_KIND).setFilter(bookFilter);
    PreparedQuery results = datastore.prepare(query);
    if (results == null) {
      return reviews.build();
    }
    for (Entity entity : results.asIterable()) {
      Review review = entityToReview(entity);
      reviews.add(review);
    }
    return reviews.build();
  }

  private static Review entityToReview(Entity reviewEntity) {
    return Review.builder()
        .fullText((String) reviewEntity.getProperty(FULLTEXT_PROPERTY))
        .book((Book) reviewEntity.getProperty(BOOK_PROPERTY))
        .user((User) reviewEntity.getProperty(USER_PROPERTY))
        .build();
  }
}
