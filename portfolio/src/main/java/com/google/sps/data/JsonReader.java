// Java program to read JSON from a file
package com.google.sps.data;
 
import com.google.gson.Gson;
import com.google.sps.data.GsonBook;
import java.text.ParseException;
 
public class JsonReader {
  public JsonReader() {}
 
  public GsonBook toGsonBook(String jsonString) throws ParseException {
    Gson gson = new Gson();
    GsonBook gsonBookObject = gson.fromJson(jsonString, GsonBook.class);
    return gsonBookObject;
  }
}

