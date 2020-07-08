package com.google.sps.data;

import java.io.BufferedReader;
import java.lang.RuntimeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringBufToMap {
  public Map<String, Object> outputMap;
  private String inputString;

  public Map<String, Object> toMap(StringBuffer sb) {
    this.inputString = sb.toString();
    this.outputMap = getMap(inputString);
    return outputMap;
  }

  private Map<String, Object> getMap(String parent) {
    String key = "";
    Object value = null;
    int index = 0;
    char character;
    Map newMap = new HashMap<String, Object>();
    int incrementor = 1;
    int parentLength = parent.length();
    boolean isKey = true;

    while (index < parentLength) {
      character = parent.charAt(index);

      incrementor = 1;
      switch (character) {
        case '{': {
          // either the start of the map or a new sub map
          if (index != 0) {
            value = getMap(getEnd(index + 1, character, parent));

          } else {
            newMap = getMap(getEnd(index + 1, character, parent));
          }
          incrementor = getIncriment(index, character, parent);
          break;
        }
        case '[': {
          // a new sublist
          value = getList(getEnd(index + 1, character, parent));
          incrementor = getIncriment(index, character, parent);
          break;
        }
        case '\"': {
          // get the entire field (e.g. "title", "kind")
          // need to add 1 because these operations are inclusive
          int nextIndex = parent.indexOf(character, index + 1);
          if (nextIndex > 0) {
            String sub = parent.substring(index + 1, nextIndex);
            incrementor = nextIndex - index + 1;
            if (isKey) {
              key = sub;
            } else {
              value = sub;
            }
          }
          break;
        }
        case ':': {
          isKey = false;
          break;
        }
        case ',': {
          isKey = true;
          break;
        }
        default: {
          incrementor = 1;
          break;
        }
      }
      index += incrementor;
      if (key.length() > 0 && value != null) {
        newMap.put(key, value);
        key = "";
        value = null;
        isKey = true;
      }
    }
    return newMap;
  }

  private ArrayList<Object> getList(String parent) {
    Object value = null;
    int index = 0;
    char character;
    ArrayList<Object> newList = new ArrayList<Object>();
    int incrementor = 1;
    int parentLength = parent.length();

    while (index < parentLength) {
      character = parent.charAt(index);
      incrementor = 1;
      switch (character) {
        case '{': {
          value = getMap(getEnd(index + 1, character, parent));
          incrementor = getIncriment(index, character, parent);
          break;
        }
        case '[': {
          value = getList(getEnd(index + 1, character, parent));
          incrementor = getIncriment(index, character, parent);
          break;
        }
        case '\"': {
          int nextIndex = parent.indexOf(character, index + 1);
          if (nextIndex > 0) {
            value = parent.substring(index + 1, nextIndex);
            incrementor = nextIndex - index + 1;
          }
          break;
        }
        default: {
          incrementor = 1;
          break;
        }
      }
      index += incrementor;
      if (value != null) {
        newList.add(value);
        value = null;
      }
    }
    return newList;
  }

  private int getIncriment(int index, char character, String parent) {
    int counterPartIndex = indexOfOpp(index + 1, character, parent);
    if (counterPartIndex > index) {
      return counterPartIndex - index;
    }
    return 1;
  }

  private String getEnd(int start, char character, String parent) {
    return parent.substring(start, indexOfOpp(start, character, parent));
  }

  private int indexOfOpp(int start, char character, String parent) {
    int strLength = parent.length();
    int index = start;
    int balance = 1;
    char currentChar;
    char opposite = opposite(character);

    while (index < strLength) {
      if (balance < 0 || balance > strLength) {
        throw new RuntimeException("Balance thrown to unreasonable levels");
      }
      currentChar = parent.charAt(index);
      if (currentChar == character) {
        balance++;
      } else if (currentChar == opposite) {
        balance--;
      }
      if (balance == 0 && currentChar == opposite) {
        return index;
      }
      index++;
    }
    throw new RuntimeException("Could not find the end of the map/array.");
  }

  private char opposite(char c) {
    if (c == '{') {
      return '}';
    } else if (c == '}') {
      return '{';
    } else if (c == '[') {
      return ']';
    } else if (c == ']') {
      return '[';
    } else {
      throw new RuntimeException("Function can only accept {}[] values");
    }
  }
}

