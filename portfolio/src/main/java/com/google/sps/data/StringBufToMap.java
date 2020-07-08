package com.google.sps.data;

import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;

public class StringBufToMap{
    public Map<String,String> map;
    private String input;

    public Map<String,String> toMap(StringBuffer sb){
        this.input = sb.toString();
        map = new HashMap<String,String>();
        int quotationCnt = 0;
        int curlyCnt = 0;
        String key = "";
        String value = "";

        int index = 0;
        char character;
        Map newMap;
        //Map newMap = new HashMap<String,String>;
        String sub;
        int incrementor = 1;
        int keyValueBalance = 0;
        int inputLength = input.length();
        boolean isKey = true;

        while(index < inputLength){
            /*System.out.println("");
            System.out.println("Index:" + index);
            System.out.println("Map: " + map);
            System.out.println("isKey: " + isKey);*/

            character = input.charAt(index);
            //System.out.println("Char: " + character);
            incrementor = 1;
            if(key.length() > 0 && value.length() > 0){
                map.put(key,value);
                key = "";
                value = "";
            }
            switch (character) {
                case '{': {
                    //newMap = new Map<String,String>();
                    break;
                }
                case '\"': {
                    //need to add 1 because these operations are inclusive
                    int nextIndex = input.indexOf(character,index+1);
                    //System.out.println("next index: " + nextIndex);
                    if(nextIndex > 0){
                        sub = input.substring(index+1,nextIndex);
                        //System.out.println("sub: "+sub);
                        incrementor = nextIndex - index + 1;
                        if(isKey){
                            key = sub;
                        }else{
                            value = sub;
                        }
                    }
                    break;
                }
                case ':': {
                    isKey = !isKey;
                    break;
                }
                case ',': {
                    isKey = true;
                    break;
                }
                case '[': {
                    //
                    break;
                }
                case ']': {
                    //
                    break;
                }
                case '}': {
                    //
                    break;
                }
                default: {
                    incrementor = 1;
                    break;
                }
            }
            index += incrementor;
        }
        return this.map;
    }
}