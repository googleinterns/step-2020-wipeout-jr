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
    Preconditions.checkArgument(
        googleApiBook.title().toLowerCase().equals(goodReadsBook.title().toLowerCase()), "The books must be of the same title. Expected [" + goodReadsBook.title() + "] but got ["+googleApiBook.title()+"]");

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
   * Validates the book by checking that it has a title and the
   * ISBN is in the correct format
   * @param book: the book you want to validate
   */
  private static void validate(Book book) {
    Preconditions.checkArgument(!book.title().equals(""), "The book's title was empty");
    Preconditions.checkArgument(!book.title().equals("N/A"), "The book's title was empty");
  }

  private static void validateISBN(String isbn) {
    String errorMessage = "Expected Google book-api based Book to contain the ISBN-13 populated.";
    Preconditions.checkArgument(!isbn.equals(""), errorMessage);
    Preconditions.checkArgument(!isbn.equals("N/A"), errorMessage);
    Preconditions.checkArgument(isbn.matches("[0-9]+"), errorMessage);
    Preconditions.checkArgument(isbn.length() == 13, errorMessage);
  }
}

