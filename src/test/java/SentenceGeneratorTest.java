import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class SentenceGeneratorTest {

    private JSONFileParser testParser;
    private Grammar testGrammar;
    private SentenceGenerator testSentence1;
    private SentenceGenerator testSentence2;
    private static final String testStringOutput = "hi Sally Jones";
    private static final Long RANDOM_SEED = 42L;
    private static final String sampleJSONtest = "Grammar{grammarTitle='A sample grammar', info={lastName=[Smith, Jones], start=[hi <name>], name=[Sally <lastName>]}}";

    @Before
    public void setUp() throws Exception {
        this.testParser = new JSONFileParser("./json/sample.json");
        this.testGrammar = new Grammar(this.testParser);
        this.testSentence1 = new SentenceGenerator(this.testGrammar, RANDOM_SEED);
        this.testSentence2 = new SentenceGenerator(this.testGrammar, RANDOM_SEED);
    }

    @Test
    public void buildSentence() throws NoSuchGrammarTypeException {
        String buildSentenceCalled = this.testSentence1.buildSentence();
        Assert.assertEquals(testStringOutput, buildSentenceCalled);
    }

    @Test
    public void buildSentenceNoSeed() throws NoSuchGrammarTypeException {
        SentenceGenerator testSentenceGenerator = new SentenceGenerator(this.testGrammar, null);
        String testSentence = testSentenceGenerator.buildSentence();
    }

    @Test (expected = NoSuchGrammarTypeException.class)
    public void nullGrammarHandle() throws NoSuchGrammarTypeException, IOException, ParseException {
        JSONFileParser parser = new JSONFileParser("./json/sample_fail.json");
        Grammar grammar = new Grammar(parser);
        SentenceGenerator generator = new SentenceGenerator(grammar, null);
        generator.buildSentence();
    }

    @Test
    public void testToString() throws NoSuchGrammarTypeException {
        String testSentenceOut = this.testSentence1.buildSentence();
        String fullToStringTest = "SentenceGenerator{" + "sentence='" + testSentenceOut + '\'' + ", " + sampleJSONtest + "}";
        Assert.assertEquals(fullToStringTest, this.testSentence1.toString());
    }

    @Test
    public void testEquals() {
        Assert.assertEquals(this.testSentence1, this.testSentence2);
    }

    @Test
    public void testNullEquals() {
        Assert.assertNotEquals(null, this.testSentence1);
    }

    @Test
    public void testHashCode() {
        Assert.assertEquals(this.testSentence1.hashCode(), this.testSentence2.hashCode());
    }

}