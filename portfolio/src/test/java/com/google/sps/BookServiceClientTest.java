import com.google.sps.data.BookServiceClient;
import com.google.sps.data.Book;
import java.text.ParseException;
import java.lang.IllegalAccessException;
import java.lang.NullPointerException;
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
  
  @Test
  public void getTopResult(){
    //calls the API with the title and checks to make sure it only returns one item
    String title = "A Court of Wings and Ruin";
    String bookInfo = "";
    try{
        bookInfo = BookServiceClient.getBookInfo(title);
    }catch(Exception e){
        String failureMessage = "Was not able to query the Book API. \n"+e;
        Assert.fail(failureMessage);
    }
    JSONObject jsonObject = new JSONObject(bookInfo);
    Assert.assertEquals(true,jsonObject.has("volumeInfo"));
    Assert.assertEquals(false,jsonObject.has("items"));
  }

  @Test(expected = NullPointerException.class)
  public void queryNull() throws Exception{
      //attempts to query a null value
      BookServiceClient.getBookInfo(null);
  }

  @Test(expected = NullPointerException.class)
  public void queryEmpty() throws Exception{
      //attempts to query an empty value
      BookServiceClient.getBookInfo("");
  }
}

