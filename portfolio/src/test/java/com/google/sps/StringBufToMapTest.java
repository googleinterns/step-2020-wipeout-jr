import com.google.sps.data.StringBufToMap;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * This is a test class for StringBufToMap.java
 */

@RunWith(JUnit4.class)
public final class StringBufToMapTest {
  // TODO: change strings to immutable lists
  @Test
  public void stringValues() {
    // tests for String Buffers with only single string values
    Map<String, Object> expected = new HashMap<String, Object>();
    expected.put("TITLE", "title");
    expected.put("GENRE", "genre");
    expected.put("CATEGORIES", "categories");
    expected.put("AUTHOR", "author");
    expected.put("LANGUAGE", "language");
    expected.put("DESCRIPTION", "description");
    expected.put("INFO_LINK", "infoLink");
    expected.put("PAGE_COUNT", "pageCount");
    expected.put("PUBLISH_DATE", "publishDate");
    expected.put("PUBLISHER", "publisher");
    expected.put("MATURITY_RATING", "maturityRating");

    StringBuffer sb = new StringBuffer();
    String content = "{"
        + "\"TITLE\": \"title\","
        + "\"GENRE\": \"genre\","
        + "\"CATEGORIES\": \"categories\","
        + "\"AUTHOR\": \"author\","
        + "\"LANGUAGE\": \"language\","
        + "\"DESCRIPTION\": \"description\","
        + "\"INFO_LINK\": \"infoLink\","
        + "\"PAGE_COUNT\": \"pageCount\","
        + "\"PUBLISH_DATE\": \"publishDate\","
        + "\"PUBLISHER\": \"publisher\","
        + "\"MATURITY_RATING\": \"maturityRating\"}";
    sb.append(content);
    // Tests when all the values are only strings

    StringBufToMap translate = new StringBufToMap();
    Map<String, Object> actual = translate.toMap(sb);
    ;

    // checks all fields are the same as original
    for (Object value : expected.values()) {
      Assert.assertEquals(true, actual.containsValue(value));
    }
    for (String key : expected.keySet()) {
      Assert.assertEquals(true, actual.containsKey(key));
    }
    Assert.assertEquals(expected.size(), actual.size());
  }
}

