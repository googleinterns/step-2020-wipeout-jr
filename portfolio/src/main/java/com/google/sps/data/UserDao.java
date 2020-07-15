package com.google.sps.data;

import java.util.Optional;

public interface UserDao {
  public Optional<User> get(String email);

  public void upload(User user);

  public void update(User user);
}
