package com.google.sps;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.sps.data.User;
import com.google.sps.data.UserDao;
import com.google.sps.data.UserDaoDatastore;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

/**
 * Unit tests for UserDaoDatastore.java, testing implementation for getting
 * a user, uploading, and updating
 */

@RunWith(JUnit4.class)
public final class UserDaoDatastoreTest extends Mockito {
  private final UserDaoDatastore userStorage = new UserDaoDatastore();
  private static final String EMAIL = "potter@google.com";
  private static final String NICKNAME = "Harry";
  private static final User USER = User.create(EMAIL, NICKNAME);
  private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig().setApplyAllHighRepJobPolicy());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test(expected = NoSuchElementException.class)
  public void getUnsavedUser() {
    userStorage.get(EMAIL).get();
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidEmail() {
    userStorage.get("danyagaogoogle.com").get();
  }

  @Test
  public void emptyOptional() {
    Optional<User> actual = userStorage.get(EMAIL);
    Optional<User> expected = Optional.empty();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void getSavedUser() {
    userStorage.upload(USER);
    Optional<User> actual = userStorage.get(EMAIL);
    Assert.assertEquals(USER, actual.get());
  }

  @Test(expected = NullPointerException.class)
  public void uploadNullUser() {
    userStorage.upload(null);
  }

  @Test
  public void updateUserInfo() {
    User updatedUser = User.create("potter@google.com", "Ron WEASLEY");

    userStorage.upload(USER);
    userStorage.update(updatedUser);

    User expected = updatedUser;
    User actual = userStorage.get(EMAIL).get();

    Assert.assertEquals(expected, actual);
  }
}
