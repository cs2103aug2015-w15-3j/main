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
     
    
    return builder.createParsedInput();
  }
  
  /**
   * Method that modifies the attribute values of the ParseInputBuilder.
   * 
   * @param builder             builder to be modified
   * @param id                  Task ID to be allocated for this task.
   * @param taskName            Name of To-do.
   * @param dateTime            Date and time of task.
   * @param displayOptions      Display option.
   * @return                    builder object back after inserting changing its attributes
   */
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
  
  /**
   * Method that parses the input for any date or time inputs when task is added.
   * Creates appropriate ParseInputBuilders accordingly.
   */
  public void parseAddTask() {
    boolean containsStartDate = false;
    boolean containsEndDate = false;
    boolean containsStartTime = false;
    boolean containsEndTime = false;
    int day, month, time; // For testing changing of string to integer format.
    String startDate, startTime, endDate, endTime;
    
    for (int i = 0; i < wordsOfInput.length - 1; i++) {
      if (wordsOfInput[i].equalsIgnoreCase("by") || wordsOfInput[i].equalsIgnoreCase("on")) {
        if (wordsOfInput[i+1].contains("/")) {
          // Checks for format of {startDate startTime}. If doesn't exist, ignore.
          try {
            String[] dayMonth = wordsOfInput[i+1].split("/");
            day = Integer.parseInt(dayMonth[0]);
            month = Integer.parseInt(dayMonth[1]);
            containsStartDate = true;
            startDate = wordsOfInput[i+1];
          } catch (NumberFormatException e) {
          } catch (NullPointerException e) {
          }
          try {
            time = Integer.parseInt(wordsOfInput[i+2]);
            containsStartTime = true;
            startTime = wordsOfInput[i+2];
          } catch (NumberFormatException e) {
          } catch (NullPointerException e) {
          }
          
          if (containsStartDate && containsStartTime && i < wordsOfInput.length-5 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endDate endTime
            try {
              String[] dayMonth = wordsOfInput[i+4].split("/");
              day = Integer.parseInt(dayMonth[0]);
              month = Integer.parseInt(dayMonth[1]);
              containsEndDate = true;
              startDate = wordsOfInput[i+4];
            } catch (NumberFormatException e) {
              // Invalid command?
            } catch (NullPointerException e) {
              // Invalid command?
            }
            try {
              time = Integer.parseInt(wordsOfInput[i+5]);
              containsEndTime = true;
              endTime = wordsOfInput[i+5];
            } catch (NumberFormatException e) {
              // Invalid command?
            }
          } else if (containsStartDate && containsStartTime && i < wordsOfInput.length-4 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endTime
            try {
              time = Integer.parseInt(wordsOfInput[i+4]);
              containsEndTime = true;
              endTime = wordsOfInput[i+4];
            } catch (NumberFormatException e) {
              // Invalid command?
            }
          }
        } else {
          // Checks for format of {startTime}. If doesn't exist, ignore.
          try {
            time = Integer.parseInt(wordsOfInput[i+1]);
            containsStartTime = true;
          } catch (NumberFormatException e) {
          } catch (NullPointerException e) {
          }
        }
      }
    }
    
    // Creates relevant DateTime objects and then creates ParseInputBuilders.
    if (containsStartDate && containsStartTime) {
      if (containsEndDate && containsEndTime) {
         
        //createBuilder();
      } else if (containsEndTime) {
        
        //createBuilder();
      } else {
      
        //createBuilder();
      } 
    } else if (containsStartDate) {
      
      //createBuilder();
    } 
  }
  
}
