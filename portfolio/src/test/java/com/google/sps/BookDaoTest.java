import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.data.Book;
import java.text.ParseException;
import java.lang.IllegalAccessException;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
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
public final class BookDaoTest {
  private final BookDao BookDao = new BookDaoDatastore();
  private static final Book DEFAULT_BOOK = createDefaultBook();

  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(
          new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
  
  private static Book createDefaultBook(){
    //creates a book from these hard coded values
    String title = "A Court of Wings and Ruin";
    ArrayList<String> authors = new ArrayList<String>();
    authors.add("Sarah J. Maas");
    String isbn = "9781619634497";
    int pageCount = 432;
    ArrayList<String> categories = new ArrayList<String>();
    categories.add("Fiction");
    Set<String> genre = new HashSet<String>();
    genre.add("Fantasy");
    String publisher = "Bloomsbury Publishing USA";
    String language = "en";
    String publishedDate = "2017-05-02";
    String maturityRating = "NOT_MATURE";
    String thumbnail = "http://books.google.com/books/content?id=pLL-DAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";
    String infoLink = "https://play.google.com/store/books/details?id=pLL-DAAAQBAJ&source=gbs_api";
    String description = "Looming war threatens all Feyre holds dear in the third volume of the #1 "
        +"New York Times bestselling A Court of Thorns and Roses series. Feyre has returned to the "
        +"Spring Court, determined to gather information on Tamlin\'s maneuverings and the invading "
        +"king threatening to bring Prythian to its knees. But to do so she must play a deadly game "
        +"of deceit-and one slip may spell doom not only for Feyre, but for her world as well. As war "
        +"bears down upon them all, Feyre must decide who to trust amongst the dazzling and lethal "
        +"High Lords-and hunt for allies in unexpected places. In this thrilling third book in the "
        +"#1 New York Times bestselling series from Sarah J. Maas, the earth will be painted red as "
        +"mighty armies grapple for power over the one thing that could destroy them all.";

    Book.Builder builder = Book.builder().title(title).genre(genre)
        .categories(categories).authors(authors).language(language)
        .description(description).infoLink(infoLink).pageCount(pageCount)
        .publishedDate(publishedDate).publisher(publisher)
        .maturityRating(maturityRating).thumbnail(thumbnail).isbn(isbn);
    Book expected = builder.build();
    return expected;
  }

  private static Book newBook(String title, String category, String author,
    String genre, String language, int pageCount,String isbn){
    ArrayList<String> authors = new ArrayList<String>();
    authors.add(author);
    ArrayList<String> categories = new ArrayList<String>();
    categories.add(category);
    Set<String> genres = new HashSet<String>();
    genres.add(genre);
    Book.Builder builder = Book.builder().title(title).authors(authors)
            .categories(categories).language(language)
            .genre(genres).pageCount(pageCount).isbn(isbn);
    Book expected = builder.build();
    return expected;
  }
  
  @Test(expected = NullPointerException.class)
  public void validateNullIsbn(){
    BookDao.getEntity(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void validateEmptyIsbn(){
    BookDao.getEntity("");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void validateShortIsbn(){
    BookDao.getEntity("1234567");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void validateLongIsbn(){
    BookDao.getEntity("1234567891124151234");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void validateNonNumericIsbn(){
    BookDao.getEntity("puppies");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void validateIllegalCharIsbn(){
    BookDao.getEntity("1+2-344444444");
  }

  @Test(expected = NullPointerException.class)
  public void validateBookNullIsbn(){
    Book nullIsbn = newBook("A Court of Wings and Ruin","Fiction","Sarah Maas","Fantasy","en",34,null);
    BookDao.create(nullIsbn);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateBookEmptyIsbn(){
    Book emptyIsbn = newBook("A Court of Wings and Ruin","Fiction","Sarah Maas","Fantasy","en",34,"");
    BookDao.create(emptyIsbn);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateBookShortIsbn(){
    Book shortIsbn = newBook("A Court of Wings and Ruin","Fiction","Sarah Maas","Fantasy","en",34,"12345");
    BookDao.create(shortIsbn);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateBookLongIsbn(){
    Book longIsbn = newBook("A Court of Wings and Ruin","Fiction","Sarah Maas","Fantasy","en",34,"1234543243792349374074");
    BookDao.create(longIsbn);
  }

  @Test
  public void createAndGetBook() {
    //creates a book and stores in DS, gets the book and compares
    BookDao.create(DEFAULT_BOOK);
    Book actual = BookDao.getEntity(DEFAULT_BOOK.isbn());
    Assert.assertEquals(DEFAULT_BOOK,actual);
  }

  @Test(expected = RuntimeException.class)
  public void createExistingBook() {
    //tries to create a book that is already in datastore
    BookDao.create(DEFAULT_BOOK);
    BookDao.create(DEFAULT_BOOK);
  }

  @Test
  public void updateBook() {
    //updates a book and in DS, gets the book and compares to original
    Book original = newBook("A Court of Wings and Ruin","Fiction","Sarah Maas","Fantasy","en",34,"9781619634497");
    BookDao.create(original);

    Book updatedBook = newBook("A Court of Ruin and Wings","Historical","Sarah Maas","Biography","en",34,"9781619634497");
    BookDao.update(updatedBook);

    Book actual = BookDao.getEntity(DEFAULT_BOOK.isbn());
    Assert.assertEquals(updatedBook,actual);
  }

  @Test
  public void updateNullBook() {
    //creates a book and stores in DS, gets the book and compares
    BookDao.update(DEFAULT_BOOK);
    Assert.assertEquals(null, BookDao.getEntity(DEFAULT_BOOK.isbn()));
  }

  @Test
  public void deleteEntity(){
    //deletes a book that exists in datastore
    BookDao.create(DEFAULT_BOOK);
    BookDao.delete(DEFAULT_BOOK.isbn());
    Assert.assertEquals(null, BookDao.getEntity(DEFAULT_BOOK.isbn()));
  }

  @Test
  public void deleteNullEntity(){
    //deletes a book that does not exist in datastore
    BookDao.delete(DEFAULT_BOOK.isbn());
  }
}

