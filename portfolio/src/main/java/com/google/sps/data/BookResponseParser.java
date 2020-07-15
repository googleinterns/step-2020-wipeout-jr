package com.google.sps.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.sps.data.Book;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Given a json String from Google Book Api, converts the response into a in-memory Book
// representation.
public class BookResponseParser {
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
  private static final String INDUSTRTRY_IDS = "industryIdentifiers";

  public static Book parseBook(String jsonResponse) throws JSONException{
    try{
        if(validate(jsonResponse)){
            JSONObject jsonObject = new JSONObject(jsonResponse);
            return jsonToBook(jsonObject);
        }
        return null;
    }catch(JSONException e){
        return null;
    }
  }

  private static Book jsonToBook(JSONObject jsonObject) throws JSONException {
    Book.Builder builder = Book.builder();
    JSONObject volumeInfo = jsonObject.getJSONObject(VOLUME_INFO);

    try {
      String isbn = getIsbn(volumeInfo).get(ISBN);// double nested [{}]
      if (isbn == null) {
        //should never be the case, all books should have ISBN-13
        return null;
      }
      builder.isbn(isbn);

      if (volumeInfo.has(TITLE)) {
        String title = volumeInfo.get(TITLE).toString();
        builder.title(title);
      }

      if (volumeInfo.has(LANGUAGE)) {
        String lang = volumeInfo.get(LANGUAGE).toString();
        builder.language(lang);
      }

      if (volumeInfo.has(DESCRIPTION)) {
        String desc = volumeInfo.get(DESCRIPTION).toString();
        builder.description(desc);
      }

      if (volumeInfo.has(INFO_LINK)) {
        String infoLink = volumeInfo.get(INFO_LINK).toString();
        builder.infoLink(infoLink);
      }

      if (volumeInfo.has(PAGE_COUNT)) {
        int pageCount =
            Integer.parseInt(volumeInfo.get(PAGE_COUNT).toString());
        builder.pageCount(pageCount);
      }

      if (volumeInfo.has(PUBLISHED_DATE)) {
        String pubDate = volumeInfo.get(PUBLISHED_DATE).toString();
        builder.publishedDate(pubDate);
      }

      if (volumeInfo.has(PUBLISHER)) {
        String publisher = volumeInfo.get(PUBLISHER).toString();
        builder.publisher(publisher);
      }

      if (volumeInfo.has(MATURITY_RATING)) {
        String matRate = volumeInfo.get(MATURITY_RATING).toString();
        builder.maturityRating(matRate);
      }

      if (volumeInfo.has(CATEGORIES)) {
        ArrayList<String> categories =
            jsonArrayToStringArray(volumeInfo.getJSONArray(CATEGORIES));
        builder.categories(categories);
      }

      if (volumeInfo.has(AUTHORS)) {
        ArrayList<String> authors =
            jsonArrayToStringArray(volumeInfo.getJSONArray(AUTHORS));
        builder.authors(authors);
      }

      // nested in map {}
      if (volumeInfo.has(IMAGE_LINKS)
          && volumeInfo
                 .getJSONObject(IMAGE_LINKS)
                 .has(THUMBNAIL)) { // if imageLinks and the thumbnail are present
        String thumbnail = volumeInfo
                            .getJSONObject(IMAGE_LINKS)
                            .get(THUMBNAIL)
                            .toString();
        builder.thumbnail(thumbnail);
      }
    } catch (JSONException e) {
        throw new JSONException("There was an error when building the book from the json string",e);
    }

    Book book = builder.build();
    return book;
  }

  private static ArrayList<String> jsonArrayToStringArray(JSONArray jsArray) {
    ArrayList<String> strArray = new ArrayList<String>();

    for (int j = 0; j < jsArray.length(); j++) {
      try {
        strArray.add(jsArray.get(j).toString());

      } catch (JSONException e) {
        throw new JSONException("There was an error converting jsonArray to ArrayList<String>",e);
      }
    }
    return strArray;
  }

  private static Map<String, String> getIsbn(JSONObject volumeInfo) throws JSONException {
    Gson gson = new Gson();

    String type = "";
    String isbn = "";

    Map<String, String> isbnMap = new HashMap<String, String>();
    if (volumeInfo.has(INDUSTRTRY_IDS)) {
      JSONArray arrayOfIsbns = volumeInfo.getJSONArray(INDUSTRTRY_IDS);
      Type listType = new TypeToken<List<HashMap<String, String>>>() {}.getType();
      List<HashMap<String, String>> isbns = gson.fromJson(arrayOfIsbns.toString(), listType);

      for (HashMap<String, String> map : isbns) {
        isbn = map.get("identifier");
        type = map.get("type");
        if(isbn != null && type != null){
            isbnMap.put(type,isbn);
        }
      }
      return isbnMap;
    }
    return null;
  }

  private static boolean validate(String jsonResponse){
      //checks to see if the input is an validate value
      //empty string
      if(jsonResponse == null || jsonResponse.equals("")){
        throw new JSONException("The response was either null or empty.");
      }
      JSONObject jsonObject = new JSONObject(jsonResponse);
      //if full api response (contains multiple items)
      if(jsonObject.has("items") || !jsonObject.has(VOLUME_INFO)){
        throw new JSONException("The response was not in the required format.");
      }
      return true;
  }
}

