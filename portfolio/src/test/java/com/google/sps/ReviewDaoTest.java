import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Book;
import com.google.common.collect.ImmutableSet;
import com.google.sps.data.Review;
import com.google.sps.data.ReviewDao;
import com.google.sps.data.ReviewDaoDatastore;
import com.google.appengine.api.datastore.EntityNotFoundException;
import java.lang.IllegalAccessException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
   This is a test class for BookDao.java
    Tests the implementations ability to create,
    parse, put, retrieve, update, and delete
    entities in Datastore
*/

@RunWith(JUnit4.class)
public final class ReviewDaoTest {
  private ReviewDao reviewDao = new ReviewDaoDatastore();
  private static final String DEFAULT_ISBN_1 = "1111111111111";
  private static final String DEFAULT_ISBN_2 = "1111111111112";
  private static final String DEFAULT_ISBN_3 = "1111111111113";
  private static final String DEFAULT_EMAIL_1 = "unknown@email.com0";
  private static final String DEFAULT_EMAIL_2 = "unknown@email.com1";
  private static final String DEFAULT_EMAIL_3 = "unknown@email.com2";
  private static final String USER_EMAIL_1 = "user1@br.com";
  private static final String USER_EMAIL_2 = "user2@br.com";

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());

  @Test
  public void uploadAllTest() {
    Book bookWithReviews = createBook();
    reviewDao.uploadAll(bookWithReviews);
    Set<Review> actual = reviewDao.getAllByISBN(DEFAULT_ISBN_1);
    Set<Review> expected =
        new HashSet<>(Arrays.asList(Review.create("Review #1", DEFAULT_ISBN_1, DEFAULT_EMAIL_1),
            Review.create("Review #2", DEFAULT_ISBN_1, DEFAULT_EMAIL_2),
            Review.create("Review #3", DEFAULT_ISBN_1, DEFAULT_EMAIL_3)));

    Assert.assertEquals(expected, actual);
  }

  @Test
  public void uploadNewNormalTest() throws Exception{
      Review review1 = Review.create("New user review #1", DEFAULT_ISBN_1, USER_EMAIL_1);
      Review review2 = Review.create("New user review #2", DEFAULT_ISBN_2, USER_EMAIL_1);
      reviewDao.uploadNew(review1);
      reviewDao.uploadNew(review2);
      Set<Review> actual = reviewDao.getAllByEmail(USER_EMAIL_1);
      Set<Review> expected =
        new HashSet<>(Arrays.asList(review1, review2));

      Assert.assertEquals(expected, actual);
  }

  @Test(expected = Exception.class)
  public void uploadNewEdgeTest() throws Exception{
      Review review1 = Review.create("New user review #1", DEFAULT_ISBN_2, USER_EMAIL_1);
      Review review2 = Review.create("New user review #2", DEFAULT_ISBN_2, USER_EMAIL_1);
      reviewDao.uploadNew(review1);
      reviewDao.uploadNew(review2); // Error: Same user leaving new comment for same book
  }

  @Test
  public void updateReviewNormalTest() throws Exception{
      // Add reviews:
      Review review1 = Review.create("New user review #1", DEFAULT_ISBN_2, USER_EMAIL_1);
      Review review2 = Review.create("New user review #2", DEFAULT_ISBN_2, USER_EMAIL_2);
      reviewDao.uploadNew(review1);
      reviewDao.uploadNew(review2);
      
      // Update reviews:
      Review updatedReview1 = Review.create("Updating Review 1!", DEFAULT_ISBN_2, USER_EMAIL_1);
      Review updatedReview2 = Review.create("Updating Review 2!", DEFAULT_ISBN_2, USER_EMAIL_2);
      reviewDao.updateReview(updatedReview1);
      reviewDao.updateReview(updatedReview2);

      Set<Review> actual = reviewDao.getAllByISBN(DEFAULT_ISBN_2);
      Set<Review> expected =
        new HashSet<>(Arrays.asList(updatedReview1, updatedReview2));

      Assert.assertEquals(expected, actual);
  }

  @Test(expected = EntityNotFoundException.class)
  public void updateReviewEdgeTest() throws Exception{
      Review review1 = Review.create("New user review #1", DEFAULT_ISBN_1, USER_EMAIL_1);
      Review review2 = Review.create("New user review #2", DEFAULT_ISBN_2, USER_EMAIL_1);
      reviewDao.uploadNew(review1);
      reviewDao.updateReview(review2); // Error: User has not left a review for ISBN_2 before
  }

  @Test
  public void getAllByISBNEdgeTest(){
      Set<Review> actual = reviewDao.getAllByISBN(DEFAULT_ISBN_1);
      Set<Review> expected = ImmutableSet.of();
      Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAllByISBNNonNumericTest(){
      Set<Review> actual = reviewDao.getAllByISBN("Not a valid ISBN");
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAllByISBNWrongNumOfDigitsTest(){
      Set<Review> actual = reviewDao.getAllByISBN("2341234");
  }

  @Test(expected = NullPointerException.class)
  public void getAllByISBNNullTest(){
      Set<Review> actual = reviewDao.getAllByISBN(null);
  }

  @Test
  public void getAllByEmailEdgeTest() throws Exception{
      Set<Review> actual = reviewDao.getAllByEmail("notreal@email.com");
      Set<Review> expected = ImmutableSet.of();
      Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getAllByEmailWrongFormatTest(){
      Set<Review> actual = reviewDao.getAllByEmail("notRight@emailFormat");
  }

  @Test(expected = NullPointerException.class)
  public void getAllByEmailNullTest(){
      Set<Review> actual = reviewDao.getAllByEmail(null);
  }

  

  private static Book createBook() {
    return Book.builder()
        .title("Book with Reviews")
        .genre(new HashSet<>(Arrays.asList("Default Genres")))
        .addReview("Review #1")
        .addReview("Review #2")
        .addReview("Review #3")
        .isbn(DEFAULT_ISBN_1)
        .build();
  }

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
}