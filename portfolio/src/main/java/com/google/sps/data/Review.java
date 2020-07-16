package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.sps.data.Book;
import com.google.sps.data.User;

@AutoValue
public abstract class Review {
  public static Builder builder() {
    return new AutoValue_Review.Builder();
  }

  public abstract Builder toBuilder();
  public abstract String fullText();
  public abstract Book book();
  public abstract User user();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder fullText(String fullText);
    public abstract Builder book(Book book);
    public abstract Builder user(User user);
    public abstract Review build();
  }
}
