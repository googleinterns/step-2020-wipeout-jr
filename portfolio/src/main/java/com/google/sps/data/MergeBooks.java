package com.google.sps.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.sps.data.Book;
import java.util.ArrayList;

public class MergeBooks {
  /**
  * This method merges two books that have the same title
  * and ISBN and returns a new book combined from both of them
  * @param googleApiBook: a book created by the bookResponse parser from the Books API
  * Contains: title, categories, author, language, description, infoLink, pageCount, publishedDate,
  * publisher, maturityRating, thumbnail, ISBN
  * @param goodReadsBook: the book constructed from the csv file
  * Contains: title, genre, reviews
  */
  public static Book merge(Book googleApiBook, Book goodReadsBook) {
    validate(googleApiBook);
    validate(goodReadsBook);
    validateISBN(googleApiBook.isbn());
    
    checkTitle(googleApiBook.title(),goodReadsBook.title());

    // Get API data
    Book.Builder combinedBuilder = googleApiBook.toBuilder();
    // Get GoodReads data
    combinedBuilder.genre(goodReadsBook.genre());
    ImmutableList<String> reviews = goodReadsBook.reviews();
    for (String each : reviews) {
      combinedBuilder.addReview(each);
    }

    return combinedBuilder.build();
  }

  /**
  * Checks the book titles, by comparing the shorter title with the first
  * n characters of the longer title, so that if the series is included the
  * books will still be able to be merged
  * @param apiTitle: the title of the book parsed from the Google Books API
  * @param goodReadsTitle: the title of the book parsed from the goodReads csv
  */
  private static void checkTitle(String apiTitle, String goodReadsTitle){
    String longerString;
    String shorterString;
    if(apiTitle.length() > goodReadsTitle.length()){
        longerString = apiTitle.toLowerCase();
        shorterString = goodReadsTitle.toLowerCase();
    }else{
        longerString = goodReadsTitle.toLowerCase();
        shorterString = apiTitle.toLowerCase();
    }
    String titleMismatchErrorMessage = "The books must be of the same title. Expected [%s] but got [%s]";
    String substringOfLongTitle = longerString.substring(0,shorterString.length());
    titleMismatchErrorMessage = String.format(titleMismatchErrorMessage, goodReadsTitle, apiTitle);
    Preconditions.checkArgument(
    shorterString.equals(substringOfLongTitle), titleMismatchErrorMessage);

  }

  /**
  * Validates the book by checking that it has a title and the
  * ISBN is in the correct format
  * @param book: the book you want to validate
  */
  private static void validate(Book book) {
    Preconditions.checkArgument(!book.title().equals(""), "The book's title was empty");
    Preconditions.checkArgument(!book.title().equals("N/A"), "The book's title was empty");
  }

  /**
  * Validates the ISBN by checking that it is in the right format
  * @param isbn: the ISBN you want to validate
  */
  private static void validateISBN(String isbn) {
    String errorMessage = "Expected Google book-api based Book to contain the ISBN-13 populated.";
    Preconditions.checkArgument(!isbn.equals(""), errorMessage);
    Preconditions.checkArgument(!isbn.equals("N/A"), errorMessage);
    Preconditions.checkArgument(isbn.matches("[0-9]+"), errorMessage);
    Preconditions.checkArgument(isbn.length() == 13, errorMessage);
  }
}

