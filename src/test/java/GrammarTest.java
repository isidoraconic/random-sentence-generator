import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GrammarTest {

  //We will be using the sample.json and poem_grammar.json files in this project to perform
  //testing for the Grammar class.

  //Will be created using Constructor 1
  private Grammar sample;

  //Will be created using Constructor 2
  private Grammar poem;

  //Will attempt to be created using a nonexistent file to check for errors
  private Grammar fake;

  @Before
  public void setUp() throws IOException, ParseException {
    JSONFileParser sampleParser = new JSONFileParser("./json/sample.json");
    this.sample = new Grammar(sampleParser);
    this.poem = new Grammar("./json/poem_grammar.json");
  }

  @Test (expected = FileNotFoundException.class)
  public void failConstructorTest() throws IOException, ParseException {
    this.fake = new Grammar("./json/nonexistent_file.json");
  }

  @Test
  public void getTitleTest() {
    Assert.assertEquals("A sample grammar", this.sample.getGrammarTitle());
    Assert.assertEquals("Poem Generator", this.poem.getGrammarTitle());
  }

  @Test
  public void getDescriptionTest() {
    Assert.assertEquals("A grammar that generates sample grammars.", sample.getGrammarDesc());
    Assert.assertEquals("A grammar that generates poems. ", poem.getGrammarDesc());
  }

  @Test
  public void getInfoTest() {
    HashMap<String, ArrayList<String>> sampleInfo = sample.getInfo();

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
  public void getInfoValueTest() {
    List<String> name = this.sample.getInfoValue("name");
    Assert.assertEquals(1, name.size());
    Assert.assertEquals("Sally <lastName>", name.get(0));
  }

  @Test
  public void toStringTest() {
    String sampleString = this.sample.toString();
    Assert.assertEquals("Grammar{grammarTitle='A sample grammar', "
        + "info={lastName=[Smith, Jones], start=[hi <name>], name=[Sally <lastName>]}}",
        sampleString);
  }

  @Test
  public void equalsTest() throws IOException, ParseException {
    Grammar sampleCopy = new Grammar("./json/sample.json");
    Assert.assertTrue(sampleCopy.equals(this.sample));
  }

  @Test
  public void notEqualTest() throws IOException, ParseException {
    Assert.assertFalse(this.poem.equals(this.sample));
  }

  @Test
  public void notEqualObjects() {
    String test = "hello";
    Assert.assertFalse(this.poem.equals(test));
  }

  @Test
  public void hashcodeTest() throws IOException, ParseException {
    Grammar sampleCopy = new Grammar("./json/sample.json");
    int sampleHashcode = this.sample.hashCode();
    int sampleCopyHashcode = sampleCopy.hashCode();
    Assert.assertEquals(sampleHashcode, sampleCopyHashcode);
  }


}
