package com.google.sps.data;

import static com.google.common.base.Preconditions.checkNotNull;

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
import com.google.common.base.Preconditions;
import java.util.Optional;

/**
 * Access Datastore to manage Users
 */
public class UserDaoDatastore implements UserDao {
  private DatastoreService datastore;
  private UserService userService;
  private static final String EMAIL = "email";
  private static final String NICKNAME = "nickname";

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
      validateEmail(email);
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
    validateEmail(user.email());
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
        (String) userEntity.getProperty(EMAIL), (String) userEntity.getProperty(NICKNAME));
  }

  /**
   * Creates a datastore entity from a user object
   * @param User user: The user object that the entity will be created of
   * @return User user: the entity that is created
   */
  private Entity userToEntity(User user) {
    String email = user.email();
    Entity userEntity = new Entity(createKey(email));

    userEntity.setProperty(EMAIL, email);
    userEntity.setProperty(NICKNAME, user.nickname());

    return userEntity;
  }

  private Key createKey(String email) {
    return KeyFactory.createKey("User", email);
  }

  /**
   * Checks to see if the email is valid
   * @param String email: The email you want to validate
   */
  private void validateEmail(String email) {
    Preconditions.checkNotNull(email, "The email cannot be null");
    Preconditions.checkArgument(email.contains("@"), "The email must be a valid email");
  }
}
