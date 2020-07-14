package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

public interface UserDao {
    Entity get(String id);
    void upload();
}