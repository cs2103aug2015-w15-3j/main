/**
 * Class that defines the basic parsing unit that reads and process the user input.
 * 
 * @author LingJie
 */

package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.DateTime;

public class SimpleParser implements ParserInterface {
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  /**
   * Parses the user's input and creates corresponding ParsedInput object.
   * 
   * @param userInput   String of user input into the system.
   * @return            ParsedInput object based on user input.
   */
  public ParsedInput parse(String userInput) {
    // TODO Auto-generated method stub
    wordsOfInput = userInput.split(" ");
    
    if (isFirstWord("add")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD);
      if (containsDateInput()){
        parseDatedTask();
      } else {
        // builder = createBuilder(builder, );
      }
    } else if (isFirstWord("edit")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT);
      //TODO
    } else if (isFirstWord("delete")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE);  
      //TODO
    } else if (isFirstWord("done")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DONE); 
      //TODO
    } else if (isFirstWord("undo")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.UNDO);
      //TODO
    } else if (isFirstWord("display")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DISPLAY);
      //TODO
    } else if (isFirstWord("exit")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EXIT);
      //TODO
    }
     
    
    return builder.createParsedInput();
  }
  

  public ParsedInput.ParsedInputBuilder createBuilder(ParsedInput.ParsedInputBuilder builder, int id, 
      String taskName, DateTime dateTime, char displayOptions) {
    builder.id(id);
    builder.name(taskName);
    builder.dateTime(dateTime);
    builder.displayOptions(displayOptions);
    return builder;
  }
  
  
  /**
   * Method that checks first String of String array for a particular word.
   * 
   * @param word    Word to check for in the first element of array.
   * @return        boolean true or false whether it contains the word.
   */
  public boolean isFirstWord(String word) {
    return wordsOfInput[0].toLowerCase().equals(word.toLowerCase());
  }
  
  public boolean containsDateInput() {
    
    return false; //TODO    
  }
  
  public void parseDatedTask() {
    
    //TODO
  }

}
