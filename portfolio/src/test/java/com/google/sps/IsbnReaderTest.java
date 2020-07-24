package com.google.sps;

import com.google.sps.data.IsbnReader;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This is a test class for IsbnReader.java
 * It only tests the basic functionality for reading from a CSV file.
 * It does not check that the results match those from the Books API.
 */
@RunWith(JUnit4.class)
public final class IsbnReaderTest {
  private final String COURT_TITLE = "A Court of Wings and Ruin";
  private final String COURT_ISBN = "9781619634497";
  private final String HATE_TITLE = "The Hate U Give";
  private final String HATE_ISBN = "9780062498557";
  private Map<String, String> isbnMap;

  @Test
  public void basicSanityTests() {
    try {
      IsbnReader reader = new IsbnReader("src/test/java/com/google/sps/isbnTestFile.csv");
      isbnMap = reader.makeIsbnMap();
    } catch (Exception ex) {
      Assert.fail("Could not open file. " + ex);
    }
    Assert.assertEquals(COURT_ISBN, isbnMap.get(COURT_TITLE));
    Assert.assertEquals(HATE_ISBN, isbnMap.get(HATE_TITLE));
  }
}

