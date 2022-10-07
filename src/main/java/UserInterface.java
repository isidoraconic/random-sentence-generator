import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.simple.parser.ParseException;

/**
 * UserInterface Class
 * Class that serves as a front-end interface for a user
 * Creates all the Grammar objects, from their grammarTitle creates a menu
 * Takes in input from the user to allow them to select a grammar and then generates and prints it
 */
public class UserInterface {

  private String directory;
  private String runMode;
  private List<Grammar> grammarList;

  /**
   * Constructor
   * Sets String directory = null initially, because we will read the directory from the console
   * and assign it later (in the setDirectory method)
   * Also creates an empty ArrayList of Grammars
   * We will create and place the available Grammars there based on the files in the directory
   */
  public UserInterface() {
    this.directory = null;
    this.runMode = null;
    this.grammarList = new ArrayList<>();
  }

  /**
   * Method that sets the UserInterface directory field
   * @param directory (String)
   * @throws NoSuchDirectoryException if the specified directory doesn't exist
   */
  public void setDirectory(String directory) throws NoSuchDirectoryException {
    //Check if the output directory exists, if not, throw NoSuchDirectoryException
    if (!(new File(directory).exists())) {
      throw new NoSuchDirectoryException("The specified directory does not exist. "
          + "Please enter an existing directory.");
    } else {
      this.directory = directory;
    }
  }

  /**
   * Method that sets the UserInterface runtime mode
   * @param mode String param passed if in test mode, otherwise null
   */
  public void setRunMode(String mode) { this.runMode = mode; }

  /**
   * Getter method for the directory (String) field
   * @return this.directory (String)
   */
  public String getDirectory() {
    return this.directory;
  }

  /**
   * Method that adds a Grammar to the list of available Grammars
   * @param grammar (Grammar)
   */
  public void addGrammar(Grammar grammar) {
    this.grammarList.add(grammar);
  }

  //Is it poor form to have a method that prints?
  /**
   * Method that uses the ArrayList of Grammars to create the option menu, with all the
   * Grammars listed in a numbered list
   * @return String of user input from the command line, menu option chosen drives program sequence
   * @throws IOException default IO error
   */
  public String menuCommand() throws IOException {
    System.out.println("The following grammars are available :");
    int counter = 1;
    for(Grammar g : this.grammarList) {
      System.out.println("[" + counter + "] " + g.getGrammarTitle());
      counter++;
    }
    System.out.println();
    System.out.println("Please enter a number corresponding to one of the options above, or 'q' to quit.");
    return readInput();
  }

