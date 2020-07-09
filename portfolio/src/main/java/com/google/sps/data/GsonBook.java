package com.google.sps.data;
 
public class GsonBook {
  // the object that is created when the string is read from the csv file and the Books API
 
  public String title;
  public String genre;
  public String categories;
  public String author;
  public String language;
  public String description;
  public String infoLink;
  public String pageCount;
  public String publishDate;
  public String publisher;
  public String maturityRating;
 
  public GsonBook(String title, String genre, String categories, String author, String language,
      String description, String infoLink, String pageCount, String publishDate, String publisher,
      String maturityRating) {
    this.title = title;
    this.genre = genre;
    this.categories = categories;
    this.author = author;
    this.language = language;
    this.description = description;
    this.infoLink = infoLink;
    this.pageCount = pageCount;
    this.publishDate = publishDate;
    this.publisher = publisher;
    this.maturityRating = maturityRating;
  }

  public String printObject(){
    String formatter = ""
    + this.title + ", "
    + this.genre + ", "
    + this.author + ", "
    + this.language + ", "
    + this.description + ", "
    + this.infoLink + ", "
    + this.pageCount + ", "
    + this.publishDate + ", "
    + this.publisher + ", "
    + this.maturityRating + ".";
    return formatter;
  }
}

