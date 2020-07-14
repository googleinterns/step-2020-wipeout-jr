package com.google.sps.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSetMultimap.Builder;
import com.google.sps.data.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenreRecommender {
  private List<Book> books;
  private Map<Book, Integer> relevanceScoreMap;
  private static final int MATCH_SCORE = 5;
  private ImmutableSetMultimap<Book, String> bookToGenres;
  private ImmutableSetMultimap<String, Book> genreToBooks;

  /**
   * Constructor that takes in a list of books
   * and populates class variables appropriately.
   *
   * @param books: List of Book objects for the recommendation algorithm
   */
  public GenreRecommender(List<Book> books) {
    this.books = books;
    ImmutableSetMultimap.Builder<Book, String> bookToGenresBuilder =
        new ImmutableSetMultimap.Builder<Book, String>();
    ImmutableSetMultimap.Builder<String, Book> genreToBooksBuilder =
        new ImmutableSetMultimap.Builder<String, Book>();
    for (Book book : books) {
      for (String genre : book.genre()) {
        bookToGenresBuilder.put(book, genre);
        genreToBooksBuilder.put(genre, book);
      }
    }
    bookToGenres = bookToGenresBuilder.build();
    genreToBooks = genreToBooksBuilder.build();
  }

  /**
   * Takes in a Book object and returns a set of genres
   * of that book
   *
   * @param book: Book object
   * @return ImmutableSet of genres of that book
   */
  public ImmutableSet<String> getGenres(Book book) {
    ImmutableSet<String> genres = bookToGenres.get(book);
    if (genres == null) {
      return ImmutableSet.of();
    }
    return genres;
  }

  /**
   * Takes in a set of genres and returns a set of Book objects
   * that have exactly the genres from the set.
   *
   * @param genres: Set of genres (as Strings)
   * @return ImmutableSet of books that have exactly these genres
   */
  public ImmutableSet<Book> getBooksWithExactGenres(Set<String> genres) {
    ImmutableSet.Builder<Book> matchingBooks = new ImmutableSet.Builder<Book>();
    for (Book book : bookToGenres.keySet()) {
      if (bookToGenres.get(book).equals(genres)) {
        matchingBooks.add(book);
      }
    }
    ImmutableSet<Book> matches = matchingBooks.build();
    if (matches.isEmpty()) {
      return ImmutableSet.of();
    }
    return matches;
  }

  /**
   * Takes in a set of genres and returns an ImmutableMap
   * with books and their relevance score
   *
   * @param genres: Set of genres (as Strings)
   * @return ImmutableMap with {Book:relevanceScore}
   */
  private ImmutableMap<Book, Integer> getBooksWithScores(Set<String> genres) {
      ImmutableMap.Builder<Book, Integer> bookToScore = new ImmutableMap.Builder<Book, Integer>();
      Set<Book> booksToConsider = new HashSet<Book>();
      for (String genre: genres) {
          booksToConsider.addAll(genreToBooks.get(genre));
      }

      for (Book book: booksToConsider) {
          // assign score based on relevance:
          int score = 0;
          for (String genre: getGenres(book)) {
              if (genres.contains(genre)) {
                  score += MATCH_SCORE;
              }
          }
          bookToScore.put(book, score);
          System.out.println(book.genre() + " has score: " + score);

      }
      return bookToScore.build();
  }

  /**
    * Returns the top N matches of a book by Genres
    * 
    * @param book: Book object for recommendations
    * @param n: Number of books to be returned
    * @return List of n books sorted most-> least recommended
    */
  public List<Book> getTopNMatches(Book originalBook, int n) {
      relevanceScoreMap = getBooksWithScores(originalBook.genre());
      List<Book> topNBooks = new ArrayList<Book>(); // sorted list of top n books in descending order

      if (n < 1){
          return topNBooks;
      }
      
      for (Book book: relevanceScoreMap.keySet()) {
          if (book.equals(originalBook)) {
              continue;
          }
          if (topNBooks.size() < n) {
              addInPosition(topNBooks, book); // add it in the right place
          } else if (relevanceScoreMap.get(book) > relevanceScoreMap.get(topNBooks.get(n-1))){
              addInPosition(topNBooks.subList(0, n-1), book);
          }
      }
      return topNBooks;
  }

  private void addInPosition(List<Book> list, Book book) {
      if (list.size() == 0) {
          list.add(book);
          return;
      } else {
          for (int i = 0; i < list.size(); i++) {
              if (relevanceScoreMap.get(book) < relevanceScoreMap.get(list.get(i))) {
                  list.add(i, book);
                  return;
              }
          }
      }
      list.add(book);
  }

}
