package com.google.sps.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
// import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.ImmutableMultimap;
import java. util. Collection;
import com.google.common.collect.ImmutableSetMultimap.Builder;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multimap;
import com.google.sps.data.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenreRecommender {
  private List<Book> books;
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
    ImmutableSetMultimap.Builder<Book, String> bookToGenresBuilder = new ImmutableSetMultimap.Builder<Book, String>();
    ImmutableSetMultimap.Builder<String, Book> genreToBooksBuilder = new ImmutableSetMultimap.Builder<String, Book>();
    for (Book book : books) {
      for (String genre: book.genre()) {
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
}
