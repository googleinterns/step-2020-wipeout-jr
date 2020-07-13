package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.lang.NoSuchFieldException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullBook {
  public String title, genre, language, description, infoLink, pageCount, publishedDate, publisher,
      maturityRating;
  public ArrayList<String> authors = new ArrayList<String>();
  public ArrayList<String> categories = new ArrayList<String>();
  public String thumbnail;
  public String isbn; // stores isbn 13 value

  public FullBook(String title, ArrayList<String> authors, String isbn, String description) {
    this.title = title;
    this.isbn = isbn;
    this.authors = authors;
    this.description = description;
  }

  // for tester
  public FullBook(String title, String language, String pageCount, String publishedDate,
      String publisher, String maturityRating, String infoLink, String thumbnail,
      ArrayList<String> authors, String isbn) {
    this.title = title;
    this.isbn = isbn;
    this.authors = authors;
    this.language = language;
    this.pageCount = pageCount;
    this.publishedDate = publishedDate;
    this.publisher = publisher;
    this.maturityRating = maturityRating;
    this.infoLink = infoLink;
    this.thumbnail = thumbnail;
  }

  public String getStringField(String fieldName) throws NoSuchFieldException {
    switch (fieldName) {
      case "title": {
        return this.title;
      }
      case "genre": {
        return this.title;
      }
      case "language": {
        return this.language;
      }
      case "description": {
        return this.description;
      }
      case "infoLink": {
        return this.infoLink;
      }
      case "pageCount": {
        return this.pageCount;
      }
      case "publishedDate": {
        return this.publishedDate;
      }
      case "publisher": {
        return this.publisher;
      }
      case "maturityRating": {
        return this.maturityRating;
      }
      case "thumbnail": {
        return this.thumbnail;
      }
      case "isbn": {
        return this.isbn;
      }
      default: {
        throw new NoSuchFieldException();
      }
    }
  }

  public void setStringField(String fieldName, String value) throws NoSuchFieldException {
    switch (fieldName) {
      case "title":
        this.title = value;
        break;
      case "language":
        this.language = value;
        break;
      case "genre":
        this.genre = value;
        break;
      case "description":
        this.description = value;
        break;
      case "infoLink":
        this.infoLink = value;
        break;
      case "pageCount":
        this.pageCount = value;
        break;
      case "publishedDate":
        this.publishedDate = value;
        break;
      case "publisher":
        this.publisher = value;
        break;
      case "maturityRating":
        this.maturityRating = value;
        break;
      case "thumbnail":
        this.thumbnail = value;
        break;
      case "isbn":
        this.isbn = value;
        break;
      default: {
        throw new NoSuchFieldException();
      }
    }
  }

  public ArrayList<String> getArrayField(String fieldName) throws NoSuchFieldException {
    if (fieldName.equals("categories")) {
      return this.categories;
    } else {
      return this.authors;
    }
  }

  public void setArrayField(String fieldName, List<String> value) {
    if (fieldName.equals("categories")) {
      this.categories = (ArrayList<String>) value;
    } else {
      this.authors = (ArrayList<String>) value;
    }
  }

  public FullBook() {}

  @Override
  public String toString() {
    return String.format("Title: %s with isbn: %s", this.title, this.isbn);
  }

  public String toStringAll() {
    String format = "";
    format += String.format("Title: %s with genre:%s in %s with desc: %s", this.title, this.genre,
        this.language, this.description);
    format +=
        String.format("This book has %s pages, published on %s by %s with a maturity rating of %s",
            this.pageCount, this.publishedDate, this.publisher, this.maturityRating);
    format += String.format("infoLink %s and thumbnail %s", this.infoLink, this.thumbnail);
    format += String.format("the isbn is %s", this.isbn);
    return format;
  }

  public String toStringNoDesc() {
    String format = "";
    format +=
        String.format("Title: %s with genre:%s in %s. ", this.title, this.genre, this.language);
    format += String.format(
        "This book has %s pages, published on %s by %s with a maturity rating of %s. ",
        this.pageCount, this.publishedDate, this.publisher, this.maturityRating);
    format += String.format("infoLink %s and thumbnail %s", this.infoLink, this.thumbnail);
    format += String.format("the isbn is %s", this.isbn);
    return format;
  }
}

