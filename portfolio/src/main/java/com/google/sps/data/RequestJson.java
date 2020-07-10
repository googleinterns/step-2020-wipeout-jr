//  RequestJson.java
//  Sample Google API
//
//  Created by Mohammadreza on 10/7/18.
//  Copyright © 2019 Mohammadreza Mohades. All rights reserved.


package com.google.sps.data;

//import Models.FullBook;
import com.google.sps.data.FullBook;
import com.google.sps.data.Publisher;
//import Models.Publisher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class RequestJson {
    static int count2 = 0;

    public static JSONObject requestQuery(String query) throws Exception {
        //Gson gson = new Gson();
        //convert space to url format

        String url = String.format("https://www.googleapis.com/FullBooks/v1/volumes?q=%s", query);
        System.out.println("Formatted url: "+url);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject myResponse = new JSONObject(response.toString());

        return myResponse;


    }


    public static ArrayList<FullBook> call_me(String query) throws Exception {

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

        JSONObject myResponse = new JSONObject(requestQuery(query).toString());//request Query
        JSONArray allItems = myResponse.getJSONArray("items");

        for (int i = 0; i < allItems.length(); i++) {

            returnedList.add(jsonToFullBook(allItems.getJSONObject(i)));//jsonToFullBook
            //Image tempImage = getSmallImage(allItems.getJSONObject(i));

            //returnedList.get(i).setIcon(tempImage);
            returnedList.get(i).getPublisherInstance().setID(i);
            returnedList.get(i).setId(i + 1);


        }
        return returnedList;
    }


    public static FullBook jsonToFullBook(JSONObject jsonObject) throws JSONException {
        Gson gson = new Gson();
        String tempPubTitle = "", publishedDate = "", tempDesc = "", tempTitle = "";
        Map<String, String> tempISBN = new HashMap<String, String>();
        List<String> tempAuthors = new ArrayList<String>();


        tempTitle = jsonObject.getJSONObject("volumeInfo").get("title").toString();
        try {
            if(jsonObject.getJSONObject("volumeInfo").has("authors"))
            tempAuthors = jsonArrayToStringArray(jsonObject.getJSONObject("volumeInfo").
                    getJSONArray("authors"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tempISBN = getIsbn(jsonObject);

//            Publisher
        try {
            if (jsonObject.getJSONObject("volumeInfo").has("publisher"))
                tempPubTitle = jsonObject.getJSONObject("volumeInfo").get("publisher").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject.getJSONObject("volumeInfo").has("publishedDate"))
            publishedDate = jsonObject.getJSONObject("volumeInfo").get("publishedDate").toString();
        if (jsonObject.has("description"))
            tempDesc = jsonObject.getJSONObject("description").toString();

        FullBook returnedFullBook = new FullBook(tempTitle, (ArrayList<String>) tempAuthors, tempISBN, tempDesc, 0);
        Publisher tempPublisher = new Publisher();
        tempPublisher.setName(tempPubTitle);
        returnedFullBook.setPublisherInstance(tempPublisher);
        returnedFullBook.setDatePublished(publishedDate);

        return returnedFullBook;

    }

    /*public static Image getSmallImage(JSONObject jsonObject) {
        Image tempImage = null;
        count2++;

        try {

            if (jsonObject.getJSONObject("volumeInfo").has("imageLinks"))
                if (jsonObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").has("smallThumbnail")) {

                    try {
                        URL url = new URL(jsonObject.getJSONObject("volumeInfo").getJSONObject("imageLinks").get("smallThumbnail").toString());
                        tempImage = new Image(url.toString());

                    } catch (IOException e) {
                    }
                    return tempImage;
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    public static Map<String, String> getIsbn(JSONObject item) throws JSONException {
        Gson gson = new Gson();

        String tempType = "", tempISBN = "";
        Map<String, String> isbnMap = new HashMap<String, String>();
        int counter = 0;
        if(item.getJSONObject("volumeInfo").has("industryIdentifiers")){
            JSONArray tempArr = item.getJSONObject("volumeInfo").getJSONArray("industryIdentifiers");
            Type listType = new TypeToken<List<HashMap<String, String>>>() {
            }.getType();
            List<HashMap<String, String>> isbns =
                    gson.fromJson(tempArr.toString(), listType);

            for (HashMap<String, String> map : isbns) {
                counter = 0;
                for (HashMap.Entry<String, String> entry : map.entrySet()) {
                    if (counter == 0) {
                        tempISBN = entry.getValue(); //Key is identifier
                    }
                    if (counter == 1) {
                        tempType = entry.getValue();
                        //Key is type
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

    public static void printMap(Map<String, String> map) {

        for (HashMap.Entry<String, String> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
        }
    }

    public static ArrayList<String> jsonArrayToStringArray(JSONArray jsArray) {
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


    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
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

    public static List<Object> toList(JSONArray array) throws JSONException {
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