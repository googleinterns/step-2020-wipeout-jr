//  RequestJson.java
//  Sample Google API
//
//  Created by Mohammadreza on 10/7/18.
//  Copyright © 2019 Mohammadreza Mohades. All rights reserved.

package com.google.sps.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.FullBook;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.IllegalAccessException;
import java.lang.NoSuchFieldException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestJson {
  private static final String VOLUME_INFO = "volumeInfo";
  private static final String IMG_LINKS = "imageLinks";

  public RequestJson() {}

  public JSONObject requestQuery(String query) throws Exception {
    // This method calls the actual Book API from the URL and returns a JSON Object from the
    // response convert space to url format

    String url = String.format("https://www.googleapis.com/books/v1/volumes?q=%s", query);
    // this link contains different data than the book's selfLink
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    System.out.println("Created Buffered Reader: "+in);
    String inputLine;
    StringBuffer response = new StringBuffer(); // will contain the large string of information
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    JSONObject myResponse = new JSONObject(response.toString());
    return myResponse;
  }

  public ImmutableList<FullBook> getBookList(String query, int numberResults) throws Exception {
    File index = new File("output");

    if (!index.exists()) {
      index.mkdir();
    } else {
      String[] entries = index.list();
      for (String s : entries) {
        File currentFile = new File(index.getPath(), s);
        currentFile.delete();
      }
    }

    ArrayList<FullBook> returnedList = new ArrayList<FullBook>();

    JSONObject myResponse =
        new JSONObject(requestQuery(query).toString()); // request Query returns a JSON object of
                                                        // the JSON string with all the data
    JSONArray allItems = myResponse.getJSONArray("items");

    for (int i = 0; i < numberResults; i++) {
      returnedList.add(jsonToFullBook(allItems.getJSONObject(i))); // jsonToFullBook
    }
    ImmutableList<FullBook> immutableBookList = 
                         ImmutableList.copyOf(bookList); 
    return immutableBookList;
  }

  public FullBook jsonToFullBook(JSONObject jsonObject) throws JSONException {
    FullBook newBook = new FullBook();
    // Map<String, String> tempISBN = new HashMap<String, String>();
    String tempISBN = ""; // double nested [{}]

    tempISBN = getIsbn(jsonObject).get("ISBN_13");
    try {
      newBook.setStringField("isbn", tempISBN);
    } catch (Exception e) {
      e.printStackTrace();
    }

    for (BookFieldsEnum field : BookFieldsEnum.values()) {
      String jsProperty = field.getJSProperty(); // the string value in the enum
      String tempProperty = "Undefined";
      List<String> tempPropertyArray = new ArrayList<String>();
      boolean isArray = false;

      if (!jsProperty.equals("genre") && !jsProperty.equals("isbn")) {
        try {
          if (jsProperty.equals("categories") || jsProperty.equals("authors")) {
            // seperate because they exists as arrays in the JSON object [] and genre has a
            // different name ("categories")
            isArray = true;
            tempArrayProperty = getArrayInObj(jsProperty,jsonObject);
          } else if (jsProperty.equals("thumbnail")) {
            // seperate because it is nested in another object {}
            if (jsonObject.getJSONObject(VOLUME_INFO).has(IMG_LINKS)
                && jsonObject.getJSONObject(VOLUME_INFO)
                       .getJSONObject(IMG_LINKS)
                       .has(jsProperty)) {
              tempProperty = jsonObject.getJSONObject(VOLUME_INFO)
                                 .getJSONObject(IMG_LINKS)
                                 .get(jsProperty)
                                 .toString();
            }
          } else {
            tempProperty = getStringInObj(jsProperty,jsonObject);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
        try {
          if (isArray) {
            newBook.setArrayField(jsProperty, tempPropertyArray);
          } else {
            newBook.setStringField(jsProperty, tempProperty);
          }
        } catch (NoSuchFieldException e) {
          e.printStackTrace();
        }
      }
    }
    return newBook;
  }

  private String getStringInObj(String jsProperty, JSONObject jsonObject){
      //gets the value assigned in the json object to this key
      String tempProperty = "";
      if (jsonObject.getJSONObject(VOLUME_INFO).has(jsProperty)) {
              tempProperty = jsonObject.getJSONObject(VOLUME_INFO).get(jsProperty).toString();
            }
      return tempProperty;
  }

  private ArrayList<String> getArrayInObj(String jsProperty, JSONObject jsonObject){
      //gets the value assigned in the json object to this key
      List<String> tempPropertyArray = new ArrayList<String>;
      if (jsonObject.getJSONObject(VOLUME_INFO).has(jsProperty)) {
              tempPropertyArray = jsonArrayToStringArray(
                  jsonObject.getJSONObject(VOLUME_INFO).getJSONArray(jsProperty));
            } else {
              tempPropertyArray = null;
            }
      return tempPropertyArray;
  }

  public ImmutableMap<String,String> getIsbn(JSONObject item) throws JSONException {
    Gson gson = new Gson();

    String tempType = "", tempISBN = "";
    Map<String, String> isbnMap = new HashMap<String, String>();
    int counter = 0;
    if (item.getJSONObject(VOLUME_INFO).has("industryIdentifiers")) {
      JSONArray tempArr = item.getJSONObject(VOLUME_INFO).getJSONArray("industryIdentifiers");
      Type listType = new TypeToken<List<HashMap<String, String>>>() {}.getType();
      List<HashMap<String, String>> isbns = gson.fromJson(tempArr.toString(), listType);

      for (HashMap<String, String> map : isbns) {
        counter = 0;
        for (HashMap.Entry<String, String> entry : map.entrySet()) {
          if (counter == 0) {
            tempISBN = entry.getValue(); // Key is identifier
          }
          if (counter == 1) {
            tempType = entry.getValue();
            // Key is type
          }
          counter++;
        }
        if (tempType != null || tempISBN != null){
          isbnMap.put(tempType, tempISBN);
        }
      }
      ImmutableMap<String,String> immutableIsbnMap = 
                         ImmutableMap.copyOf(isbnMap); 
      return immutableIsbnMap;
    }
    return null;
  }

  public void printMap(Map<String, String> map) {
    for (HashMap.Entry<String, String> entry : map.entrySet()) {
      System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
    }
  }

  public ArrayList<String> jsonArrayToStringArray(JSONArray jsArray) {
    ArrayList<String> strArray = new ArrayList<String>();

    for (int j = 0; j < jsArray.length(); j++) {
      try {
        strArray.add(jsArray.get(j).toString());

      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return strArray;
  }

  public Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
    Map<String, Object> retMap = new HashMap<String, Object>();

    if (json != JSONObject.NULL) {
      retMap = toMap(json);
    }
    return retMap;
  }

  public Map<String, Object> toMap(JSONObject object) throws JSONException {
    Map<String, Object> map = new HashMap<String, Object>();

    Iterator<String> keysItr = object.keys();
    while (keysItr.hasNext()) {
      String key = keysItr.next();
      Object value = object.get(key);

      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      map.put(key, value);
    }
    return map;
  }

  public List<Object> toList(JSONArray array) throws JSONException {
    List<Object> list = new ArrayList<Object>();
    for (int i = 0; i < array.length(); i++) {
      Object value = array.get(i);
      if (value instanceof JSONArray) {
        value = toList((JSONArray) value);
      } else if (value instanceof JSONObject) {
        value = toMap((JSONObject) value);
      }
      list.add(value);
    }
    return list;
  }
}

