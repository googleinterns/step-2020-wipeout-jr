import com.google.sps.data.BookAPI;
import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.FullBook;
import com.google.sps.data.RequestJson;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.lang.IllegalAccessException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
 
/**
   This is a test class for RequestJson.java
    Tests the implementations ability to convert
    strings into a FullBook with the correct fields
*/
 
@RunWith(JUnit4.class)
public final class RequestJsonTest {
  @Test
  public void simpleCallAndCompare() {
    //calls the API with the title and compares the result
    String title = "A Court of Wings and Ruin";
    BookAPI BookAPI = new BookAPI();
    ArrayList<FullBook> actualAsList = BookAPI.search(title,1);//how many results you want
    FullBook actual = actualAsList.get(0);
    String actualString = actual.toStringAll();
    System.out.println(actualString);

    Map<String, String> isbn = new HashMap<String,String>();
    isbn.put("ISBN_10","161963449X");
    isbn.put("ISBN_13","9781619634497");
    
    ArrayList<String> authors = new ArrayList<String>();
    authors.add("Sarah J. Maas");

    String language = "en";
    String pageCount = "432";
    String publishedDate = "2017-05-02";
    String publisher = "Bloomsbury Publishing USA";
    String maturityRating = "NOT_MATURE";
    String infoLink = "https://play.google.com/store/books/details?id=pLL-DAAAQBAJ&source=gbs_api";
    String thumbnail = "http://books.google.com/books/content?id=pLL-DAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";

    FullBook expected = new FullBook(title,language, pageCount, 
        publishedDate, publisher, maturityRating, infoLink, thumbnail, authors, isbn);

    String expectedString = expected.toStringAll();
    System.out.println(expectedString);

    System.out.println(expectedString.equals(actualString));

    Assert.assertEquals(expected.title, actual.title);
    Assert.assertEquals(expected.isbn, actual.isbn);
    Assert.assertEquals(expected.authors, actual.authors);
    Assert.assertEquals(expected.language, actual.language);
    Assert.assertEquals(expected.pageCount, actual.pageCount);
    Assert.assertEquals(expected.publishedDate, actual.publishedDate);
    Assert.assertEquals(expected.publisher, actual.publisher);
    Assert.assertEquals(expected.maturityRating, actual.maturityRating);
    Assert.assertEquals(expected.infoLink, actual.infoLink);
    Assert.assertEquals(expected.thumbnail, actual.thumbnail);
  }
}

