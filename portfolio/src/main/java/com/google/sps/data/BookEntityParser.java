package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.sps.data.Book;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Converts books to entities, and vice-versa for Datastore interactions.
public class BookEntityParser {
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
  private static final String THUMBNAIL = "thumbnail";
  private static final String IMAGE_LINKS = "imageLinks";
  private static final String ISBN = "ISBN_13";

  /**
   * Parse a entity from a book
   * @param book: the book that you want to create an entity from
   * @param isbnKey: a Key object made from the book's isbn
   */
  public static Entity parseEntity(Book book, Key isbnKey) {
    long timeStamp = System.currentTimeMillis();
    Entity bookEntity = new Entity(isbnKey);

    //try {
      bookEntity.setProperty(TIME_STAMP, timeStamp);
      bookEntity.setProperty(ISBN, book.isbn());

      if (book.title() != "N/A") {
        bookEntity.setProperty(TITLE, book.title());
      }

      if (book.language() != "N/A") {
        bookEntity.setProperty(LANGUAGE, book.language());
      }

      if (book.description() != "N/A") {
        bookEntity.setProperty(DESCRIPTION, new Text(book.description()));
      }

      if (book.infoLink() != "N/A") {
        bookEntity.setProperty(INFO_LINK, book.infoLink());
      }

      if (book.pageCount() != 0) {
        bookEntity.setProperty(PAGE_COUNT, book.pageCount());
      }

      if (book.publishedDate() != "N/A") {
        bookEntity.setProperty(PUBLISHED_DATE, book.publishedDate());
      }

      if (book.publisher() != "N/A") {
        bookEntity.setProperty(PUBLISHER, book.publisher());
      }

      if (book.maturityRating() != "N/A") {
        bookEntity.setProperty(MATURITY_RATING, book.maturityRating());
      }

      if (book.genre() != null) {
        bookEntity.setProperty(GENRE, book.genre());
      }
      
      if (book.categories() != null) {
        bookEntity.setProperty(CATEGORIES, book.categories());
      }

      if (book.authors() != null) {
        bookEntity.setProperty(AUTHORS, book.authors());
      }

      if (book.thumbnail() != "N/A") {
        bookEntity.setProperty(THUMBNAIL, book.thumbnail());
      }
    // } catch (Exception e) {
    //   throw new Exception("There was an error when building the book from the entity", e);
    // }

    return bookEntity;
  }

  /**
  * Parse a book from an entity
  * @param bookEntity: the entity that you want to create a book from
  */
  public static Book parseBook(Entity bookEntity){
    Book.Builder builder = Book.builder();

    //try {
      if (bookEntity.hasProperty(ISBN)) {
        String isbn = (String) bookEntity.getProperty(ISBN);
        builder.isbn(isbn);
      }

      if (bookEntity.hasProperty(TITLE)) {
        String title = (String) bookEntity.getProperty(TITLE);
        builder.title(title);
      }

      if (bookEntity.hasProperty(LANGUAGE)) {
        String lang = (String) bookEntity.getProperty(LANGUAGE);
        builder.language(lang);
      }
      
      if (bookEntity.hasProperty(DESCRIPTION)) {
        //check the type and handle differently for strings and text
        Object bookEntityObj = bookEntity.getProperty(DESCRIPTION);
        if(bookEntityObj instanceof String){
            builder.description((String)bookEntityObj);
        }else{
            Text descriptionAsText = (Text) bookEntity.getProperty(DESCRIPTION);
            String description = descriptionAsText.getValue();
            builder.description(description);
        }
      }
        

      if (bookEntity.hasProperty(INFO_LINK)) {
        String infoLink = (String) bookEntity.getProperty(INFO_LINK);
        builder.infoLink(infoLink);
      }

      if (bookEntity.hasProperty(PAGE_COUNT)) {
        Long tempPageCount = (Long) bookEntity.getProperty(PAGE_COUNT);
        int pageCount = tempPageCount.intValue();
        builder.pageCount(pageCount);
      }

      if (bookEntity.hasProperty(PUBLISHED_DATE)) {
        String pubDate = (String) bookEntity.getProperty(PUBLISHED_DATE);
        builder.publishedDate(pubDate);
      }

      if (bookEntity.hasProperty(PUBLISHER)) {
        String publisher = (String) bookEntity.getProperty(PUBLISHER);
        builder.publisher(publisher);
      }

      if (bookEntity.hasProperty(MATURITY_RATING)) {
        String matRate = (String) bookEntity.getProperty(MATURITY_RATING);
        builder.maturityRating(matRate);
      }

      if (bookEntity.hasProperty(GENRE)) {
        Set<String> genre = new HashSet<String>();
        ArrayList<String> genreList = (ArrayList<String>) bookEntity.getProperty(GENRE);
        genre.addAll(genreList);
        builder.genre(genre);
      }
      
      if (bookEntity.hasProperty(CATEGORIES) && bookEntity.getProperty(CATEGORIES) != null) {
        ArrayList<String> categories = (ArrayList<String>) bookEntity.getProperty(CATEGORIES);
        builder.categories(categories);
      }

      if (bookEntity.hasProperty(AUTHORS)) {
        ArrayList<String> authors = (ArrayList<String>) bookEntity.getProperty(AUTHORS);
        builder.authors(authors);
      }

      // nested in map {}
      if (bookEntity.hasProperty(THUMBNAIL)) {
        String thumbnail = (String) bookEntity.getProperty(THUMBNAIL);
        builder.thumbnail(thumbnail);
      }
    // } catch (Exception e) {
    //   throw new Exception("There was an error when building the book from the entity", e);
    // }

    Book book = builder.build();
    return book;
  }
}

