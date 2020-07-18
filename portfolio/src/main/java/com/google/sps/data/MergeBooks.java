package com.google.sps.data;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.sps.data.Book;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MergeBooks {

  /**
  * This method merges two books that have the same title
  * and ISBN and returns a new book combined from both of them
  * @param original: the original book
  * @param addition: the book with additional info you 
  * want to add
  * The order of the params does not matter
  */
  public static Book merge(Book original, Book addition){
    validate(original);
    validate(addition);
    String isbn = getIsbn(original,addition);
    //One of the ISBNs will be null so you only need to have one valid isbn
    checkArgument(optional.isbn() != null|| addition.isbn() != null,"One of the books must have a non-null ISBN");
    Preconditions.checkArgument(original.title()==addition.title(),"The books must be of the same title");

    Book.Builder combined_builder = Book.builder();
    combined_builder.title(original.title());
    //Lists and Sets
    combined_builder.categories(combineLists(original.categories(),addition.categories()));
    combined_builder.genre(combineSets(original.genre(),addition.genre()));
    combined_builder.authors(combineLists(original.authors(),addition.authors()));
    ArrayList<String> reviews = combineLists(original.reviews(),addition.reviews());
    for(String each: reviews){
        combined_builder.addReview(each);
    }
    //Strings
    combined_builder.isbn(getNonNullString(original.isbn(),addition.isbn()));
    combined_builder.description(getNonNullString(original.description(),addition.description()));
    combined_builder.infoLink(getNonNullString(original.infoLink(),addition.infoLink()));
    combined_builder.thumbnail(getNonNullString(original.thumbnail(),addition.thumbnail()));
    combined_builder.publishedDate(getNonNullString(original.publishedDate(),addition.publishedDate()));
    combined_builder.publisher(getNonNullString(original.publisher(),addition.publisher()));
    combined_builder.maturityRating(getNonNullString(original.maturityRating(),addition.maturityRating()));
    combined_builder.language(getNonNullString(original.language(),addition.language()));
    //Integers
    combined_builder.pageCount(getNonNullInt(original.pageCount(),addition.pageCount()));
    
    return combined_builder.build();
  }

  /**
  * This method checks whether both objects are null
  */
  private static boolean bothNonNull(Object original, Object addition){
    if(original != null && addition == null){
        return false;
    }
    if(original == null && addition != null){
        return false;
    }
    if(original.equals(addition)){
        return true;
    }
    return false;
  }

  /**
  * This method combines two strings together
  * @param original: the first string
  * @param addition: the second string
  */
  private static String combineStrings(String original, String addition){
    if(bothNonNull(original,addition)){
        return getNonNullString(original,addition);
    }
    return original + addition;
  }

  /**
  * Gets the string that isn't null
  * @param original: first string, the default
  * @param addition: the second string
  */
  private static String getNonNullString(String original, String addition){
    if(original == null && addition != null){
        return addition;
    }
    return original;
  }

  /**
  * Gets the integer that isn't null
  * @param original: first int, the default
  * @param addition: the second int
  */
  private static int getNonNullInt(Integer original, Integer addition){
    if(original == null && addition != null){
        return addition.intValue();
    }
    return original;
  }

  /**
  * Combines two list by adding them to a set
  * @param original: first list
  * @param addition: second list
  */
  private static ArrayList<String> combineLists(ImmutableList<String> original, ImmutableList<String> addition){
    if(bothNonNull(original,addition)){
        return new ArrayList<>(getNonNullList(original,addition));
    }
    Set<String> combined = new LinkedHashSet<>(new ArrayList<>(original));
    combined.addAll(new ArrayList<>(addition));
    return new ArrayList<>(combined);
  }

  /**
  * Gets the list that isn't null
  * @param original: first list, the default
  * @param addition: the second list
  */
  private static ImmutableList<String> getNonNullList(ImmutableList<String> original, ImmutableList<String> addition){
    if(original == null && addition != null){
        return addition;
    }
    return original;
  }

  /**
  * Combines two sets
  * @param original: first set, the default
  * @param addition: the second set
  */
  private static Set<String> combineSets(Set<String> original, Set<String> addition){
    if(original.equals(addition)){
        return original;
    }
    if(original != null && addition == null){
        return original;
    }
    if(original == null && addition != null){
        return addition;
    }
    original.addAll(addition);
    return original;
  }

  /**
  * Validates the book by checking that it has a title and the
  * ISBN is in the correct format
  * @param book: the book you want to validate
  */
  private static void validate(Book book){
    Preconditions.checkNotNull(book,"One of the books was null");
    Preconditions.checkNotNull(book.title(),"The book's title was null");
    Preconditions.checkArgument(!book.title().equals(""),"The book's title was empty");
  }

  private static void validateISBN(String isbn){
    Preconditions.checkNotNull(book.isbn(),"The book's ISBN was null");
    Preconditions.checkArgument(!book.isbn().equals(""),"The book's ISBN was empty");
    Preconditions.checkArgument(book.isbn().matches("[0-9]+"), "The ISBN can only be numeric");
    Preconditions.checkArgument(book.isbn().length() == 13, "The ISBN must be 13 digit's");
  }
}