package com.google.sps.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.sps.data.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: MAKE LISTS IMMUTABLE

public class GenreRecommender {
  private List<Book> books;
  private Map<Book, Set<String>> bookToGenres;
  private Map<String, Set<Book>> genreToBooks;

  public GenreRecommender(List<Book> books) {
    this.books = books;
    bookToGenres = new HashMap<Book, Set<String>>();
    genreToBooks = new HashMap<String, Set<Book>>();
    for (Book book : books) {
      Set<String> genres = getGenreSet(book.genre());
      bookToGenres.put(book, genres);
      for (String genre : genres) {
        if (genreToBooks.containsKey(genre)) { // genre already in map
          // add book to value list
          genreToBooks.get(genre).add(book);
        } else {
          // make new list
          genreToBooks.put(genre, Sets.newHashSet(book));
        }
      }
    }
  }

  private Set<String> getGenreSet(String genreString) {
    Set<String> genres = new HashSet<String>();
    for (String genre : genreString.toLowerCase().split(";")) {
      genres.add(genre);
    }
    return genres;
  }

  public ImmutableSet<String> getGenres(Book book) {
    Set<String> genres = bookToGenres.get(book);
    if (genres == null) {
      return ImmutableSet.of();
    }
    return ImmutableSet.copyOf(genres);
  }

  public ImmutableSet<Book> getBooksWithExactGenres(Set<String> genres) {
    Set<Book> matchingBooks = new HashSet<Book>();
    for (Book book : bookToGenres.keySet()) {
      if (bookToGenres.get(book).equals(genres)) {
        matchingBooks.add(book);
      }
    }
    if (matchingBooks.isEmpty()) {
      return ImmutableSet.of();
    }
    return ImmutableSet.copyOf(matchingBooks);
  }
}