import com.google.sps.servlets.UploadBookWithListOfFields;
import com.google.sps.data.BookAPI;
import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.FullBook;
import com.google.sps.data.RequestJson;
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
  public void simpleCallAndCompare(){
    //calls the API with the title and compares the result

    String title = "A Court of Wings and Ruin";
    BookAPI BookAPI = new BookAPI();
    ArrayList<FullBook> actualAsList = BookAPI.search(title,1);//how many results you want
    FullBook actual = actualAsList.get(0);
    //System.out.println(actual.toStringAll());
  }
}

