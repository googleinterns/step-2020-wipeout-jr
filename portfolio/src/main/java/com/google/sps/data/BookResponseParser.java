package com.google.sps.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.sps.data.BookFieldsEnum;
import com.google.sps.data.Book;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Given a json String from Google Book Api, converts the response into a in-memory Book representation.
class BookResponseParser {
    private static final String VOLUME_INFO = "volumeInfo";
    private static final String TITLE = "title";
    private static final String GENRE = "genre";
    private static final String CATEGORIES = "categories";
    private static final String AUTHORS = "authors";
    private static final String LANGUAGE = "language";
    private static final String DESCRIPTION = "description";
    private static final String INFO_LINK = "infoLink";
    private static final String PAGE_COUNT = "pageCount";
    private static final String PUBLISHED_DATE = "publishedDate";
    private static final String PUBLISHER = "publisher";
    private static final String MATURITY_RATING = "maturityRating";
    private static final String THUMBNAIL = "thumbnail";
    private static final String IMAGE_LINKS = "imageLinks";
    private static final String ISBN = "ISBN_13";

  public Book parseBook(String jsonResponse){
    JSONObject myResponse = new JSONObject(jsonResponse.toString());
    return jsonToBook(myResponse);
  }

  public Book jsonToBook(JSONObject jsonObject) throws JSONException {
    String tempTitle, tempLang, tempDesc, tempInfoLink, tempPubDate, tempPublisher, tempMatRate;//at same depth
    int tempPageCount;
    String tempThumbnail; // nested in map {}
    String tempIsbn; // double nested [{}]
    ArrayList<String> tempCategories = new ArrayList<String>();
    ArrayList<String> tempAuthors = new ArrayList<String>();

    tempIsbn = getIsbn(jsonObject).get(ISBN);
    Book.Builder builder = Book.builder().isbn(tempIsbn);
    
    try{

        if (jsonObject.getJSONObject(VOLUME_INFO).has(TITLE)) {
            tempTitle = jsonObject.getJSONObject(VOLUME_INFO).get(TITLE).toString();
            builder.title(tempTitle);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(LANGUAGE)) {
            tempLang = jsonObject.getJSONObject(VOLUME_INFO).get(LANGUAGE).toString();
            builder.language(tempLang);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(DESCRIPTION)) {
            tempDesc = jsonObject.getJSONObject(VOLUME_INFO).get(DESCRIPTION).toString();
            builder.description(tempDesc);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(INFO_LINK)) {
            tempInfoLink = jsonObject.getJSONObject(VOLUME_INFO).get(INFO_LINK).toString();
            builder.infoLink(tempInfoLink);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(PAGE_COUNT)) {
            tempPageCount = Integer.parseInt(jsonObject.getJSONObject(VOLUME_INFO).get(PAGE_COUNT).toString());
            builder.pageCount(tempPageCount);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(PUBLISHED_DATE)) {
            tempPubDate = jsonObject.getJSONObject(VOLUME_INFO).get(PUBLISHED_DATE).toString();
            builder.publishedDate(tempPubDate);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(PUBLISHER)) {
            tempPublisher = jsonObject.getJSONObject(VOLUME_INFO).get(PUBLISHER).toString();
            builder.publisher(tempPublisher);
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(MATURITY_RATING)) {
            tempMatRate = jsonObject.getJSONObject(VOLUME_INFO).get(MATURITY_RATING).toString();
            builder.maturityRating(tempMatRate);                
        }
        
        if (jsonObject.getJSONObject(VOLUME_INFO).has(CATEGORIES)) {
            tempCategories = jsonArrayToStringArray(
                jsonObject.getJSONObject(VOLUME_INFO).getJSONArray(CATEGORIES));
            builder.categories(tempCategories);                
        }

        if (jsonObject.getJSONObject(VOLUME_INFO).has(AUTHORS)) {
            tempAuthors = jsonArrayToStringArray(
                jsonObject.getJSONObject(VOLUME_INFO).getJSONArray(AUTHORS));
            builder.authors(tempAuthors);
        }

        if (jsonObject.getJSONObject(VOLUME_INFO).has(IMAGE_LINKS)
                && jsonObject.getJSONObject(VOLUME_INFO)
                    .getJSONObject(IMAGE_LINKS)
                    .has(THUMBNAIL)) {//if imageLinks and the thumbnail are present
                        tempThumbnail = jsonObject.getJSONObject(VOLUME_INFO)
                                    .getJSONObject(IMAGE_LINKS)
                                    .get(THUMBNAIL)
                                    .toString();
                        builder.thumbnail(tempThumbnail);
                    }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    Book book = builder.build();
    return book;
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

    public Map<String, String> getIsbn(JSONObject item) throws JSONException {
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
        if (tempType != null || tempISBN != null)
          isbnMap.put(tempType, tempISBN);
      }
      return isbnMap;
    }
    return null;
  }

}