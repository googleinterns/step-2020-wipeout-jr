package com.google.sps.data;

import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.data.BookResponseParser;
import com.google.sps.data.MergeBooks;

// Given a book name, fetches it's info from Google Books Api and stores the response in Datastore.
public class BookUploadUtility {
  // the BookServiceClient and the BookResponseParser are both static
  private final BookDao bookDao = new BookDaoDatastore();
  private final ReviewDao reviewDao = new ReviewDaoDatastore();

  /**
   * This method retrieves a book made from the bookname
   * and uploads the book into datastore.
   * @param bookName: the name of the book you want to query
   * the book API for
   */
  public void uploadBook(String bookName) throws Exception {
    String bookApiResponse = BookServiceClient.getBookInfo(bookName);
    Book book = BookResponseParser.parseBook(bookApiResponse);
    bookDao.create(book);
    reviewDao.uploadAll(book);
  }

  /**
   * This method retrieves a book made from the bookname
   * and combines it with a pre-existing book made from the GoodReads
   * csv and uploads the new merged book into datastore.
   * @param bookName: the name of the book you want to query
   * the book API for
   * @param goodReadsBook: the book constructed from the csv file
   */
  public void mergeUploadBook(String bookName, Book goodReadsBook) throws Exception {
    String bookApiResponse = BookServiceClient.getBookInfo(bookName);
    Book apiBook = BookResponseParser.parseBook(bookApiResponse);
    Book composite = MergeBooks.merge(apiBook, goodReadsBook);
    bookDao.create(composite);
    reviewDao.uploadAll(composite);
  }
}

