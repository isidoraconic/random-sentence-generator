import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import org.json.simple.parser.ParseException;

/**
 * Grammar Class
 * This creates a Grammar object that contains all the data from a .json file
 * The .json file is read/handled by the JSONFileParser class which helps creater the Grammar object
 */
public class Grammar {

  private String grammarTitle;
  private String grammarDesc;
  private HashMap<String, ArrayList<String>> info;

  /**
   * Grammar object constructor 1
   * Reads the JSONFileParser object and then gets the grammarTitle and grammarDesc
   * @param file (JSONFileParser object)
   * @throws IOException default error
   * @throws ParseException default error
   */
  public Grammar(JSONFileParser file) throws IOException, ParseException {
    this.info = file.readFile();
    this.grammarTitle = file.getTitle();
    this.grammarDesc = file.getDesc();
  }

  /**
   * Grammar object constructor 2
   * Takes in a String fileName and then constructs the JSONFileParser object
   * Then, does the same as Constructor 2: Reads the JSONFileParser object
   * And gets the grammarTitle and grammarDesc
   * @param fileName (String)
   * @throws IOException default error
   * @throws ParseException default error
   */
  public Grammar(String fileName) throws IOException, ParseException {
    JSONFileParser file = new JSONFileParser(fileName);
    this.info = file.readFile();
    this.grammarTitle = file.getTitle();
    this.grammarDesc = file.getDesc();
  }

  /**
   * Getter method for the grammarTitle
   * @return String grammarTitle
   */
  public String getGrammarTitle() {
    return this.grammarTitle;
  }

  /**
   * Getter method for grammarDesc
   * @return String grammarDesc
   */
  public String getGrammarDesc() {
    return this.grammarDesc;
  }

  /**
   * Getter method for info
   * @return HashMap with key String and value ArrayList of String info
   */
  public HashMap<String, ArrayList<String>> getInfo() {
    return this.info;
  }

  /**
   * Getter method for a specific ArrayList of String within the info HashMap
   * @param key (String)
   * @return ArrayList of String associated with the given key
   */
  public ArrayList<String> getInfoValue(String key) {
    return this.info.get(key);
  }

  /**
   * Overridden toString method for default toString()
   * Note that grammarDesc is not used, because in the spec it is not guaranteed that each .json
   * file will have a description (but it will have a title).
   * @return String
   */
  @Override
  public String toString() {
    String string =  "Grammar{" +
        "grammarTitle='" + grammarTitle + '\'' +
        ", info=" + info +
        '}';
    return string;
  }

  /**
   * Overridden equals method for default equals()
   * @param o Object to compare equality
   * @return Boolean if the objects are equal (true if yes, false if no)
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Grammar)) {
      return false;
    }
    Grammar grammar = (Grammar) o;
    return grammarTitle.equals(grammar.grammarTitle) &&
        Objects.equals(grammarDesc, grammar.grammarDesc) &&
        info.equals(grammar.info);
  }

  /**
   * Overridden hashCode method for default hashCode()
   * @return int (the hashcode of the given Grammar object)
   */
  @Override
  public int hashCode() {
    return Objects.hash(grammarTitle, grammarDesc, info);
  }


}

