package com.google.sps.data;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.HashSet;
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
    public void create(Book book){
      validateBook(book);
      if(!entityExists(book.isbn())){
        datastore.put(parseEntity(book));
      }else{
        throw new RuntimeException(String.format("Book already exists with ISBN-13=%s", book.isbn()));
      }
    }

    /**
    * Delete a Book entity in Datastore
    * @param isbn: the isbn of the book that you want to delete
    */
    @Override
    public void delete(String isbn){
        validateIsbn(isbn);
        if(entityExists(isbn)){
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
        try{
            bookEntity = datastore.get(createKey(isbn));
        } catch(EntityNotFoundException ex) {
            return null;
        }
        return parseBook(bookEntity);
    }

    /**
    * Update a user entity in Datastore
    * @param book: The book to be updated
    */
    @Override
    public void update(Book book) {
        validateBook(book);
        if(entityExists(book.isbn())){
            datastore.put(parseEntity(book));
        }
    }

    /**
    * Parse a book from an entity
    * @param bookEntity: the entity that you want to create a book from
    */
    private Book parseBook(Entity bookEntity){
        String title = (String) bookEntity.getProperty(TITLE);
        String language = (String) bookEntity.getProperty(LANGUAGE);
        Text descriptionAsText = (Text) bookEntity.getProperty(DESCRIPTION);
        String description = descriptionAsText.getValue();
        String infoLink = (String) bookEntity.getProperty(INFO_LINK);
        String thumbnail = (String) bookEntity.getProperty(THUMBNAIL);
        String publishedDate = (String) bookEntity.getProperty(PUBLISHED_DATE);
        String publisher = (String) bookEntity.getProperty(PUBLISHER);
        String maturityRating = (String) bookEntity.getProperty(MATURITY_RATING);
        String isbn = (String) bookEntity.getProperty(ISBN);
        ArrayList<String> categories = (ArrayList<String>) bookEntity.getProperty(CATEGORIES);
        ArrayList<String> authors = (ArrayList<String>) bookEntity.getProperty(AUTHORS);

        ArrayList<String> genreList = (ArrayList<String>) bookEntity.getProperty(GENRE);
        Set<String> genre = new HashSet<String>();
        genre.addAll(genreList);
        
        Long tempPageCount = (Long) bookEntity.getProperty(PAGE_COUNT);
        int pageCount = tempPageCount.intValue();
 
        Book.Builder builder = Book.builder().title(title).genre(genre)
            .categories(categories).authors(authors).language(language)
            .description(description).infoLink(infoLink).pageCount(pageCount)
            .publishedDate(publishedDate).publisher(publisher).thumbnail(thumbnail)
            .maturityRating(maturityRating).isbn(isbn);
 
        Book book = builder.build();
        return book;

    }

    /**
    * Parse an entity from a book
    * @param bookEntity: the entity that you want to create a book from
    */
    private Entity parseEntity(Book book) {
        //parse entity from book
        long timeStamp = System.currentTimeMillis();

        Entity bookEntity = new Entity(createKey(book.isbn()));

        bookEntity.setProperty(TIME_STAMP, timeStamp);
        bookEntity.setProperty(TITLE, book.title());
        bookEntity.setProperty(GENRE, book.genre());
        bookEntity.setProperty(CATEGORIES, book.categories());
        bookEntity.setProperty(AUTHORS, book.authors());
        bookEntity.setProperty(LANGUAGE, book.language());
        bookEntity.setProperty(DESCRIPTION, new Text(book.description()));
        bookEntity.setProperty(INFO_LINK, book.infoLink());
        bookEntity.setProperty(THUMBNAIL, book.thumbnail());
        bookEntity.setProperty(PAGE_COUNT, book.pageCount());
        bookEntity.setProperty(PUBLISHED_DATE, book.publishedDate());
        bookEntity.setProperty(PUBLISHER, book.publisher());
        bookEntity.setProperty(MATURITY_RATING, book.maturityRating());
        bookEntity.setProperty(ISBN, book.isbn());

        return bookEntity;
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
    private void validateBook(Book book){
        Preconditions.checkNotNull(book, "The Book cannot be a null value");
        validateIsbn(book.isbn());
    }

    /**
    * Checks to see if the input is valid
    * @param isbn: The isbn you want to validate
    */
    private void validateIsbn(String isbn){
        Preconditions.checkNotNull(isbn, "The ISBN cannot be a null value");
        Preconditions.checkArgument(isbn.matches("[0-9]+"), "The ISBN can only be numeric");
        Preconditions.checkArgument(isbn.length() == 13, "The ISBN must be 13 digit's");
    }

    /**
    * Checks to see if book is already in datastore
    * @param isbn: The isbn of the book you want to check for
    */
    private boolean entityExists(String isbn){
        return getEntity(isbn) != null;
    }
}

