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
  private static final String ISBN_PROPERTY = "ISBN";
  private static final String FULLTEXT_PROPERTY = "FullText";
  private static final String USEREMAIL_PROPERTY = "UserEmail";

  public ReviewDaoDatastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * {@inheritDoc}
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
   * {@inheritDoc}
   */
  @Override
  public void uploadNew(Review review) {
    if (reviewExists(review.book().isbn(), review.user().email())) {
      throw new Exception("Review exists for book:" + review.book().title() + ", by user "
          + review.user().nickname());
    }
    datastore.put(reviewToEntity(review));
  }

  /**
   * {@inheritDoc}
   */
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
   * {@inheritDoc}
   */
  @Override
  public ImmutableSet<Review> getAllByISBN(String isbn) {
    return getAllByProperty(ISBN_PROPERTY, isbn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImmutableSet<Review> getAllByEmail(String email) {
    return getAllByProperty(USEREMAIL_PROPERTY, email);
  }

  /**
   * Takes in a property and the associated value to create a 
   * filter and returns all Entities with this value
   * 
   * @param property: property to be filtered by
   * @param value: value of this property that is wanted
   * @return ImmutableSet of Reviews that pass this filter
   */
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

  /**
   * Creates a Review object from entity
   * @param reviewEntity: the entity representing the review to be created
   * @return Corresponding Review object that is created
   */
  private static Review entityToReview(Entity reviewEntity) {
    return Review.builder()
        .fullText((String) reviewEntity.getProperty(FULLTEXT_PROPERTY))
        .book((Book) reviewEntity.getProperty(BOOK_PROPERTY))
        .user((User) reviewEntity.getProperty(USER_PROPERTY))
        .build();
  }

  /**
   * Creates a datastore entity from a Review object
   * @param review: The Review object that the entity will be created of
   * @return Corresponding Entity that is created
   */
  private static Entity reviewToEntity(Review review) {
    String isbn = review.book().isbn();
    String email = review.user().email();
    Entity reviewEntity = new Entity(createKey(isbn, email));

    reviewEntity.setProperty(FULLTEXT_PROPERTY, review.fullText());
    reviewEntity.setProperty(ISBN_PROPERTY, isbn);
    reviewEntity.setProperty(USEREMAIL_PROPERTY, email);
    return userEntity;
  }

  /**
   * Creates a key for Reviews containing Book and User information
   *
   * @param isbn: isbn value of the Book for which the Review is written
   * @param email: email of the User who writes the Review
   * @return Key object for a Review 
   */
  private Key createKey(String isbn, String email) {
    String uniqueID = isbn + "+" + email;
    return KeyFactory.createKey(ENTITY_KIND, uniqueID);
  }

  /**
   * Checks if a review exists for a given book by given user
   *
   * @param isbn: ISBN of book to be checked
   * @param email: email of user to be checked
   * @return whether a review exists for this book/user pair
   */
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
