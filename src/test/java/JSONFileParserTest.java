import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JSONFileParserTest {

  //We will be using the sample.json and poem_grammar.json files in this project to perform
  //testing for the Grammar class.

  //Will be created using Constructor 1
  private JSONFileParser sample;

  //Will be created using Constructor 2
  private JSONFileParser poem;

  //Will attempt to be created using a nonexistent file to check for errors
  private JSONFileParser fake;

  @Before
  public void setUp() throws IOException, ParseException {
    String splityBy = "(?<=\")[^,;](.*?)(?=\")";
    this.sample = new JSONFileParser("./json/sample.json");
    this.poem = new JSONFileParser("./json/poem_grammar.json", splityBy);
  }

  @Test(expected = FileNotFoundException.class)
  public void fakeConstructor() throws FileNotFoundException {
    this.fake = new JSONFileParser("./json/nonexistent_file.json");
  }

  @Test
  public void getFileNameTest()  {
    Assert.assertEquals("./json/sample.json", this.sample.getFileName());
  }

  @Test
  public void getTitleTest() throws IOException, ParseException {
    this.sample.readFile();
    Assert.assertEquals("A sample grammar", this.sample.getTitle());
    Assert.assertEquals("", this.poem.getTitle());
  }

  @Test
  public void getDescriptionTest() throws IOException, ParseException {
    this.sample.readFile();
    Assert.assertEquals("A grammar that generates sample grammars.", this.sample.getDesc());
    Assert.assertEquals("", this.poem.getDesc());
  }

  @Test
  public void readFileTest() throws IOException, ParseException {
    HashMap<String, ArrayList<String>> sampleInfo = sample.readFile();

    //Make sure the size is 3, and then all 3 keys are contained in the HashMap
    Assert.assertEquals(3, sampleInfo.size());
    Assert.assertTrue(sampleInfo.containsKey("start"));
    Assert.assertTrue(sampleInfo.containsKey("name"));
    Assert.assertTrue(sampleInfo.containsKey("lastName"));

    //Get each of the lists (values) associated with each key
    List<String> start = sampleInfo.get("start");
    List<String> name = sampleInfo.get("name");
    List<String> lastName = sampleInfo.get("lastName");

    //Make sure each of the lists is the right (expected) length and then check values

    //'start' arraylist
    Assert.assertEquals(1, start.size());
    Assert.assertEquals("hi <name>", start.get(0));

    //'name' arraylist
    Assert.assertEquals(1, name.size());
    Assert.assertEquals("Sally <lastName>", name.get(0));

    //'lastName' arraylist
    Assert.assertEquals(2, lastName.size());
    Assert.assertEquals("Smith", lastName.get(0));
    Assert.assertEquals("Jones", lastName.get(1));
  }

  @Test
  public void toStringTest() {
    String sampleString = "JSONFileParser{jsonFile='./json/sample.json'}";
    Assert.assertEquals(sampleString, this.sample.toString());
  }

  @Test
  public void equalsTest() throws FileNotFoundException {
    JSONFileParser sampleCopy = new JSONFileParser("./json/sample.json");
    Assert.assertTrue(sampleCopy.equals(this.sample));
  }

  @Test
  public void notEqualsTest() {
    Assert.assertFalse(this.sample.equals(this.poem));
  }

  @Test
  public void notEqualObjectTest() {
    String test = "hello";
    Assert.assertFalse(this.poem.equals(test));
  }

  @Test
  public void hashcodeTest() throws FileNotFoundException {
    JSONFileParser sampleCopy = new JSONFileParser("./json/sample.json");
    Assert.assertEquals(this.sample.hashCode(), sampleCopy.hashCode());
  }


}