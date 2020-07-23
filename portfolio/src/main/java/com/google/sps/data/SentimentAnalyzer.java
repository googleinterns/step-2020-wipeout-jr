package com.google.sps.data;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Uses the Natural Language API to perform entity and sentiment analysis.
 */
public class SentimentAnalyzer {
  private static LanguageServiceClient languageService;
  private static final String SALIENCE_PROPERTY = "SALIENCE";
  private static final String SENTIMENT_PROPERTY = "SENTIMENT";

  /**
   * Constructor that instantiates the LanguageServiceClient
   */
  public SentimentAnalyzer() throws IOException {
    languageService = LanguageServiceClient.create();
  }

  /**
   * Checks if the given text has a positive sentiment
   *
   * @param text: Text to be analyzed
   */
  public static boolean hasPositiveSentiment(String text) throws IOException {
    return getSentimentScore(text) > 0;
  }

  /**
   * Gets sentiment score (between -1 and 1) for given text
   *
   * @param text: Text to be analyzed
   * @return float score, where -1 is the negative extreme and +1 is the positive extreme
   */
  public static float getSentimentScore(String text) throws IOException {
    Document doc = makeDocument(text);
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    return score;
  }

  /**
   * Gets categories and their associated confidences for given text
   * Complete list of categories: cloud.google.com/natural-language/docs/categories
   *
   * @param text: Text to be classified
   * @return ImmutableMap from categories (as Strings) to confidences (as floats)
   */
  public ImmutableMap<String, Float> getCategories(String text) {
    ImmutableMap.Builder<String, Float> strCategories = new ImmutableMap.Builder<String, Float>();
    Document doc = makeDocument(text);
    List<ClassificationCategory> categories = languageService.classifyText(doc).getCategoriesList();
    for (ClassificationCategory category : categories) {
      strCategories.put(category.getName(), category.getConfidence());
    }
    return strCategories.build();
  }

  /**
   * Gets an immutableSet of entities in given text
   *
   * @param text: Text to be analyzed
   * @return ImmutableSet of entities found, represented as Strings
   */
  public ImmutableSet<String> getEntities(String text) {
    return getEntitiesWithSentiments(text).keySet();
  }

  /**
   * Gets an immutableMap of entities in given text
   * along with associated sentiment values.
   *
   * @param text: Text to be analyzed
   * @return ImmutableMap from entities (as Strings) to sentiment values (as floats)
   */
  public ImmutableMap<String, Float> getEntitiesWithSentiments(String text) {
    return getEntitiesWith(text, SENTIMENT_PROPERTY);
  }

  /**
   * Gets an immutableMap of entities in given text
   * along with associated salience values (importance to text)
   *
   * @param text: Text to be analyzed
   * @return ImmutableMap from entities (as Strings) to salience values (as floats)
   */
  public ImmutableMap<String, Float> getEntitiesWithSaliences(String text) {
    return getEntitiesWith(text, SALIENCE_PROPERTY);
  }

  /**
   * Gets an immutableMap of entities in given text
   * along with the type of the entity.
   * Complete list of types: cloud.google.com/natural-language/docs/reference/rest/v1/Entity#Type
   *
   * @param text: Text to be classified
   * @return ImmutableMap from entities (as Strings) to Type values (as Strings)
   */
  public ImmutableMap<String, String> getEntitiesWithTypes(String text) {
    ImmutableMap.Builder<String, String> entitiesWithTypes =
        new ImmutableMap.Builder<String, String>();
    Document doc = makeDocument(text);
    List<Entity> entities = languageService.analyzeEntitySentiment(doc).getEntitiesList();

    for (Entity entity : entities) {
      entitiesWithTypes.put(entity.getName(), entity.getType().toString());
    }

    return entitiesWithTypes.build();
  }

  /**
   * Returns Map of entities in given text and specified property
   *
   * @param text: Text to be analyzed
   * @param valueProperty: value (Sentiment or Salience) to be returned
   * @return ImmutableMap from entities (as Strings) to specified property (as float)
   */
  private ImmutableMap<String, Float> getEntitiesWith(String text, String valueProperty) {
    ImmutableMap.Builder<String, Float> entitiesWithProperty =
        new ImmutableMap.Builder<String, Float>();
    Document doc = makeDocument(text);
    List<Entity> entities = languageService.analyzeEntitySentiment(doc).getEntitiesList();

    if (valueProperty.equals(SENTIMENT_PROPERTY)) {
      for (Entity entity : entities) {
        entitiesWithProperty.put(entity.getName(), entity.getSentiment().getScore());
      }
    } else {
      for (Entity entity : entities) {
        entitiesWithProperty.put(entity.getName(), entity.getSalience());
      }
    }
    return entitiesWithProperty.build();
  }

  /**
   * Creates a Document object from given text
   *
   * @param text: Text in string format
   * @return Document object representing the text
   */
  private static Document makeDocument(String text) {
    return Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
  }
}