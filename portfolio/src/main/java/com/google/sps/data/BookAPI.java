//  BookAPI.java
//  Sample Google API
//
//  Created by Mohammadreza on 10/7/18.
//  Copyright © 2019 Mohammadreza Mohades. All rights reserved.


package com.google.sps.data;

import com.google.sps.data.FullBook;
import com.google.sps.data.RequestJson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BookAPI {


    public ArrayList<FullBook> search(String query) {
        System.out.println("Query string:"+query +" --BookAPI");//REMOVE

        if (query.equals(""))
            return null;
        String encodedUrl = null;
        try {
            encodedUrl = URLEncoder.encode(query, "UTF-8");
            System.out.println("encodedUrl:"+encodedUrl +" --BookAPI");//REMOVE
        } catch (UnsupportedEncodingException ignored) {

        }

        try {
            RequestJson RequestJson = new RequestJson();
            return RequestJson.call_me(encodedUrl);

        } catch (Exception e) {
            System.out.println("Stacktrace error!");
            e.printStackTrace();
        }
        return null;
    }



}