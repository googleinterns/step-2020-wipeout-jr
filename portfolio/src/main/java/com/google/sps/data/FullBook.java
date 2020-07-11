package com.google.sps.data;
//import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullBook {

    //private String title, description;
    public String title, genre, language, description, infoLink, pageCount, publishedDate, publisher, maturityRating;
    public ArrayList<String> authors = new ArrayList<String>();
    public ArrayList<String> categories = new ArrayList<String>();
    public String thumbnail;
    public Map<String, String> isbn = new HashMap<String, String>();

    public FullBook(String title, ArrayList<String> authors, Map<String, String> isbn, String description) {
        this.title = title;
        this.isbn = isbn;
        this.authors = authors;
        this.description = description;

    }
    
    //for tester
    public FullBook(String title,String language, String pageCount, String publishedDate, String publisher, String maturityRating, String infoLink, String thumbnail, ArrayList<String> authors, Map<String, String> isbn) {
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

    public FullBook() {
    }

    public Map<String, String> getisbn() {
        return isbn;
    }

    public void setIsbn(Map<String, String> isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return String.format("Title: %s with isbn: %s", this.title,this.isbn);
    }

    public String toStringAll() {
        String format = "";
        format += String.format("Title: %s with genre:%s in %s with desc: %s", this.title, this.genre, this.language, this.description);
        format += String.format("This book has %s pages, published on %s by %s with a maturity rating of %s", this.pageCount, this.publishedDate, this.publisher, this.maturityRating);
        format += String.format("infoLink %s and thumbnail %s",this.infoLink,this.thumbnail);
        format += String.format("the isbn is %s",this.isbn);
        return format;
    }

    public String toStringNoDesc() {
        String format = "";
        format += String.format("Title: %s with genre:%s in %s. ", this.title, this.genre, this.language);
        format += String.format("This book has %s pages, published on %s by %s with a maturity rating of %s. ", this.pageCount, this.publishedDate, this.publisher, this.maturityRating);
        format += String.format("infoLink %s and thumbnail %s",this.infoLink,this.thumbnail);
        format += String.format("the isbn is %s",this.isbn);
        return format;
    }


}
