import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class UserInterfaceTest {

    private UserInterface testUI;
    private Grammar testGrammar;
    private SecurityManager testSecurity;
    private static final String jsonPath = "./json/";

    @Before
    public void setUp() throws Exception {
        this.testUI = new UserInterface();
        this.testGrammar = new Grammar(jsonPath + "sample.json");

        // everything below was trying to override the native SecurityManager to catch System.exit(0)
//        this.testSecurity = System.getSecurityManager();
//        System.setSecurityManager(new SecurityManager() {
//            @Override
//            public void checkExit(int status) {
//                super.checkExit(status); // This is IMPORTANT!
//                throw new SecurityException("Overriding shutdown...");
//            }
//        });

    }

    @Test
    public void getSetDirectory_ValidPath() throws NoSuchDirectoryException {
        this.testUI.setDirectory(jsonPath);
        Assert.assertEquals(jsonPath, this.testUI.getDirectory());
    }

    @Test (expected = NoSuchDirectoryException.class)
    public void setDirectory_InvalidPath() throws NoSuchDirectoryException  {
        this.testUI.setDirectory("C:/tests/and/then/some/");
    }

    @Test
    public void setRunMode() {
        this.testUI.setRunMode("q");
    }

    @Test
    public void addGrammar() {
        this.testUI.addGrammar(this.testGrammar);
    }

    @Test
    public void menuCommand() {
        // not sure how to build tests for this without refactoring how user input is passed
    }

    @Test (expected = NumberFormatException.class)
    public void checkInputFalse() throws IOException, NoSuchGrammarTypeException, NumberFormatException {
        this.testUI.handleInput("a");
    }

    @Test
    public void handleInputSingleCycle() throws NoSuchDirectoryException, IOException, NoSuchGrammarTypeException {
        this.testUI.setDirectory(jsonPath);
        this.testUI.addGrammar(this.testGrammar);
        this.testUI.setRunMode("q");
        this.testUI.handleInput("1");
    }


    @Test (expected = IndexOutOfBoundsException.class)
    public void handleInputIndexOutOfBounds() throws IOException, NoSuchGrammarTypeException, NoSuchDirectoryException {
        this.testUI.setDirectory(jsonPath);
        this.testUI.handleInput("1");
    }

    @Test
    public void testToString() {
        String testToString = "UserInterface{" +
                "directory='" + this.testUI.getDirectory() + '\'' +
                '}';
        String toStringCalled = this.testUI.toString();
        Assert.assertEquals(testToString, toStringCalled);
    }

    @Test
    public void testEquals() {
        UserInterface localUI = new UserInterface();
        Assert.assertEquals(localUI, this.testUI);
    }

    @Test
    public void testEquals2() {
        UserInterface localUI = new UserInterface();
        Assert.assertTrue(this.testUI.equals(localUI));
    }

    @Test
    public void testNotEquals() throws NoSuchDirectoryException {
        UserInterface localUI = new UserInterface();
        localUI.setDirectory("./");
        Assert.assertNotEquals(localUI, this.testUI);
    }

    @Test
    public void testHashCode() {
        UserInterface localUI = new UserInterface();
        Assert.assertEquals(localUI.hashCode(), this.testUI.hashCode());
    }

    @Test
    public void mainImmediateQuit() throws NoSuchDirectoryException, NoSuchGrammarTypeException, ParseException, IOException {
        String[] args = {"./json/", "q"};
        UserInterface.main(args);
    }

    // everything below was trying to override the native SecurityManager to catch System.exit(0)
//    @Test (expected = SecurityException.class)
//    public void mainNoArgsPassed() throws ArrayIndexOutOfBoundsException, NoSuchDirectoryException, NoSuchGrammarTypeException, ParseException, IOException {
//        String[] args = {};
//        UserInterface.main(args);
//        System.setSecurityManager(this.testSecurity);
//        System.exit(0);
//    }
//
//    @After
//    public void tearDown() {
//        System.setSecurityManager(this.testSecurity);
//    }
}