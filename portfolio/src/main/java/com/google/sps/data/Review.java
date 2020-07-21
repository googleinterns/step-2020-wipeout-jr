package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.sps.data.Book;

@AutoValue
public abstract class Review {
  public static Builder builder() {
    return new AutoValue_Review.Builder();
  }

  public abstract Builder toBuilder();
  public abstract String fullText();
  public abstract String isbn();
  public abstract String email();
  public static Review create(String fullText, String isbn, String email) {
    return builder().fullText(fullText).isbn(isbn).email(email).build();
  }

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder fullText(String fullText);
    public abstract Builder isbn(String isbn);
    public abstract Builder email(String email);
    public abstract Review build();
  }
}
