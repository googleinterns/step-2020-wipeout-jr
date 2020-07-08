import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.GsonBook;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
 
/**
   This is a test class for JsonReader.java
    Tests the implementations ability to convert
    strings into a GsonBook with the correct fields
*/
 
@RunWith(JUnit4.class)
public final class JsonReaderTest {
  @Test
  public void singleValues() {
    // all of the values in the key-value-pairs are single strings
    GsonBook expected = new GsonBook("title", "genre", "categories", "author", "language",
        "description", "infoLink", "pageCount", "publishDate", "publisher", "maturityRating");
 
    String content = "{"
        + "\"title\": \"title\","
        + "\"genre\": \"genre\","
        + "\"categories\": \"categories\","
        + "\"author\": \"author\","
        + "\"language\": \"language\","
        + "\"description\": \"description\","
        + "\"infoLink\": \"infoLink\","
        + "\"pageCount\": \"pageCount\","
        + "\"publishDate\": \"publishDate\","
        + "\"unwanted1\": \"should not appear\","
        + "\"unwanted2\": \"should not appear\","
        + "\"publisher\": \"publisher\","
        + "\"maturityRating\": \"maturityRating\"}";
 
    JsonReader convert = new JsonReader();
    try {
      GsonBook actual = convert.toGsonBook(content);
      ArrayList<Object> holder = new ArrayList<Object>();
      for (Field f : expected.getClass().getDeclaredFields()) {
        holder.add(f.get(expected));
      }
      for (Field f : actual.getClass().getDeclaredFields()) {
        Assert.assertEquals(true, holder.contains(f.get(actual)));
      }
    } catch (ParseException e) {
      System.out.println("Could not parse.");
    } catch (IllegalAccessException e) {
      System.out.println("Cannot access");
    }
  }
}

