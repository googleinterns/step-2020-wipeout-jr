package com.google.sps.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.nio.charset.Charset;

@AutoValue
public abstract class Book {
  public static Builder builder() {
    return new AutoValue_Book.Builder();
  }

  public abstract Builder toBuilder();
  public abstract String title();
  public abstract String genre();
  public abstract ImmutableList<String> reviews();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder title(String title);
    public abstract Builder genre(String genre);
    protected abstract ImmutableList.Builder<String> reviewsBuilder();

    public Builder addReview(String review) {
      review = cleanReview(review);
      reviewsBuilder().add(review);
      return this;
    }

    /** In this function, we use replaceAll along with enconding/decoding
      * methods because some of the bad characters do not come in during our encoding, 
      * but rather, already exist in the file that we're reading from. 
      * The first and last regex that we replace are in fact, different
      * If anyone knows why or how to fix this in a cleaner way, please let ankita know!
     **/
    private String cleanReview(String original) {
        String newString = original.replaceAll("â€", "'").replaceAll("™", "").replaceAll("œ", "").replaceAll("â€", "'");
        Charset charset = Charset.forName("UTF-8");
        newString = charset.decode(charset.encode(newString)).toString();
        
        return newString;
    }
    public abstract Book build();
  }
}
