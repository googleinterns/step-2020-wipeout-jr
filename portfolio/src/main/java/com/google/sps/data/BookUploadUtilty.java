package com.google.sps.data;

import com.google.sps.data.Book;
import com.google.sps.data.BookDao;
import com.google.sps.data.BookDaoDatastore;
import com.google.sps.data.BookResponseParser;
import com.google.sps.data.MergeBooks;

// Given a book name, fetches it's info from Google Books Api and stores the response in Datastore.
class BookUploadUtility {
  //the BookServiceClient and the BookResponseParser are both static
  private final BookDao bookDao = new BookDaoDatastore();

  /**
  * This method retrieves a book made from the bookname
  * and uploads the book into datastore.
  * @param bookName: the name of the book you want to query
  * the book API for
  */
  void uploadBook(String bookName) {
    try{
        String bookApiResponse = BookServiceClient.getBookInfo(bookName);
        Book book = BookResponseParser.parseBook(bookApiResponse);
        bookDao.create(book);
    }
    catch(Exception e){
        //
    }
    
  }

  /**
  * This method retrieves a book made from the bookname
  * and combines it with a pre-existing book and uploads the
  * new merged book into datastore.
  * @param bookName: the name of the book you want to query
  * the book API for
  * @param book: the pre-existing book you want to merge with,
  * most likely a book constructed from the CSV file
  */
  void mergeUploadBook(String bookName, Book preExisting) {
    try{
        String bookApiResponse = BookServiceClient.getBookInfo(bookName);
        Book apiBook = BookResponseParser.parseBook(bookApiResponse);
        Book composite = MergeBooks.merge(apiBook,preExisting);
        bookDao.create(composite);  
    }catch(Exception e){
        //
    }
    
  }
}