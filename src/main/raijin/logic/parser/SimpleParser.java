/**
 * Class that defines the basic parsing unit that reads and process the user input.
 * 
 * @author LingJie
 */

package raijin.logic.parser;

import java.text.DecimalFormat;
import java.time.LocalDate;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandException;
import raijin.common.exception.IllegalCommandArgumentException;
import raijin.common.exception.FailedToParseException;

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
  public ParsedInput parse(String userInput) throws FailedToParseException {
    wordsOfInput = userInput.split(" ");
    
    try {
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
      } else if (isFirstWord("display")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DISPLAY);
        parseDisplay();
      } else if (isFirstWord("help")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.HELP);
      } else if (isFirstWord("exit")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EXIT);
      } else {
        throw new IllegalCommandException("Invalid command input.", wordsOfInput[0]);
      }
    } catch (IllegalCommandException e1) {
      throw new FailedToParseException(e1.getMessage(), userInput, e1);
    } catch (IllegalCommandArgumentException e2) {
      throw new FailedToParseException(e2.getMessage(), userInput, e2);
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
   * @throws    IllegalCommandArgumentException
   */
  public void parseAddTask() throws IllegalCommandArgumentException {
    boolean containsStartDate = false;
    boolean containsEndDate = false;
    boolean containsStartTime = false;
    boolean containsEndTime = false;
    String name = "", startDate = "", startTime = "", endDate = "", endTime = "";
    int index = wordsOfInput.length; // (Last index of task name - 1)
    
    for (int i = 0; i < wordsOfInput.length - 1; i++) {
      if (wordsOfInput[i].toLowerCase().matches(Constants.DATE_START_PREPOSITION)) {
        if (wordsOfInput[i+1].toLowerCase().matches(datePattern)) {
          // Checks for format of {startDate}. If doesn't exist, ignore.
          containsStartDate = true;
          index = i;
          startDate = wordsOfInput[i+1];
          if (i < wordsOfInput.length - 2 && wordsOfInput[i+2].matches(timePattern)) {
            // Checks for format of {startTime}. If doesn't exist, ignore.
            containsStartTime = true;
            startTime = wordsOfInput[i+2];
          }
          if (containsStartDate && containsStartTime && i < wordsOfInput.length-5 && 
              wordsOfInput[i+3].equalsIgnoreCase("to")) {
            // startDate startTime endDate endTime
            if (wordsOfInput[i+4].toLowerCase().matches(datePattern)) {
              containsEndDate = true;
              endDate = wordsOfInput[i+4];
            } else {
              throw new IllegalCommandArgumentException("Invalid end date format.",
                                                        Constants.CommandParam.DATETIME); 
            }
            if (wordsOfInput[i+5].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+5];
            } else {
              throw new IllegalCommandArgumentException("Invalid end time format.",
                                                        Constants.CommandParam.DATETIME); 
            } 
          } else if (containsStartDate && containsStartTime && i < wordsOfInput.length-4 && 
              wordsOfInput[i+3].toLowerCase().matches(Constants.DATE_END_PREPOSITION)) {
            // startDate startTime endTime
            if (wordsOfInput[i+4].matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+4];
            } else {
              throw new IllegalCommandArgumentException("Invalid end time format.",
                                                        Constants.CommandParam.DATETIME); 
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
    
    startDate = formatDate(startDate,0);
    endDate = formatDate(endDate,0);
    startTime = formatTime(startTime);
    endTime = formatTime(endTime);
    
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
   * @throws IllegalCommandArgumentException
   */
  public void parseEditTask() throws IllegalCommandArgumentException {
    try {
      builder.id(Integer.parseInt(wordsOfInput[1]));
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.",
                                                Constants.CommandParam.ID);
    }
    
    // Deletes taskID from wordsOfInput and makes use of parseAddTask() method.
    String[] newWordsOfInput = new String[wordsOfInput.length-1];
    newWordsOfInput[0] = "edit";
    for (int i = 1; i < newWordsOfInput.length; i++) {
      newWordsOfInput[i] = wordsOfInput[i+1];
    }
    wordsOfInput = newWordsOfInput;
    parseAddTask();
  }
  
  /**
   * Method that deletes a task based on the taskID input by user.
   * 
   * @throws IllegalCommandArgumentException
   */
  public void parseDeleteTask() throws IllegalCommandArgumentException {
    try {
      builder.id(Integer.parseInt(wordsOfInput[1]));
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.", 
                                                Constants.CommandParam.ID);
    }
  }
  
  /**
   * Method that marks a task as done based on taskID input by user.
   * 
   * @throws IllegalCommandArgumentException
   */
  public void parseDoneTask() throws IllegalCommandArgumentException {
    try {
      builder.id(Integer.parseInt(wordsOfInput[1]));
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.",
                                                Constants.CommandParam.ID);
    }
  }
  
  /**
   * Method that parses display type input by user and responds accordingly.
   * Currently allows for "p", "c" or dates for its options.
   * 
   * @throws IllegalCommandArgumentException
   */
  public void parseDisplay() throws IllegalCommandArgumentException {
    String displayType = "p";
    for (int i = 1; i < wordsOfInput.length; i++) {
      if (wordsOfInput[i].matches(datePattern)) {
        builder.dateTime(new DateTime(formatDate(wordsOfInput[i], 1)));
      } else if (wordsOfInput[i].matches("p|c")){
        displayType = wordsOfInput[i];
      }
    }
    // TO ADD ADDITIONAL FEATURES HERE.
    builder.displayOptions(displayType);
  }
  
  /**
   * Method that formats date into the proper dd/mm/yyyy format.
   * Date will be assumed to be next year if (year isn't input) & (current date is later).
   * Assumption: Date string has already been checked to be valid.
   * 
   * @param date    date String that hasn't been formatted.
   * @param type    Type of formatting: 0 for add/edit, 1 for display
   * @return        Date string formatted to proper dd/mm/yyyy format.  
   */
  public String formatDate(String date, int type) {
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
      if (dayMonth[1].toLowerCase().equals(months[i])) {
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
    } else if (type == 0 && (month < monthNow || (month == monthNow && day < dayNow))) {
      year = LocalDate.now().getYear() + 1;
    } else {
      year = LocalDate.now().getYear();
    }
    
    
    return twoDigits.format(day) + "/" + twoDigits.format(month) + "/" + year;
  }
  
  /**
   * Method that formats time to a proper format that will allow LocalTime parsing.
   * Assumes that the time format has already been checked to be valid (24h timing).
   * 
   * @param time    String of time input by user.
   * @return        The proper time format after formatting the string input.     
   */
  public static String formatTime(String time) {
    if (time.length() == 3) {
      if (time.charAt(0) == '0') {
        // Assuming the String time starts with "00" as it passed the format matching already.
        time += "0";
      } else {
        time = "0" + time;
      }
    }

    return time;
  }
}
