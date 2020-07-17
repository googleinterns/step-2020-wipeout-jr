package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
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
import java.lang.Exception;
import java.util.Set;

public class ReviewDaoDatastore implements ReviewDao {
  private DatastoreService datastore;
  private static final String ENTITY_KIND = "Review";
  private static final String BOOK_PROPERTY = "Book";
  private static final String ISBN_PROPERTY = "ISBN";
  private static final String FULLTEXT_PROPERTY = "FullText";
  private static final String USER_PROPERTY = "User";
  private static final String USEREMAIL_PROPERTY = "UserEmail";

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
    for (String reviewStr : book.reviews()) {
      Review review = Review.builder().fullText(reviewStr).book(book).user(defaultUser).build();
      datastore.put(reviewToEntity(review));
    }
  }

  /**
   * Takes in a review and uploads to Datastore
   *
   * @param review: Review object to be uploaded
   */
  @Override
  public void uploadNew(Review review) {
    if (reviewExists(review.book().isbn(), review.user().email())) {
      throw new Exception("Review exists for book:" + review.book().title() + " by user "
          + review.user().nickname());
    }
    datastore.put(reviewToEntity(review));
  }

  @Override
  public void updateReview(Review review) {
    String isbn = review.book().isbn();
    String email = review.user().email();
    String fullText = review.fullText();

    if (!reviewExists(isbn, email)) {
      throw new Exception("Review does not exist for book:" + review.book().title() + " by user "
          + review.user().nickname());
    }

    Entity newReview = Entity.newBuilder(datastore.get(createKey(isbn, email)))
                           .set(FULLTEXT_PROPERTY, fullText)
                           .build();
    datastore.update(newReview);
  }

  /**
   * Takes in a book and returns a set of all Reviews associated
   * with that book in Datastore
   *
   * @param book: Book object whose reviews are to be returned
   * @return ImmutableSet of reviews associated with the book
   */
  @Override
  public ImmutableSet<Review> getAllByISBN(String isbn) {
    return getAllByProperty(ISBN_PROPERTY, isbn);
  }

  @Override
  public ImmutableSet<Review> getAllByEmail(String email) {
    return getAllByProperty(USEREMAIL_PROPERTY, email);
  }

  private static ImmutableSet<Review> getAllByProperty(String property, String value) {
    ImmutableSet.Builder<Review> reviews = new ImmutableSet.Builder<Review>();
    Filter filter = null;
    if (property.equals(ISBN_PROPERTY)) {
      filter = new FilterPredicate(ISBN_PROPERTY, FilterOperator.EQUAL, value);
    } else {
      filter = new FilterPredicate(USEREMAIL_PROPERTY, FilterOperator.EQUAL, value);
    }
    Query query = new Query(ENTITY_KIND).setFilter(filter);
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

  private static Entity reviewToEntity(Review review) {
    String isbn = review.book().isbn();
    String email = review.user().email();
    Entity reviewEntity = new Entity(createKey(isbn, email));

    reviewEntity.setProperty(FULLTEXT_PROPERTY, review.fullText());
    reviewEntity.setProperty(ISBN_PROPERTY, isbn);
    reviewEntity.setProperty(USEREMAIL_PROPERTY, email);
    return userEntity;
  }

  private Key createKey(String isbn, String email) {
    String uniqueID = isbn + "+" + email;
    return KeyFactory.createKey(ENTITY_KIND, uniqueID);
  }

  private boolean reviewExists(String isbn, String email) {
    Entity reviewEntity;
    try {
      reviewEntity = datastore.get(createKey(isbn, email));
    } catch (EntityNotFoundException ex) {
      return false;
    }
    return true;
  }
}
