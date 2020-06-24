package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;

@AutoValue
public abstract class Book {
  public static Builder builder() {
    return new AutoValue_Book.Builder();
  }

  public abstract Builder toBuilder();

  public abstract String title();

  public abstract ImmutableList<String> reviews();

  @AutoValue.Builder
  public static abstract class Builder {

    public abstract Builder title(String title);

    protected abstract ImmutableList.Builder<String> reviewsBuilder();

    public Builder addReview(String card) {
      reviewsBuilder().add(card);
      return this;
    }

    public abstract Book build();
  }
}


// @AutoValue
// public abstract class Book {
//   public static Book create(String title, ArrayList<String> reviews) {
//     return new AutoValue_Book(title, reviews);
//   }

//   abstract String title();
//   abstract ArrayList<String> reviews();
// }