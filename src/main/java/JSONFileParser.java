import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * JSONFileParser Class
 * This class is meant to take in a .json file, and parse out its elements
 * The object itself is made up of the name of the json file, the delimiter by which to parse it
 * Additionally, we added a grammarTitle and grammarDesc fields, in order to avoid opening and
 * reading the file over and over simply for a field or two (this allows us to store the values
 * the first time we read it).
 */
public class JSONFileParser {

  private String jsonFile;
  private String splitBy;
  private String grammarTitle;
  private String grammarDesc;
  private final static String LINE_SPLIT = "(?<=\")[^,;](.*?)(?=\")";

  /**
   * Default constructor
   * By default, the grammarTitle and grammarDesc are set to empty strings
   * These fields will be populated when the readFile method is called (if they exist)
   * @param fileName String of the file we are going to parse/handle
   * @throws FileNotFoundException if the file doesn't exist
   */
  public JSONFileParser(String fileName) throws FileNotFoundException {
    if (!(new File(fileName).exists())) {
      throw new FileNotFoundException("The specified template file does not exist. "
          + "Please enter an existing file.");
    } else {
      this.jsonFile = fileName;
    }
    this.splitBy = LINE_SPLIT;
    this.grammarTitle = "";
    this.grammarDesc = "";
  }

  /**
   * Constructor that parses the file by a specific regex expression
   * By default, the grammarTitle and grammarDesc are set to empty strings
   * These fields will be populated when the readFile method is called (if they exist)
   * @param fileName String of the file we are going to parse/handle
   * @param splitBy String regex express by which to parse the .json file
   * @throws FileNotFoundException if the file doesn't exist
   */
  public JSONFileParser(String fileName, String splitBy) throws FileNotFoundException {
    if (!(new File(fileName).exists())) {
      throw new FileNotFoundException("The specified file does not exist. "
          + "Please enter an existing file.");
    } else {
      this.jsonFile = fileName;
    }
    this.splitBy = splitBy;
    this.grammarTitle = "";
    this.grammarDesc = "";
  }

  /**
   * Getter method to get the name of the file
   * @return String name of the .json file (this.jsonFile)
   */
  public String getFileName() {
    return this.jsonFile;
  }

  /**
   * Method that reads the specified .json file, and parses it based on the specified regex
   * @return HashMap with key String and value ArrayList of String info, which contains the file contents
   * The String key is the key in json file, and then the ArrayList is a list of parsed contents
   * (i.e. the data)
   * @throws IOException default error
   * @throws ParseException default error
   */
  public HashMap<String, ArrayList<String>> readFile() throws IOException, ParseException {

    //Setting up the JSON parser
    Object obj = new JSONParser().parse(new FileReader(this.jsonFile));
    JSONObject parser = (JSONObject) obj;

    //Making the hashmap to store all the parsed JSON file info
    HashMap<String, ArrayList<String>> info = new HashMap<>();

    //Get all the keys and removing
    Set keys = parser.keySet();

    //We dont want grammarTitle in the hashmap if it exists in the file
    //If it exists, assign it to the grammarTitle field
    if(keys.contains("grammarTitle")) {
      this.grammarTitle = (String) parser.get("grammarTitle");
      keys.remove("grammarTitle");
    }

    //We dont want grammarDesc in the hashmap if it exists in the file
    //If it exists, assign it to the grammarDesc field
    if(keys.contains("grammarDesc")) {
      this.grammarDesc = (String) parser.get("grammarDesc");
      keys.remove("grammarDesc");
    }

    //now, we iterate through all the rest of the keys
    //get the values corresponding to the list by parsing them and adding to a list
    for(Object s : keys) {

      //Getting one of the keys
      String key = (String) s;

      //Get the key as a STRING
      String line = parser.get(key).toString();

      //ArrayList to store all the key values, created by splitting the values in the string
      ArrayList<String> keyValues = patternMatch(line);

      //Add the arraylist and corresponding key to the hashmap
      info.put(key, keyValues);
    }
    return info;
  }

  /**
   * Helper method for the readFile method
   * @param inputString String that needs to be parsed based on the parseBy field
   * @return ArrayList of String containing all the parsed strings
   */
  private ArrayList<String> patternMatch(String inputString) {
    ArrayList<String> parsedString = new ArrayList<>();
    Pattern pattern = Pattern.compile(this.splitBy);
    Matcher matcher = pattern.matcher(inputString);
    while (matcher.find()) {
      parsedString.add(matcher.group());
    }
    return parsedString;
  }

  /**
   * Getter method to get the grammarTitle
   * Will return an empty String (i.e. "") if the readFile method hasn't been called yet
   * @return String grammarTitle
   */
  public String getTitle() {
    return this.grammarTitle;
  }

  /**
   * Getter method to get the grammarDesc
   * Will return an empty String (i.e. "") if the readFile method hasn't been called yet
   * or if grammarDesc hasn't been given in the .json file
   * @return String of grammar description
   */
  public String getDesc() {
    return this.grammarDesc;
  }

  /**
   * Overridden toString method for default toString()
   * @return String
   */
  @Override
  public String toString() {
    return "JSONFileParser{" +
        "jsonFile='" + jsonFile + '\'' +
        '}';
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
    if (!(o instanceof JSONFileParser)) {
      return false;
    }
    JSONFileParser that = (JSONFileParser) o;
    return jsonFile.equals(that.jsonFile);
  }

  /**
   * Overridden hashCode method for default hashCode()
   * @return int (the hashcode of the given JSONFileParser object)
   */
  @Override
  public int hashCode() {
    return Objects.hash(jsonFile);
  }

}

