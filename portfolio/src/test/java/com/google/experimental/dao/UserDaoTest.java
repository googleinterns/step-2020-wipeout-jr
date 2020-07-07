package com.google.experimental.dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link UserDao}.
 */
@RunWith(JUnit4.class)
public final class UserDaoTest {
  private static final User USER1 = User.create("saur", "saur@google.com");
  private static final User USER2 = User.create("amanhey", "amanhey@google.com");
  private static final User USER3 = User.create("danyagao", "danyagao@google.com");
  private static final User USER4 = User.create("mitraan", "mitraan@google.com");

  private UserDao userDao;

  @Before
  public void setUp() {
    userDao = new UserDaoDataStoreImpl();
  }

  @Test
  public void saveAndGet() {
      userDao.save(USER1);
      Assert.assertEquals(userDao.get(USER1.id()), USER1);
  }

  @Test
  public void list() {
      userDao.save(USER1);
      userDao.save(USER2);
      userDao.save(USER3);
      userDao.save(USER4);
      List<User> users = userDao.getAll();
      Assert.assertTrue(users.contains(USER1));
      Assert.assertTrue(users.contains(USER2));
      Assert.assertTrue(users.contains(USER3));
      Assert.assertTrue(users.contains(USER4));
  }
}