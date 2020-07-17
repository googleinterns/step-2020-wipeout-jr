package com.google.sps.data;
 
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.sps.data.Book;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.NullPointerException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
// Given a book name, fetches book info json by calling Google books api and returns as string.
public class BookServiceClient {

  /**
  * Validates the book name and gets the most relevent result
  * @param bookName: the name of the book you want to search for
  */
  public static String getBookInfo(String bookName){
    validate(bookName);
    String allResults = queryBooksAPI(bookName);
    if(allResults == null){
      return null;
    }
    return getTopResult(allResults);
  }

  /**
  * Takes the response from the book api and returns only the top result
  * @param bookApiResults: the json string given by the api
  */
  private static String getTopResult(String booksApiResults){
    JSONObject jsonObject = new JSONObject(booksApiResults);
    JSONArray allItems = jsonObject.getJSONArray("items");
    JSONObject firstItem = allItems.getJSONObject(0);//returns top result'
    return firstItem.toString();
  }

  /**
  * Provided the bookname it encodeds and formats it with the api
  * url and gets the results from the api by calling queryUrl 
  * @param bookName: the name of the book you want to search for
  */
  private static String queryBooksAPI(String bookName){
    try{
      String encodedBookName = null;
      encodedBookName = URLEncoder.encode(bookName, "UTF-8");
      String url = String.format("https://www.googleapis.com/books/v1/volumes?country=US&q=%s", encodedBookName);
      return queryURL(url);
    }
    catch(Exception e){
      return null;
    }
  }

  /**
  * It opens a connection and makes a GET request
  * to a url, reads that data through a buffered
  * reader, and returns a string representing that data
  * @param url: the that you want to query
  */
  public static String queryURL(String url) throws IOException, MalformedURLException, ProtocolException{
    URL obj = new URL(url); //can throw MalformedURLException
    HttpURLConnection con = (HttpURLConnection) obj.openConnection(); //can throw IOException
    con.setRequestMethod("GET"); //can throw ProtocolException
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode(); //can throw IOException
 
    try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
      String inputLine;
      StringBuffer response = new StringBuffer(); // will contain the large string of information
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      return response.toString();
    }catch(Exception e){
      return null;
    }
  }
  
  /**
  * Validates the string
  * @param bookName: the string you want to validate
  */
  private static void validate(String bookName){
    Preconditions.checkNotNull(bookName,"The book's name cannot be null");
    Preconditions.checkArgument(!bookName.equals(""),"The book's name cannot be an empty string");
  }
}

