package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Set;

@AutoValue
public abstract class Book {
  public static Builder builder() {
    return new AutoValue_Book.Builder();
  }

  public abstract Builder toBuilder();
  public abstract String title();
  public abstract Set<String> genre();
  public abstract ImmutableList<String> reviews();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder title(String title);
    public abstract Builder genre(Set<String> genre);
    protected abstract ImmutableList.Builder<String> reviewsBuilder();

    public Builder addReview(String review) {
      review = cleanReview(review);
      reviewsBuilder().add(review);
      return this;
    }

    /**
     *In this function, we use replaceAll instead of encoding/decoding
     * methods because the bad characters do not come in during our encoding,
     * but rather, already exist in the file that we're reading from.
     * The two replacements in the first line are in fact, different
     * If anyone knows why or how to fix this in a cleaner way, please let ankita know!
     **/
    private String cleanReview(String original) {
      String newString = original.replaceAll("â€", "'").replaceAll("â€", "'");
      newString = newString.replaceAll("[^a-zA-Z0-9, .:*?!'#<>(){}/-]", "");
      return newString;
    }
    public abstract Book build();
  }
}
