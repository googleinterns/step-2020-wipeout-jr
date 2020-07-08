import com.google.sps.data.StringBufToMap;
import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** This is a test class for StringBufToMap.java
*/

@RunWith(JUnit4.class)
public final class StringBufToMapTest {
  @Test
  public void stringValues(){
      Map<String,String> expected = new HashMap<String,String>();
      expected.put("TITLE","title");
      expected.put("GENRE","genre");
      expected.put("CATEGORIES","categories");
      expected.put("AUTHOR","author");
      expected.put("LANGUAGE","language");
      expected.put("DESCRIPTION","description");
      expected.put("INFO_LINK","infoLink");
      expected.put("PAGE_COUNT","pageCount");
      expected.put("PUBLISH_DATE","publishedDate");
      expected.put("PUBLISHER","publisher");
      expected.put("MATURITY_RATING","maturityRating");

    StringBuffer sb =  new StringBuffer();
    String content = "{\"TITLE\": \"title\",\"GENRE\": \"genre\",\"CATEGORIES\": \"categories\",\"AUTHOR\": \"author\",\"LANGUAGE\": \"language\",\"DESCRIPTION\": \"description\",\"INFO_LINK\": \"infoLink\",\"PAGE_COUNT\": \"pageCount\",\"PUBLISH_DATE\": \"publishedDate\",\"PUBLISHER\": \"publisher\",\"MATURITY_RATING\": \"maturityRating\"}";
    sb.append(content);
    /*Tests when all the values are only strings
    */
    StringBufToMap translate = new StringBufToMap();
    Map<String,String> actual = translate.toMap(sb);
    System.out.println("Translated: " + actual);
    System.out.println("Expected: " + expected);

    //checks that the entities retrieved by the key and by passing the entity are the same
    Assert.assertEquals(expected, actual);
    //checks all fields are the same as original
    for(String key : expected.keySet()){
         Assert.assertEquals(actual.get(key),expected.get(key));
    }
    Assert.assertEquals(expected.size(),actual.size());
  }
}

