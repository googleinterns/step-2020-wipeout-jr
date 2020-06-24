package com.google.sps.data;

import com.google.auto.value.AutoValue;
import java.util.ArrayList;

@AutoValue
public abstract class Book {
  public static Restaurant create(String title, ArrayList<String> reviews) {
    return new AutoValue_Book(title, reviews);
  }

  abstract String title();
  abstract ArrayList<String> reviews();
}