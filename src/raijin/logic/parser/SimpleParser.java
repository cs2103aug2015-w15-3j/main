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
  
  // Move all these final Strings to Constants.java?
  // Very flexible regex for recognizing date patterns. Available test cases at: http://fiddle.re/56t2j6
  private static final String datePattern = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:jan|mar|may|jul|aug|oct|dec)))"
      + "\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))\\2))"
      + "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]"
      + "|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)"
      + "(?:(?:0?[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep))|(?:1[0-2]|(?:oct|nov|dec)))(\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2}))?$";
  
  // Regex for recognizing a date operator. Used for splitting into String array.
  private static final String dateOperator = "(\\/|-|\\.)";
  
  // Flexible regex for recognizing 24hr time patterns. Available test cases at: http://fiddle.re/bc9mj6
  private static final String timePattern = "^([01]?[0-9]|2[0-3])[0-5][0-9]$";
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  private int taskID;
  
  /**
   * Parses the user's input and creates corresponding ParsedInput object.
   * 
   * @param userInput   String of user input into the system.
   * @return            ParsedInput object based on user input.
   */
  public ParsedInput parse(String userInput) {
    // TODO Auto-generated method stub
    wordsOfInput = userInput.split(" ");
    taskID = -1; //TODO
    
    if (isFirstWord("add")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD);
      parseAddTask();
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
     
    builder.id(taskID);
    return builder.createParsedInput();
  }
  
  /**
   * Method that modifies the attribute values of the ParseInputBuilder.
   * 
   * @param taskName            Name of To-do.
   * @param dateTime            Date and time of task.
   * @param displayOptions      Display option.
   * @return                    builder object back after inserting changing its attributes
   */
  public ParsedInput.ParsedInputBuilder createBuilder(String taskName, 
      DateTime dateTime, char displayOptions) {
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
  
  /**
   * Method that parses the input for any date or time inputs when task is added.
   * Creates appropriate ParseInputBuilders accordingly.
   */
  public void parseAddTask() {
    boolean containsStartDate = false;
    boolean containsEndDate = false;
    boolean containsStartTime = false;
    boolean containsEndTime = false;
    String name = "", startDate = "", startTime = "", endDate = "", endTime = "";
    int index = wordsOfInput.length; // (Last index of task name - 1)
    
    for (int i = 0; i < wordsOfInput.length - 1; i++) {
      if (wordsOfInput[i].equalsIgnoreCase("by") || wordsOfInput[i].equalsIgnoreCase("on")) {
        if (wordsOfInput[i+1].matches(datePattern)) {
          // Checks for format of {startDate}. If doesn't exist, ignore.
          containsStartDate = true;
          index = i;
          startDate = wordsOfInput[i+1].replaceAll(dateOperator, "/");
          if (i < wordsOfInput.length - 2 && wordsOfInput[i+2].matches(timePattern)) {
            // Checks for format of {startTime}. If doesn't exist, ignore.
            containsStartTime = true;
            startTime = wordsOfInput[i+2];
          }
          if (containsStartDate && containsStartTime && i < wordsOfInput.length-5 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endDate endTime
            if (wordsOfInput[i+4].matches(datePattern)) {
              containsEndDate = true;
              endDate = wordsOfInput[i+4].replaceAll(dateOperator, "/");
            } else {
              // Invalid command/format?
            }
            if (wordsOfInput[i+5].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+5];
            } else {
              // Invalid command/format?
            } 
          } else if (containsStartDate && containsStartTime && i < wordsOfInput.length-4 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endTime
            if (wordsOfInput[i+4].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+4];
            } else {
              // Invalid command/format?
            } 
          }
        } else if (wordsOfInput[i+1].matches(timePattern)) {
          // Checks for format of {startTime}. If doesn't exist, ignore.
          containsStartTime = true;
          index = i;
          startTime = wordsOfInput[i+1];
        }
      }
    }
    
    // Creates objects and then ParseInputBuilders based on info.
    for (int i = 1; i < index; i++) {
      name += wordsOfInput[i];
      if (i < index-1) {
        name += " ";
      }
    }
    
    DateTime dateTime = null;
    if (containsStartDate && containsStartTime && containsEndDate && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endDate, endTime);
    } else if (containsStartDate && containsStartTime && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endTime);
    } else if (containsStartDate && containsStartTime) {
      dateTime = new DateTime(startDate, startTime);
    } else if (containsStartDate) {
      dateTime = new DateTime(startDate);
    }
    //TODO TO CONFIRM: DISPLAYOPTION
    createBuilder(name, dateTime, 'u'); 
  }
  
}
