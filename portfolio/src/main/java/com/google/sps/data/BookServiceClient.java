package com.google.sps.data;
 
import com.google.sps.data.Book;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
// Given a book name, fetches book info json by calling Google books api and returns as string.
public class BookServiceClient {
 
  public static String getBookInfo(String bookName){
    if (bookName.equals("")){
      return null;
    }
    String encodedBookName = null;
    try {
      encodedBookName = URLEncoder.encode(bookName, "UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
 
    try {
      String allResults = queryBooksAPI(encodedBookName);
      return getTopResult(allResults);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
 
  private static String getTopResult(String booksApiResults){
    JSONObject jsonObject = new JSONObject(booksApiResults);
    JSONArray allItems = jsonObject.getJSONArray("items");
    JSONObject firstItem = allItems.getJSONObject(0);//returns top result'
    return firstItem.toString();
  }
 
  public static String queryBooksAPI(String bookName) throws Exception {
    String url =
        String.format("https://www.googleapis.com/books/v1/volumes?country=US&q=%s", bookName);
    // this link contains different data than the book's selfLink
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();
 
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer(); // will contain the large string of information
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }
}

