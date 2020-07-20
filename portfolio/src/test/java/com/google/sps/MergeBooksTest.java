import com.google.sps.data.Book;
import com.google.sps.data.MergeBooks;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
 
/**
   This is a test class for MergeBooks.java
    Tests the implementations ability to combine books
*/
 
@RunWith(JUnit4.class)
public final class MergeBooksTest {
  private static final Book DEFAULT_BOOK = createBook();
  
  private static Book createBook(){
    //creates a book from these hard coded values
    //no genre or reviews
    String title = "A Court of Wings and Ruin";
    ArrayList<String> authors = new ArrayList<String>();
    authors.add("Sarah J. Maas");
    String isbn = "9781619634497";
    int pageCount = 432;
    ArrayList<String> categories = new ArrayList<String>();
    categories.add("Fiction");
    String language = "en";

    Book.Builder builder = Book.builder().title(title).categories(categories)
        .authors(authors).language(language).pageCount(pageCount).isbn(isbn);
    Book defaultBook = builder.build();
    return defaultBook;
  }

  private static Book newBook(String title, String category, String author,
    String genre, String language, int pageCount,String isbn,ArrayList<String> reviews){
    ArrayList<String> authors = new ArrayList<String>();
    authors.add(author);
    ArrayList<String> categories = new ArrayList<String>();
    categories.add(category);
    Set<String> genres = new HashSet<String>();
    genres.add(genre);
    Book.Builder builder = Book.builder().title(title).authors(authors)
            .categories(categories).language(language)
            .genre(genres).pageCount(pageCount).isbn(isbn);
    for(String each: reviews){
        builder.addReview(each);
    }
    Book expected = builder.build();
    return expected;
  }

  private static Book newReviewsBook(String title, String genre,ArrayList<String> reviews){
    Set<String> genres = new HashSet<String>();
    genres.add(genre);
    Book.Builder builder = Book.builder().title(title).genre(genres);
    for(String each: reviews){
        builder.addReview(each);
    }
    Book expected = builder.build();
    return expected;
  }

  private static ArrayList<String> reviewsMaker(String review1, String review2, String review3){
    ArrayList<String> reviews = new ArrayList<String>();
    reviews.add(review1);
    reviews.add(review2);
    reviews.add(review3);
    return reviews;
  }

  @Test
  public void simpleMerge(){
    //the default book would represent the book created by the book parser and the API
    Book mockCSVBook = newReviewsBook("A Court of Wings and Ruin","Fantasy",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
    Book mergedBook = MergeBooks.merge(DEFAULT_BOOK,mockCSVBook);
    Book expected = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"9781619634497",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
    Assert.assertEquals(expected,mergedBook);
  }

  @Test(expected = NullPointerException.class)
  public void parseNull() throws Exception{
      //attempts to parse a null value
      Book mergedBook = MergeBooks.merge(DEFAULT_BOOK,null);

  }

   @Test(expected = IllegalArgumentException.class)
  public void parseEmptyIsbn() throws Exception{
      //attempts to parse a empty value as an ISBN
      Book emptyIsbn = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
      Book mergedBook = MergeBooks.merge(emptyIsbn,emptyIsbn);

  }

  @Test(expected = IllegalArgumentException.class)
  public void parseTwoDifferentTitles() throws Exception{
    Book originalBook = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
    Book diffTitleBook = newBook("Monkey Goes to Big City","Fiction","Sarah J. Maas","Fantasy","en",432,"",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
    Book mergedBook = MergeBooks.merge(originalBook,diffTitleBook);
  }

  @Test
  public void combineReviews() throws Exception{
    //attempts to parse the full response from the book api
    Book goodReviews = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"9781619634497",reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching."));
    Book badReviews = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"9781619634497",reviewsMaker("It was bad.","It was pretty bad.","Could only get half-way through."));
    Book mergedBook = MergeBooks.merge(goodReviews,badReviews);
    ArrayList<String> expectedReviews = reviewsMaker("It was good.","It was pretty good.","It was heart-wrenching.");
    expectedReviews.add("It was bad.");
    expectedReviews.add("It was pretty bad.");
    expectedReviews.add("Could only get half-way through.");

    Book expected = newBook("A Court of Wings and Ruin","Fiction","Sarah J. Maas","Fantasy","en",432,"9781619634497",expectedReviews);
    Assert.assertEquals(expected,mergedBook);
  }
}

