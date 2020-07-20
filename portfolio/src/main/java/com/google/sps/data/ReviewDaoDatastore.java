package com.google.sps.data;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import java.lang.Exception;
import java.lang.NumberFormatException;
import java.util.Set;

public class ReviewDaoDatastore implements ReviewDao {
  private DatastoreService datastore;
  private static final String ENTITY_KIND = "Review";
  private static final String ISBN_PROPERTY = "ISBN";
  private static final String FULLTEXT_PROPERTY = "FullText";
  private static final String USEREMAIL_PROPERTY = "UserEmail";
  private static final String DEFAULT_EMAIL = "unknown@email.com";

  public ReviewDaoDatastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uploadAll(Book book) throws Exception {
    int userNumber = 0;
    for (String reviewStr : book.reviews()) {
      Review review = Review.builder()
                          .fullText(reviewStr)
                          .isbn(book.isbn())
                          .email(DEFAULT_EMAIL + Integer.toString(userNumber))
                          .build();
      uploadNew(review);
      userNumber++;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uploadNew(Review review) throws Exception {
    if (reviewExists(review.isbn(), review.email())) {
      throw new Exception(
          "Review exists for ISBN:" + review.isbn() + ", by user " + review.email());
    }
    datastore.put(reviewToEntity(review));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateReview(Review review) throws EntityNotFoundException {
    String isbn = review.isbn();
    String email = review.email();
    String fullText = review.fullText();

    if (!reviewExists(isbn, email)) {
      throw new EntityNotFoundException(createKey(isbn, email));
    }
    datastore.put(reviewToEntity(review));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImmutableSet<Review> getAllByISBN(String isbn) {
    validateIsbn(isbn);
    return getAllByProperty(ISBN_PROPERTY, isbn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ImmutableSet<Review> getAllByEmail(String email) {
    validateEmail(email);
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
  private ImmutableSet<Review> getAllByProperty(String property, String value) {
    ImmutableSet.Builder<Review> reviews = new ImmutableSet.Builder<Review>();
    Filter filter = new FilterPredicate(property, FilterOperator.EQUAL, value);
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
  private Review entityToReview(Entity reviewEntity) {
    return Review.builder()
        .fullText((String) reviewEntity.getProperty(FULLTEXT_PROPERTY))
        .isbn((String) reviewEntity.getProperty(ISBN_PROPERTY))
        .email((String) reviewEntity.getProperty(USEREMAIL_PROPERTY))
        .build();
  }

  /**
   * Creates a datastore entity from a Review object
   * @param review: The Review object that the entity will be created of
   * @return Corresponding Entity that is created
   */
  private Entity reviewToEntity(Review review) {
    String isbn = review.isbn();
    String email = review.email();
    Entity reviewEntity = new Entity(createKey(isbn, email));

    reviewEntity.setProperty(FULLTEXT_PROPERTY, review.fullText());
    reviewEntity.setProperty(ISBN_PROPERTY, isbn);
    reviewEntity.setProperty(USEREMAIL_PROPERTY, email);
    return reviewEntity;
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

  /**
   * Checks if input is a valid ISBN value
   * @param isbn: The isbn you want to validate
   */
  private void validateIsbn(String isbn) {
    Preconditions.checkNotNull(isbn, "The ISBN cannot be a null value");
    Preconditions.checkArgument(isbn.matches("[0-9]+"), "The ISBN can only be numeric");
    Preconditions.checkArgument(isbn.length() == 13, "The ISBN must be 13 digits");
  }

  /**
   * Checks if input is a valid email value
   * @param email: The email you want to validate
   */
  private void validateEmail(String email) {
    Preconditions.checkNotNull(email, "The email cannot be a null value");
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    Preconditions.checkArgument(email.matches(regex), "Not a valid email format");
  }
}
