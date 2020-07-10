//  RequestJson.java
//  Sample Google API
//
//  Created by Mohammadreza on 10/7/18.
//  Copyright © 2019 Mohammadreza Mohades. All rights reserved.


package com.google.sps.data;

//import Models.FullBook;
import com.google.sps.data.BookFieldsEnum;
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
import java.lang.IllegalAccessException;
import java.lang.NoSuchFieldException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class RequestJson {
    static int count2 = 0;

    public RequestJson(){}

    public JSONObject requestQuery(String query) throws Exception {
        //This method calls the actual Book API from the URL and returns a JSON Object from the response 
        System.out.println("Query string:"+query +" --Request Json - rq");//REMOVE
        //Gson gson = new Gson();
        //convert space to url format

        String url = String.format("https://www.googleapis.com/books/v1/volumes?q=%s", query);
        //this link contains different data than the book's selfLink
        System.out.println("Formatted url: "+url +" --Request Json - rq");//REMOVE
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url+" --Request Json - rq");//REMOVE
        System.out.println("Response Code : " + responseCode+" --Request Json - rq");//REMOVE
        System.out.print("Response went through: ");//REMOVE
        System.out.println(responseCode == 200);//REMOVE

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();//will contain the large string of information
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //System.out.println("Response string: "+response.toString()+" --Request Json - rq");
        JSONObject myResponse = new JSONObject(response.toString());
        //System.out.println("JSON Object: "+myResponse+" --Request Json - rq");

        return myResponse;


    }


    public ArrayList<FullBook> call_me(String query) throws Exception {

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

        JSONObject myResponse = new JSONObject(requestQuery(query).toString());//request Query returns a JSON object of the JSON string with all the data
        //System.out.println("my response: "+myResponse+" --Request Json - cm");//all the data
        JSONArray allItems = myResponse.getJSONArray("items");
        //System.out.println("all items: "+allItems+" --Request Json - cm");//all the data

        //allItems.length()
        for (int i = 0; i < 3; i++) {
            System.out.println("\ngetJSONObject "+i+": "+allItems.getJSONObject(i)+" --Request Json - cm");

            returnedList.add(jsonToFullBook(allItems.getJSONObject(i)));//jsonToFullBook
            //Image tempImage = getSmallImage(allItems.getJSONObject(i));

            //returnedList.get(i).setIcon(tempImage);
            //returnedList.get(i).getPublisherInstance().setID(i);
            returnedList.get(i).setId(i + 1);


        }
        System.out.println("Printing returnedList: "+" --Request Json - cm");
        for(FullBook each: returnedList){
            System.out.println("\t"+each);
        }
        return returnedList;
    }


    public FullBook jsonToFullBook(JSONObject jsonObject) throws JSONException {
        //changed to non-static method
        //Gson gson = new Gson();
        /*String tempPubTitle = "", publishedDate = "", tempDesc = "", tempTitle = "";
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
        if (jsonObject.has("description"))//not working
            tempDesc = jsonObject.getJSONObject("description").toString();

        String tempCategories = "";//mine
        if (jsonObject.getJSONObject("volumeInfo").has("categories"))
            tempCategories = jsonObject.getJSONObject("volumeInfo").getJSONArray("categories").getString(0);*/

        ArrayList<String> fields = new ArrayList<String>();
        FullBook newBook = new FullBook();
        for (BookFieldsEnum field : BookFieldsEnum.values()) {
            String jsProperty = field.getJSProperty();
            String tempProperty = "Undefined";
            List<String> tempPropertyArray = new ArrayList<String>();
            boolean isArray = false;
            if(!jsProperty.equals("genre")){
            try{
                if(jsProperty.equals("categories") || jsProperty.equals("authors")){
                    //seperate because they exists as arrays in the JSON object and genre has a different name ("categories")
                    isArray = true;
                    if (jsonObject.getJSONObject("volumeInfo").has(jsProperty)){
                        tempPropertyArray = jsonArrayToStringArray(jsonObject.getJSONObject("volumeInfo").getJSONArray(jsProperty));
                    }else{
                        tempPropertyArray = null;
                    }
                }else{
                    System.out.println(jsonObject.getJSONObject("volumeInfo").has(jsProperty) == true);
                    if (jsonObject.getJSONObject("volumeInfo").has(jsProperty)){
                        tempProperty = jsonObject.getJSONObject("volumeInfo").get(jsProperty).toString();
                    }
                }
            }
            catch (JSONException e) {
                    e.printStackTrace();
                }
            //Field.set(FullBook.getClass().getDeclaredField(jsProperty),tempProperty);
            System.out.println("Field: "+jsProperty);
            System.out.println("Array Property: "+tempPropertyArray);
            System.out.println("Property: "+tempProperty);
            try{
                Field fieldInBook = newBook.getClass().getDeclaredField(jsProperty);
                if(isArray){
                    fieldInBook.set(newBook,tempPropertyArray);
                }else{
                    fieldInBook.set(newBook,tempProperty);
                }
            }
            catch(NoSuchFieldException e){
                e.printStackTrace();
            }
            catch(IllegalAccessException e){
                e.printStackTrace();
            }
            }
        }

        /*FullBook returnedFullBook = new FullBook(tempTitle, (ArrayList<String>) tempAuthors, tempISBN, tempDesc, 0);
        returnedFullBook.setCategories(tempCategories);
        Publisher tempPublisher = new Publisher();
        tempPublisher.setName(tempPubTitle);
        returnedFullBook.setPublisherInstance(tempPublisher);
        returnedFullBook.setDatePublished(publishedDate);
        */
        return newBook;
        //return returnedFullBook;
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

    public Map<String, String> getIsbn(JSONObject item) throws JSONException {
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