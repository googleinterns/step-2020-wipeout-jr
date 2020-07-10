package com.google.sps.data;
 
import java.lang.Enum;
import java.util.HashMap;
import java.util.Map;
 
public enum BookFieldsEnum {
  TITLE("title"),
  GENRE("genre"),
  CATEGORIES("categories"),
  AUTHORS("authors"),
  LANGUAGE("language"),
  DESCRIPTION("description"),
  INFO_LINK("infoLink"),
  PAGE_COUNT("pageCount"),
  PUBLISH_DATE("publishedDate"),
  PUBLISHER("publisher"),
  MATURITY_RATING("maturityRating");
 
  private final String javaScriptProperty;
 
  private BookFieldsEnum(String javaScriptProperty) {
    this.javaScriptProperty = javaScriptProperty;
  }
 
  public String getJSProperty() {
    return this.javaScriptProperty;
  }
}