  /**
   * Helper method that handles the direct user input from input stream reader
   * Returns a buffered reader object for the readInput method to read lines
   * @return BufferedReader object from user terminal input
   */
  public BufferedReader getUserInput() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    return br;
  }

  /**
   * Helper method that passes a default argument for test purposes, quits application
   * Returns a buffered reader object for readINput method to read lines
   * @return BufferedReader object simulating terminal input
   */
  public BufferedReader getTestInput() {
    BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.runMode.getBytes())));
    return br;
  }

  /**
   * Helper method that uses a buffered reader and reads the next line
   * The method will keep reading until it gets a valid input (checked by the checkInput method)
   * It also prompts the user to give a valid input if their input isn't valid (prints to console)
   * @return read String (valid input, since we keep reading until it's valid)
   * @throws IOException error
   */
  private String readInput() throws IOException {
    //Get the option selected and make sure it is in the correct format
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader br = null;
    if (this.runMode != null) {
      br = getTestInput();
    } else {
      br = getUserInput();
    }
    String input = br.readLine();
    while(input == null || !checkInput(input)){
      System.out.println();
      System.out.println("Please enter a VALID output.");
      System.out.println("Enter a number corresponding to one of the options above, or 'q' to quit.");
      input = br.readLine();
    }
    return input;
  }

  /**
   * Helper method for readInput
   * Checks if the read input is valid--if it is either a number within the range of offered grammars
   * or simply 'q' if we want to quit the program
   * @param input (String)
   * @return Boolean if the input String was valid or not
   */
  private boolean checkInput(String input) {
    int choice = 0;

    //First, we check if the string is a char = q
    if(input.charAt(0) == 'q' && input.length() == 1) {
      return true;
    }

    //If the input is not q, it should be an int
    //We try to parse the string to int, if it fails, we know the input is not valid (return false)
    try {
      choice = Integer.parseInt(input);
    } catch (NumberFormatException e) {
      return false;
    }

    //If it a valid integer, we just check if it is within the range of number of grammars given
    return choice > 0 && choice <= this.grammarList.size();
  }

  /**
   * Method that handles the selected Grammar choice (or 'q' to quit) and constructs the sentence
   * Once it is done, it prints the sentence to the console,
   * and then prompts the user for a new input, and reads it
   * @param input String (the user's selected menu choice, which has been checked and is valid)
   * @throws IOException default IO error
   * @throws NoSuchGrammarTypeException custom error class for no grammar type exists in grammar JSON
   */
  public void handleInput(String input) throws IOException, NoSuchGrammarTypeException {
    int grammarChoice = 0;
    while(!input.equals("q")) {
      grammarChoice = Integer.parseInt(input)-1;
      SentenceGenerator sentenceGen = new SentenceGenerator(this.grammarList.get(grammarChoice), null);

      //Surrounding the buildSentence with a try catch, so that we can catch a
      //'NoSuchGrammarException' but continue the method if it fails
      try {
        String sentence = sentenceGen.buildSentence();
        System.out.println(sentence);
      } catch (NoSuchGrammarTypeException e) {
        System.out.println(e.getMessage());
      } catch (StackOverflowError e) {
        System.out.println("Grammar file structure creates an infinite recursive call on same grammar type. " + e.getMessage());
      }
      System.out.println();
      System.out.println("Enter a number corresponding to one of the menu options for another grammar, or 'q' to quit.");
      input = readInput();
    }
  }

  /**
   * Overridden toString method for default toString()
   * @return String
   */
  @Override
  public String toString() {
    return "UserInterface{" +
        "directory='" + directory + '\'' +
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
    if (!(o instanceof UserInterface)) {
      return false;
    }
    UserInterface that = (UserInterface) o;
    return Objects.equals(directory, that.directory) &&
        Objects.equals(grammarList, that.grammarList);
  }

  /**
   * Overridden hashCode method for default hashCode()
   * @return int (the hashcode of the given UserInterface object)
   */
  @Override
  public int hashCode() {
    return Objects.hash(directory, grammarList);
  }


  /**
   * Main method for the user interface program
   * Is called with a command line argument, which is the directory where the grammar .json files
   * are located.
   * With this, it creates a UserInterface object (or throws an error if the directory is not valid).
   * The program is also terminated if the user doesn't pass a command line argument (directory).
   * Reads all the .json grammar files, creates Grammar objects, and adds them to the Grammars list.
   * Then, prompts the user with menu options and prints appropriate Grammar sentences.
   * @param args String directory where all the grammar .json files are located
   * @throws NoSuchDirectoryException custom error class for when provided directly doesn't exist
   * @throws IOException default IO error
   * @throws ParseException default parse error for handling JSON text file
   * @throws NoSuchGrammarTypeException custom error class for no grammar type exists in grammar JSON
   */
  public static void main(String[] args)
      throws NoSuchDirectoryException, IOException, ParseException, NoSuchGrammarTypeException {

    UserInterface ui = new UserInterface(); //  /Users/isidoraconic/Desktop/json_files/
    try {
      ui.setDirectory(args[0]);
      if (args.length == 2) {
        ui.setRunMode(args[1]);
      }
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("You did not provide any directory to start the program." +
          " Please provide a valid/existing directory with .json files.");
      System.exit(0);
    }

    //Getting all files in the specified directory, and making Grammar objects out of them
    //Inspired by: https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
    File folder = new File(ui.getDirectory());
    File[] listOfFiles = folder.listFiles();
    for(int i = 0; i < listOfFiles.length; i++) {
      String filePath = ui.getDirectory() + "/" + listOfFiles[i].getName();
      Grammar add = new Grammar(filePath);
      ui.addGrammar(add);
    }

    //Starts the entire program and keeps it cycling until the user chooses 'q'
    String input = ui.menuCommand();
    ui.handleInput(input);
  }


}
