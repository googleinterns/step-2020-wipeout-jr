package com.google.sps.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {
  public abstract String email();
  public abstract String nickname();

  public static User create(String email, String nickname) {
    return builder().setEmail(email).setNickname(nickname).build();
  }

  static Builder builder() {
    return new AutoValue_User.Builder();
  }

  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setEmail(String email);

    abstract Builder setNickname(String nickname);

    abstract User build();
  }
}
