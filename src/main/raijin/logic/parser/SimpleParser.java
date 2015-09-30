/**
 * Class that defines the basic parsing unit that reads and process the user input.
 * 
 * @author LingJie
 */

package raijin.logic.parser;

import java.text.DecimalFormat;
import java.time.LocalDate;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;

public class SimpleParser implements ParserInterface {
  
  private static final String datePattern = Constants.DATE_PATTERN;
  private static final String dateOperator = Constants.DATE_OPERATOR;
  private static final String timePattern = Constants.TIME_PATTERN;
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  /**
   * Parses the user's input and creates corresponding ParsedInput object.
   * 
   * @param userInput   String of user input into the system.
   * @return            ParsedInput object based on user input.
   * @throws Exception  When invalid input is detected.
   */
  public ParsedInput parse(String userInput) throws IllegalArgumentException {
    wordsOfInput = userInput.split(" ");
    
    if (isFirstWord("add")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD);
      parseAddTask();
    } else if (isFirstWord("edit")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT);
      parseEditTask();
    } else if (isFirstWord("delete")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE);  
      parseDeleteTask();
    } else if (isFirstWord("done")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DONE); 
      parseDoneTask();
    } else if (isFirstWord("undo")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.UNDO);
      parseUndo();
    } else if (isFirstWord("display")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DISPLAY);
      parseDisplay();
    } else if (isFirstWord("exit")) {
      builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EXIT);
      parseExit();
    }
     
    return builder.createParsedInput();
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
   * 
   * @throws Exception  When invalid input command is detected.
   */
  public void parseAddTask() throws IllegalArgumentException {
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
          startDate = wordsOfInput[i+1];
          if (i < wordsOfInput.length - 2 && wordsOfInput[i+2].matches(timePattern)) {
            // Checks for format of {startTime}. If doesn't exist, ignore.
            containsStartTime = true;
            startTime = wordsOfInput[i+2];
          }
          if (containsStartDate && containsStartTime && i < wordsOfInput.length-5 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endDate endTime
            if (wordsOfInput[i+4].matches(datePattern)) {
              containsEndDate = true;
              endDate = wordsOfInput[i+4];
            } else {
              throw new IllegalArgumentException("Invalid input! End date expected."); // Invalid command/format
            }
            if (wordsOfInput[i+5].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+5];
            } else {
              throw new IllegalArgumentException("Invalid input! End time expected."); // Invalid command/format
            } 
          } else if (containsStartDate && containsStartTime && i < wordsOfInput.length-4 && wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endTime
            if (wordsOfInput[i+4].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+4];
            } else {
              throw new IllegalArgumentException("Invalid input! End time expected."); // Invalid command/format
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
    
    startDate = formatDate(startDate);
    endDate = formatDate(endDate);
    
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
    
    builder.name(name).dateTime(dateTime);
  }
  
  /**
   * Method that parses the input for any modification users want to make for a specific task.
   * Able to modify name, date, or time.
   * 
   * @throws IllegalArgumentException
   */
  public void parseEditTask() throws IllegalArgumentException {
    //TODO
  }
  
  public void parseDeleteTask() throws IllegalArgumentException {
    //TODO
  }
  
  public void parseDoneTask() throws IllegalArgumentException {
    //TODO
  }
  
  public void parseUndo() throws IllegalArgumentException {
    //TODO
  }
  
  public void parseDisplay() throws IllegalArgumentException {
    //TODO
  }
  
  public void parseExit() throws IllegalArgumentException {
    //TODO
  }
  
  /**
   * Method that formats date into the proper dd/mm/yyyy format.
   * Date will be assumed to be next year if (year isn't input) & (current date is later).
   * 
   * @param date    date String that hasn't been formatted.
   * @return        
   */
  public String formatDate(String date) {
    if (date.length() == 0) {
      return date;
    }
    DecimalFormat twoDigits = new DecimalFormat("00");
    String[] dayMonth = date.split(dateOperator);
    String[] months = Constants.MONTHS;
    
    int day = Integer.parseInt(dayMonth[0]);
    int month = 0;
    int year;
    
    // Check for month written in letters
    for (int i = 0; i < months.length; i++) {
      if (dayMonth[1].equals(months[i])) {
        month = i+1;
      }
    }
    if (month == 0) {
      month = Integer.parseInt(dayMonth[1]);
    }
    
    int dayNow = LocalDate.now().getDayOfMonth();
    int monthNow = LocalDate.now().getMonthValue();
    
    // Check for year input. If no year input, use existing year or the upcoming one.
    if (dayMonth.length == 3) {
      year = Integer.parseInt(dayMonth[2]);
      if (dayMonth[2].length() < 4) {
        year += 2000; // ASSUMPTION: This app is used within the year of 2000 to 2999.
      }
    } else if (month < monthNow || (month == monthNow && day < dayNow)) {
      year = LocalDate.now().getYear() + 1;
    } else {
      year = LocalDate.now().getYear();
    }
    
    return twoDigits.format(day) + "/" + twoDigits.format(month) + "/" + year;
  }
  
}
