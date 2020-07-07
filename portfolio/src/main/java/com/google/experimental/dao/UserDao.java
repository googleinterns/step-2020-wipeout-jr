package com.google.experimental.dao;

import java.util.List;

public interface UserDao {
    User get(String id);
    List<User> getAll();
    void save(User user);
    void update(User user);
    void delete(User user);
}