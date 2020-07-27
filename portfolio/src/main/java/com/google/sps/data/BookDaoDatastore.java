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
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.common.base.Preconditions;
import com.google.sps.data.BookEntityParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
/**
 * Access Datastore to manage Books
 */
public class BookDaoDatastore implements BookDao {
  private static final String ENTITY_KIND = "Book";
  private static final String TIME_STAMP = "timeStamp";
  private static final String TITLE = "title";
  private static final String GENRE = "genre";
  private static final String CATEGORIES = "categories";
  private static final String AUTHORS = "authors";
  private static final String LANGUAGE = "language";
  private static final String DESCRIPTION = "description";
  private static final String INFO_LINK = "infoLink";
  private static final String THUMBNAIL = "thumbnail";
  private static final String PAGE_COUNT = "pageCount";
  private static final String PUBLISHED_DATE = "publishedDate";
  private static final String PUBLISHER = "publisher";
  private static final String MATURITY_RATING = "maturityRating";
  private static final String ISBN = "isbn";
 
  private DatastoreService datastore;
 
  public BookDaoDatastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }
 
  /**
   * Create a Book entity in Datastore
   * @param book: the book that you want to create an entity from
   */
  @Override
  public void create(Book book) {
    validateBook(book);
    if (!entityExists(book.isbn())) {
      Entity bookEntity = BookEntityParser.parseEntity(book,createKey(book.isbn()));
      datastore.put(bookEntity);
    } else {
      throw new RuntimeException(String.format("Book already exists with ISBN-13=%s", book.isbn()));
    }
  }
 
  /**
   * Delete a Book entity in Datastore
   * @param isbn: the isbn of the book that you want to delete
   */
  @Override
  public void delete(String isbn) {
    validateIsbn(isbn);
    if (entityExists(isbn)) {
      datastore.delete(createKey(isbn));
    }
  }
 
  /**
   * Gets the entity from datastore and parses as Book
   * @param isbn: The isbn of the book you want
   */
  @Override
  public Book getEntity(String isbn) {
    validateIsbn(isbn);
    Entity bookEntity;
    try {
      bookEntity = datastore.get(createKey(isbn));
    } catch (EntityNotFoundException ex) {
      return null;
    }
    return BookEntityParser.parseBook(bookEntity);
  }
 
  /**
  * Gets the last 20 books uploaded in Datastore
  * @return bookList: A list of 20 books
  */
  @Override
  public List<Book> getBookList() {
    Query query = new Query(ENTITY_KIND).addSort(TIME_STAMP, SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    int numberLoaded = 0;
 
    ArrayList<Book> bookList = new ArrayList<Book>();
    for (Entity entity : results.asIterable()) {
      if (numberLoaded < 20) {
        bookList.add(BookEntityParser.parseBook(entity));
        numberLoaded++;
      } else {
        break;
      }
    }
    return bookList;
  }
 
  /**
   * Update a user entity in Datastore
   * @param book: The book to be updated
   */
  @Override
  public void update(Book book) {
    validateBook(book);
    if (entityExists(book.isbn())) {
      Entity bookEntity = BookEntityParser.parseEntity(book,createKey(book.isbn()));
      datastore.put(bookEntity);
    }
  }

  /**
   * Creates a custom Key object of kind ENTITY_KIND ("Book")
   * @param isbn: The isbn of the book you want to create the key for
   * the same isbn will create the same key so it will work when
   * passing isbn to get book
   */
  private Key createKey(String isbn) {
    return KeyFactory.createKey(ENTITY_KIND, isbn);
  }
 
  /**
   * Checks to see if the input is valid
   * @param book: The input you want to validate
   */
  private void validateBook(Book book) {
    Preconditions.checkNotNull(book, "The Book cannot be a null value");
    validateIsbn(book.isbn());
  }
 
  /**
   * Checks to see if the input is valid
   * @param isbn: The isbn you want to validate
   */
  private void validateIsbn(String isbn) {
    Preconditions.checkNotNull(isbn, "The ISBN cannot be a null value");
    Preconditions.checkArgument(isbn.matches("[0-9]+"), "The ISBN can only be numeric");
    Preconditions.checkArgument(isbn.length() == 13, "The ISBN must be 13 digit's");
  }
 
  /**
   * Checks to see if book is already in datastore
   * @param isbn: The isbn of the book you want to check for
   */
  private boolean entityExists(String isbn) {
    return getEntity(isbn) != null;
  }
}

