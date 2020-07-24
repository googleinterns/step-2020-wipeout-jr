package com.google.sps.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Class that takes in an InputStream as the constructor parameter
 * and populates a map with the book titles and isbns
 */
public class IsbnReader {
  private final File file;

  public IsbnReader(String path) {
    this.file = new File(path);
  }

  public ImmutableMap<String, String> makeIsbnMap() throws IOException {
    ImmutableMap.Builder<String, String> isbnMapBuilder = new Builder<String, String>();
    try (Scanner scanner = new Scanner(file, "utf-8").useDelimiter("\n")) {
      while (scanner.hasNextLine()) {
        String[] content = scanner.nextLine().split(",");
        String title = content[0];
        String isbn = content[1].replaceAll("\\s+", "");
        if (title != "book_title") {
          isbnMapBuilder.put(title, isbn);
        }
      }
    } catch (Exception ex) {
      throw new IOException("Error reading CSV file", ex);
    }
    ImmutableMap<String, String> isbnMap = isbnMapBuilder.build();
    return isbnMap;
  }
}

