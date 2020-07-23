import static com.google.common.truth.Truth.assertThat;

import com.google.cloud.language.v1.PartOfSpeech.Tag;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import com.google.sps.data.SentimentAnalyzer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SentimentAnalyzerTest {
  private static final String POSITIVE_SENTENCE =
      "This new book Othello is so awesome; I'm so happy!";
  private static final String NEGATIVE_SENTENCE = "Today, Christmas, is the worst day of my life";
  private static final String TECH_SENTENCE =
      "the Pixelbook display rivals some of the best around, Chromebook or otherwise. It's in the same class as the 227 ppi 13-inch MacBook Pro and the 267 ppi Surface Pro 6. The panel works stunningly for movies and photos, as well as photo editing";
  private static final String FOOD_SENTENCE =
      "The portion was very generous (had me rethinking if I should have ordered the skinny version of the same burger) and the patty was made of farro, brown rice, black beans and mushrooms. The brioche bun tasted fresh and yummy and there was plenty of avocado and arugula to go on. The garlic aioli brought all the ingredients together.";
  private SentimentAnalyzer analyzer;

  @Before
  public void setUp() throws IOException {
    analyzer = new SentimentAnalyzer();
  }

  @Test
  public void testHasPositiveSentiment() throws IOException {
    Assert.assertTrue(analyzer.hasPositiveSentiment(POSITIVE_SENTENCE));
    Assert.assertFalse(analyzer.hasPositiveSentiment(NEGATIVE_SENTENCE));
  }

  @Test
  public void testCategories() throws IOException {
    Assert.assertTrue(
        analyzer.getCategories(TECH_SENTENCE)
            .containsKey("/Computers & Electronics/Computer Hardware/Laptops & Notebooks"));
    Assert.assertTrue(analyzer.getCategories(FOOD_SENTENCE).containsKey("/Food & Drink"));
  }

  @Test
  public void testEntities() throws IOException {
    Assert.assertTrue(analyzer.getEntities(TECH_SENTENCE).contains("Pixelbook"));
    Assert.assertTrue(analyzer.getEntities(TECH_SENTENCE).contains("Chromebook"));
    Assert.assertTrue(analyzer.getEntities(TECH_SENTENCE).contains("panel"));
    Assert.assertFalse(analyzer.getEntities(TECH_SENTENCE).contains("nachos"));
  }

  @Test
  public void testEntitiesWithSentiments() throws IOException {
    Assert.assertTrue(analyzer.getEntitiesWithSentiments(POSITIVE_SENTENCE).get("Othello") > 0);
    Assert.assertTrue(analyzer.getEntitiesWithSentiments(NEGATIVE_SENTENCE).get("Christmas") < 0);
  }

  @Test
  public void testEntitiesWithSaliences() throws IOException {
    Map<String, Float> techSalienceMap = analyzer.getEntitiesWithSaliences(TECH_SENTENCE);
    Assert.assertTrue(techSalienceMap.get("Pixelbook") > techSalienceMap.get("Chromebook"));

    Map<String, Float> foodSalienceMap = analyzer.getEntitiesWithSaliences(FOOD_SENTENCE);
    Assert.assertTrue(foodSalienceMap.get("patty") > foodSalienceMap.get("arugula"));
  }

  @Test
  public void testEntityTypes() throws IOException {
    Map<String, String> techEntityMap = analyzer.getEntitiesWithTypes(TECH_SENTENCE);
    Assert.assertEquals("CONSUMER_GOOD", techEntityMap.get("Surface Pro"));
    Assert.assertEquals("WORK_OF_ART", techEntityMap.get("movies"));

    Map<String, String> othelloEntityMap = analyzer.getEntitiesWithTypes(POSITIVE_SENTENCE);
    Assert.assertEquals("WORK_OF_ART", othelloEntityMap.get("Othello"));
  }
}
