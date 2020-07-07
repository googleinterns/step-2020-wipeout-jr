package com.google.experimental.dao;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class UserDaoInMemoryImpl implements UserDao {
    private final Map<String, User> users = new HashMap<String, User>();

    @Override
    public User get(String id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll(){
        return ImmutableList.copyOf(users.values());
    }

    @Override
    public void save(User user) {
        if (users.containsKey(user.id())) {
            throw new RuntimeException("User already exists: " + user.id());
        }
        users.put(user.id(), user);
    }

    @Override
    public void update(User user) {
        if (!users.containsKey(user.id())) {
            throw new RuntimeException("User with this id does not exists: " + user.id());
        }
        users.put(user.id(), user);
    }

    @Override
    public void delete(User user) {
        if (!users.containsKey(user.id())) {
            throw new RuntimeException("User with this id does not exists: " + user.id());
        }
        users.remove(user.id());
    }
}