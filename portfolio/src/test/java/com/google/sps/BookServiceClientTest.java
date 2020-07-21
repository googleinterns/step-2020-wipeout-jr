import com.google.sps.data.Book;
import com.google.sps.data.BookServiceClient;
import java.lang.IllegalAccessException;
import java.lang.NullPointerException;
import java.text.ParseException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
   This is a test class for BookServiceClient.java
    Tests the implementations ability to call the
    Book API, given the title, and return the book's information.
*/

@RunWith(JUnit4.class)
public final class BookServiceClientTest {
  // check to make sure it only returns N books

  @Test(expected = NullPointerException.class)
  public void queryNull() throws Exception {
    // attempts to query a null value
    BookServiceClient.getBookInfo(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void queryEmpty() throws Exception {
    // attempts to query an empty value
    BookServiceClient.getBookInfo("");
  }
}
