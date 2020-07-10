import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.RequestJson;
import com.google.sps.data.BookAPI;
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
public final class RequestJsonTest {
  @Test
  public void singleValues() {
    // all of the values in the key-value-pairs are single strings
    /*GsonBook expected = new GsonBook("title", "genre", "categories", "author", "language",
        "description", "infoLink", "pageCount", "publishDate", "publisher", "maturityRating");*/
 
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
 
    String title = "A Court of Wings and Ruin";
    BookAPI.search(title);
  }
}

