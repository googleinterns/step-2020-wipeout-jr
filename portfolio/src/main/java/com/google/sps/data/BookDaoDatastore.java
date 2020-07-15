package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
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
    private static final String PAGE_COUNT = "pageCount";
    private static final String PUBLISHED_DATE = "publishedDate";
    private static final String PUBLISHER = "publisher";
    private static final String MATURITY_RATING = "maturityRating";
    private static final String ISBN = "isbn";

    private DatastoreService datastore;

    public BookDaoDatastore() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void create(Book book) {
        long timeStamp = System.currentTimeMillis();

        Entity bookEntity = new Entity(ENTITY_KIND,book.isbn());

        bookEntity.setProperty(TIME_STAMP,timeStamp);
        bookEntity.setProperty(TITLE,book.title());
        bookEntity.setProperty(GENRE,book.genre());
        bookEntity.setProperty(CATEGORIES,book.categories());
        bookEntity.setProperty(AUTHORS,book.authors());
        bookEntity.setProperty(LANGUAGE,book.language());
        bookEntity.setProperty(DESCRIPTION,book.description());
        bookEntity.setProperty(INFO_LINK,book.infoLink());
        bookEntity.setProperty(PAGE_COUNT,book.pageCount());
        bookEntity.setProperty(PUBLISHED_DATE,book.publishedDate());
        bookEntity.setProperty(PUBLISHER,book.publisher());
        bookEntity.setProperty(MATURITY_RATING,book.maturityRating());

        datastore.put(bookEntity);
    }

    @Override
    public Book get(String isbn) {
        Entity bookEntity = new Entity("Book");
        try{
            Key bookKey = KeyFactory.stringToKey(isbn);
            bookEntity = datastore.get(bookKey);
        } catch(EntityNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            return parseBook(bookEntity);
        }
    }

    private Book parseBook(Entity bookEntity){
        String title = (String) bookEntity.getProperty(TITLE);
        Set<String> genre = (Set<String>) bookEntity.getProperty(GENRE);
        ArrayList<String> categories = (ArrayList<String>) bookEntity.getProperty(CATEGORIES);
        ArrayList<String> authors = (ArrayList<String>) bookEntity.getProperty(AUTHORS);
        String language = (String) bookEntity.getProperty(LANGUAGE);
        String description = (String) bookEntity.getProperty(DESCRIPTION);
        String infoLink = (String) bookEntity.getProperty(INFO_LINK);
        int pageCount = (int) bookEntity.getProperty(PAGE_COUNT);
        String publishedDate = (String) bookEntity.getProperty(PUBLISHED_DATE);
        String publisher = (String) bookEntity.getProperty(PUBLISHER);
        String maturityRating = (String) bookEntity.getProperty(MATURITY_RATING);
        String isbn = (String) bookEntity.getProperty(ISBN);
 
        Book.Builder builder = Book.builder().title(title).genre(genre)
            .categories(categories).authors(authors).language(language)
            .description(description).infoLink(infoLink).pageCount(pageCount)
            .publishedDate(publishedDate).publisher(publisher)
            .maturityRating(maturityRating).isbn(isbn);
 
        Book book = builder.build();
        return book;

    }
}
