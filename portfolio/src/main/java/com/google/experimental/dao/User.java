package com.google.experimental.dao;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;

/** In-memory representation of the User object. */
@AutoValue
public abstract class User {
  public static User create(String id, String email) {
    return new AutoValue_User(id, email);
  }

  public abstract String id();
  public abstract String email();
}
