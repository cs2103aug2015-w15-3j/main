/**
 * Class that handles add parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandArgumentException;

public class AddParser {
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  private static final String datePattern = Constants.DATE_PATTERN;
  private static final String dateOperator = Constants.DATE_OPERATOR;
  private static final String timePattern = Constants.TIME_PATTERN;
  private static final DateTimeFormat dtFormat = new DateTimeFormat();
  
  public AddParser(ParsedInput.ParsedInputBuilder builder, String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    this.builder = builder;
  }
  
  /**
   * Method that parses the input for name, date or time inputs when task is added.
   * 
   * @return ParsedInputBuilders    Appropriate ParsedInputBuilders accordingly.
   * @throws                        IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
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
    
    startDate = dtFormat.formatDate(startDate,0);
    endDate = dtFormat.formatDate(endDate,0);
    startTime = dtFormat.formatTime(startTime);
    endTime = dtFormat.formatTime(endTime);
    
    DateTime dateTime = null;
    if (containsStartDate && containsStartTime && containsEndDate && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endDate, endTime);
      checkStartEndDate(startDate, endDate, dateTime);
    } else if (containsStartDate && containsStartTime && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endTime);
      checkStartDate(startDate, dateTime);
    } else if (containsStartDate && containsStartTime) {
      dateTime = new DateTime(startDate, startTime);
      checkStartDate(startDate, dateTime);
    } else if (containsStartDate) {
      dateTime = new DateTime(startDate);
      checkStartDate(startDate, dateTime);
    }
    
    if (!name.equals("")) {
      return builder.name(name).dateTime(dateTime);
    } else {
      return builder.dateTime(dateTime);
    }
  }
  
  /**
   * Method that checks if start and end date exists on the calendar.
   * 
   * @param startDate       Start date in dd/mm/yyyy format.
   * @param endDate         End date in dd/mm/yyyy format.
   * @param dateTime        DateTime object created from both startDate & endDate.
   * @throws                IllegalCommandArgumentException
   */
  public void checkStartEndDate(String startDate, String endDate, DateTime dateTime)
      throws IllegalCommandArgumentException {
    int startDay = Integer.parseInt(startDate.split(dateOperator)[0]);
    int endDay = Integer.parseInt(endDate.split(dateOperator)[0]);
    if (dateTime.getStartDate().getDayOfMonth() != startDay ||
        dateTime.getEndDate().getDayOfMonth() != endDay) {
      throw new IllegalCommandArgumentException(
                "Date doesn't exist.", Constants.CommandParam.DATETIME);
    }
  }
  
  /**
   * Method that checks if start date exists on the calendar.
   * 
   * @param startDate       Start date in dd/mm/yyyy format.
   * @param dateTime        DateTime object created from startDate.
   * @throws                IllegalCommandArgumentException
   */
  public void checkStartDate(String startDate, DateTime dateTime)
      throws IllegalCommandArgumentException {
    int startDay = Integer.parseInt(startDate.split(dateOperator)[0]);
    if (dateTime.getStartDate().getDayOfMonth() != startDay) {
      throw new IllegalCommandArgumentException(
          "Date doesn't exist.", Constants.CommandParam.DATETIME);
    }
  }
}
