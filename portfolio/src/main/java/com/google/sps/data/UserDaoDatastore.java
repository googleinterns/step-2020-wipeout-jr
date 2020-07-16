package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Optional;

/**
 * Access Datastore to manage Users
 */
public class UserDaoDatastore implements UserDao {
  private DatastoreService datastore;
  UserService userService;

  public UserDaoDatastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
    userService = UserServiceFactory.getUserService();
  }

  /**
   * Get a user entity from Datastore
   * @param String email: email used as an id for the user
   * @return Optional: returns the entity if it exists, else return an empty
   * Optional instance
   */
  @Override
  public Optional<User> get(String email) {
    Entity userEntity;
    try {
      userEntity = datastore.get(createKey(email));
    } catch (EntityNotFoundException ex) {
      return Optional.empty();
    }
    return Optional.of(entityToUser(userEntity));
  }

  /**
   * Upload a user entity to Datastore
   * @param User user: The User to be uploaded
   */
  @Override
  public void upload(User user) {
    datastore.put(userToEntity(user));
  }

  /**
   * Update a user entity in Datastore
   * @param User user: The User to be updated
   */
  @Override
  public void update(User user) {
    datastore.put(userToEntity(user));
  };

  /**
   * Creates a User object from an entity
   * @param Entity entity: the entity representing the user to be created
   * @return User user: the user that is created
   */
  private static User entityToUser(Entity userEntity) {
    return User.create(
        (String) userEntity.getProperty("email"), (String) userEntity.getProperty("nickname"));
  }

  /**
   * Creates a datastore entity from a user object
   * @param User user: The user object that the entity will be created of
   * @return User user: the entity that is created
   */
  private Entity userToEntity(User user) {
    String userEmail = userService.getCurrentUser().getEmail();
    Entity userEntity = new Entity(createKey(userEmail));

    userEntity.setProperty("email", userEmail);
    userEntity.setProperty("nickname", user.nickname());

    return userEntity;
  }

  private Key createKey(String email) {
    return KeyFactory.createKey("User", email);
  }
}
