/**
 * Class that handles ADD parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandArgumentException;

public class AddParser {
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  private TreeSet<String> tags;
  private TreeSet<String> names;
  private String currentTime;
  private String currentDate;
  private int parseType; // 0 for add, 1 for edit, 2 for display.
  
  private boolean containsStartDate;
  private boolean containsEndDate;
  private boolean containsStartTime;
  private boolean containsEndTime;
  
  private static final String datePattern = Constants.DATE_PATTERN;
  private static final String dateOperator = Constants.DATE_OPERATOR;
  private static final String timePattern = Constants.TIME_PATTERN;
  private static final DateTimeFormat dtFormat = new DateTimeFormat();
  
  private String name, startDate, startTime, endDate, endTime;
  private int start, tpsIndex, index;
  
  public AddParser(ParsedInput.ParsedInputBuilder builder, String[] wordsOfInput, int type) 
      throws IllegalCommandArgumentException {
    
    if (type == 0 && wordsOfInput.length < 2) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_TASK_NAME,
          Constants.CommandParam.NAME);
    }  
    
    this.wordsOfInput = wordsOfInput;
    this.builder = builder;
    tags = new TreeSet<String>();
    names = new TreeSet<String>();
    
    containsStartDate = false;
    containsEndDate = false;
    containsStartTime = false;
    containsEndTime = false;
    
    parseType = type;
    currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
    currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    
    name = ""; 
    startDate = ""; 
    startTime = ""; 
    endDate = ""; 
    endTime = "";
    
  }
  
  /**
   * Method that parses the input for name, date or time inputs when task is added.
   * 
   * @return    ParsedInputBuilder                 Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    start = 1; // For recording starting index of the name currently being checked
    index = wordsOfInput.length; // (Last index of task name - 1) currently being checked
    tpsIndex = wordsOfInput.length; // Latest index of tag/priority/subtask inputs
    
    for (int i = 0; i < wordsOfInput.length; i++) {
      if (wordsOfInput[i].toLowerCase().matches(Constants.DATE_START_PREPOSITION)) {
        
        // Checks for format of {startDate}. If doesn't exist, ignore.
        if (i < wordsOfInput.length-1 && wordsOfInput[i+1].toLowerCase().matches(datePattern)) {
          
          containsStartDate = true;
          index = tpsIndex > i ? i : tpsIndex ;
          startDate = wordsOfInput[i+1];
          
          // startDate {startTime}
          if (i < wordsOfInput.length-2 && wordsOfInput[i+2].toLowerCase().matches(timePattern)) {
            containsStartTime = true;
            startTime = wordsOfInput[i+2];
          } else if (i < wordsOfInput.length-2 
              && !wordsOfInput[i+2].toLowerCase().matches(timePattern)
              && !wordsOfInput[i+2].matches(Constants.DATE_END_PREPOSITION) 
              && !wordsOfInput[i+2].equals(";") && !wordsOfInput[i+2].matches(Constants.PREFIXES)){
            throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_STARTTIME,
                Constants.CommandParam.DATETIME); 
          }

          
          // startDate {endDate}
          if (i < wordsOfInput.length-3 && 
              wordsOfInput[i+2].matches(Constants.DATE_END_PREPOSITION)) {
            if (wordsOfInput[i+3].matches(datePattern)) {
              containsEndDate = true;
              endDate = wordsOfInput[i+3];
            } else {
              throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ENDDATE,
                  Constants.CommandParam.DATETIME); 
            }
          }          
          
          if (containsStartDate && containsStartTime && i < wordsOfInput.length-4 && 
              wordsOfInput[i+3].toLowerCase().matches(Constants.DATE_END_PREPOSITION)) {
            
            if (wordsOfInput[i+4].toLowerCase().matches(timePattern)) {
              
              // startDate startTime {endTime}
              containsEndTime = true;
              endTime = wordsOfInput[i+4];
              
            } else if (wordsOfInput[i+4].matches(datePattern)) {
              
              // startDate startTime {endDate} endTime
              containsEndDate = true;
              endDate = wordsOfInput[i+4];
              
              // startDate startTime endDate {endTime}
              if (wordsOfInput[i+5].toLowerCase().matches(timePattern)) {
                containsEndTime = true;
                endTime = wordsOfInput[i+5];
              } else {
                throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ENDTIME,
                    Constants.CommandParam.DATETIME); 
              }
              
            } else {
              throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ENDDATETIME,
                  Constants.CommandParam.DATETIME); 
            } 
            
          } 
        } else if (i < wordsOfInput.length-1 && 
            wordsOfInput[i+1].toLowerCase().matches(timePattern)) {
          
          // Checks for format of {startTime}. If doesn't exist, ignore.
          containsStartTime = true;
          index = tpsIndex > i ? i : tpsIndex ;
          startTime = wordsOfInput[i+1];
          
          // startTime {endTime}
          if (i < wordsOfInput.length - 3 && 
              wordsOfInput[i+2].matches(Constants.DATE_END_PREPOSITION)) {
            if (wordsOfInput[i+3].toLowerCase().matches(timePattern)) {
              containsEndTime = true;
              endTime = wordsOfInput[i+3];
            } else {
              throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ENDTIME,
                  Constants.CommandParam.DATETIME); 
            } 
          }
          
        } else if (i < wordsOfInput.length-1 && 
            wordsOfInput[i+1].toLowerCase().matches(Constants.DAYS)) {
          // by (day)
          
          containsStartDate = true;
          index = tpsIndex > i ? i : tpsIndex ;
          produceDateFromDay(wordsOfInput[i+1], 0);
          
          if (i < wordsOfInput.length-2 && wordsOfInput[i+2].toLowerCase().matches(timePattern)) {
            startTime = wordsOfInput[i+2];
          
            if (i < wordsOfInput.length - 4 && 
                wordsOfInput[i+3].matches(Constants.DATE_END_PREPOSITION)) {
              if (wordsOfInput[i+4].toLowerCase().matches(timePattern)) {
                endTime = wordsOfInput[i+4];
              } else {
                throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ENDTIME,
                    Constants.CommandParam.DATETIME); 
              }
            }
            
          }
          
        } else if (i < wordsOfInput.length-1 && wordsOfInput[i+1].toLowerCase().equals("next")) { 
          lookUpNextDate(i);
        }
      }
    
      checkForPriority(i);
      checkForProject(i);
      checkForTag(i);
      checkForSubtask(i);
      checkForBatchAdding(i);
      
    }
    
    /********** Creates objects and then ParseInputBuilders based on info. **********/  
    
    // Adds name that isn't checked by ";" (i.e. start is < wordsOfInput.length)
    for (int n = start; n < index; n++) {
      if (wordsOfInput[n].indexOf("/") == 0) {
        name += wordsOfInput[n].substring(1);
      } else {
        name += wordsOfInput[n];
      }
      if (n < index-1) {
        name += " ";
      }
    }
    
    if (name.length() == 0 && parseType == 0) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_TASK_NAME, 
          Constants.CommandParam.NAME);
    } 
    names.add(name);
    
    // Check if type of parsing is for display or not. 
    // If it is for display, no need to check if date has already passed.
    if (parseType != 2) {
      startDate = dtFormat.formatDate(startDate,0);
      endDate = dtFormat.formatDate(endDate,0);
    } else {
      startDate = dtFormat.formatDate(startDate,1);
      endDate = dtFormat.formatDate(endDate,1);
    }
    
    startTime = dtFormat.formatTime(startTime);
    endTime = dtFormat.formatTime(endTime);
    
    DateTime dateTime = null;
    if (containsStartDate && containsStartTime && containsEndDate && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endDate, endTime);
      checkStartEndDate(startDate, endDate, dateTime);
    } else if (containsStartDate && containsStartTime && containsEndTime) {
      dateTime = new DateTime(startDate, startTime, endTime);
      checkEndDate(startDate, dateTime);
    } else if (containsStartDate && containsStartTime) {
      dateTime = new DateTime(startDate, startTime);
      checkEndDate(startDate, dateTime);
    } else if (containsStartDate && containsEndDate) {
      dateTime = new DateTime(startDate, currentTime, endDate, "2359");
      checkStartEndDate(startDate, endDate, dateTime);
    } else if (containsStartDate) {
      dateTime = new DateTime(startDate);
      checkEndDate(startDate, dateTime);
    } else if (containsStartTime && containsEndTime) {
      dateTime = new DateTime(currentDate, startTime, endTime);
    } else if (containsStartTime) {
      dateTime = new DateTime(currentDate, startTime);
    }
    
    return builder.name(names).dateTime(dateTime).tag(tags);

  }

  public void lookUpNextDate(int i) {
    if (i < wordsOfInput.length-2) {           
      if (wordsOfInput[i+2].toLowerCase().matches(Constants.DAYS)) {
        // by next (day)
        containsStartDate = true;
        index = tpsIndex > i ? i : tpsIndex ;
        produceDateFromDay(wordsOfInput[i+2], 1);
      } else if (wordsOfInput[i+2].toLowerCase().matches("week|wk")) {
        // by next (week)
        containsStartDate = true;
        index = tpsIndex > i ? i : tpsIndex ;
        produceDateFromDay(null, 2);
      } else if (wordsOfInput[i+2].toLowerCase().matches("month|mth")) {
        // by next (month)
        containsStartDate = true;
        index = tpsIndex > i ? i : tpsIndex ;
        produceDateFromDay(null, 3);
      } else if (wordsOfInput[i+2].toLowerCase().matches("year|yr")) {
        // by next (year)
        containsStartDate = true;
        index = tpsIndex > i ? i : tpsIndex ;
        produceDateFromDay(null, 4);
      }
    }
  }

  public void checkForBatchAdding(int i) throws IllegalCommandArgumentException {
    if (wordsOfInput[i].equals(";")) {
      if (index == wordsOfInput.length) {
        index = i;
      }
      for (int n = start; n < index; n++) {
        if (wordsOfInput[n].indexOf("/") == 0) {
          name += wordsOfInput[n].substring(1);
        } else {
          name += wordsOfInput[n];
        }
        if (n < index-1) {
          name += " ";
        }
      }
      if (name.length() == 0 && parseType == 0) {
        throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_TASK_NAME,
            Constants.CommandParam.NAME);
      }
      names.add(name);
      name = "";
      start = i+1;                    // Move on to check on next name
      index = wordsOfInput.length;    // Reset end index to end of string
    }
  }

  public void checkForSubtask(int i) throws IllegalCommandArgumentException {
    if (wordsOfInput[i].indexOf('@') == 0) {
      if ((!containsStartDate || !containsStartTime) && index > i) {
        index = i;
        tpsIndex = i;
      }
      String id = wordsOfInput[i].substring(1);
      try {
        builder.subTaskOf(Integer.parseInt(id));
      } catch (NumberFormatException e) {
        throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_SUBTASK,
            Constants.CommandParam.SUBTASKOF);
      }
    }
  }

  public void checkForTag(int i) {
    if (wordsOfInput[i].indexOf('#') == 0) {
      if ((!containsStartDate || !containsStartTime) && index > i) {
        index = i;
        tpsIndex = i;
      }
      String tag = wordsOfInput[i].substring(1);
      tags.add(tag);
    }
  }

  public void checkForProject(int i) {
    if (wordsOfInput[i].indexOf('$') == 0) {
      if ((!containsStartDate || !containsStartTime) && index > i) {
        index = i;
        tpsIndex = i;
      }
      String project = wordsOfInput[i].substring(1);
      builder.project(project);
    }
  }

  public void checkForPriority(int i) throws IllegalCommandArgumentException {
    if (wordsOfInput[i].indexOf("!") == 0) {
      if ((!containsStartDate || !containsStartTime) && index > i) {
        index = i;
        tpsIndex = i;
      }
      String priority = wordsOfInput[i].substring(1);
      if (priority.matches("h|high")) {
        builder.priority(Constants.PRIORITY_HIGH);
      } else if (priority.matches("m|mid|middle|medium")) {
        builder.priority(Constants.PRIORITY_MID);
      } else if (priority.matches("l|low")) {
        builder.priority(Constants.PRIORITY_LOW);
      } else {
        throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_PRIORITY,
            Constants.CommandParam.PRIORITY);
      }
    }
  }
  
  /**
   * Method that produces a Date string based on day input by user
   * 
   * @param dayInput    Day of week input by user. Eg. Mon(day), Tue(sday), etc.
   * @param plus        Amount to add onto the date. 1 for next day, 2 week, 3 month & 4 for year.
   */
  public void produceDateFromDay(String dayInput, int plus) {
    LocalDate date = null;
    
    if (plus == 2) {
      date = LocalDate.now().plusWeeks(1);
    } else if (plus == 3) {
      date = LocalDate.now().plusMonths(1);
    } else if (plus == 4) {
      date = LocalDate.now().plusYears(1);
    } else {
      for (int n = 0; n < Constants.DAYS_LIST.length; n++) {
        if (dayInput.toLowerCase().matches(Constants.DAYS_LIST[n])) {
          int days = (n+1) - LocalDate.now().getDayOfWeek().getValue();

          if (days >= 0) {
            if (plus == 1) {
              date = LocalDate.now().plusDays(days+7);
            } else {
              date = LocalDate.now().plusDays(days);
            }
          } else {
            date = LocalDate.now().plusDays(days+7);
          }

          break;
        }
      }
    }
    startDate = date.getDayOfMonth() + "/" + date.getMonthValue() + "/" +date.getYear();
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
          Constants.FEEDBACK_INVALID_DATE, Constants.CommandParam.DATETIME);
    }
  }
  
  /**
   * Method that checks if start date exists on the calendar.
   * 
   * @param endDate         End date in dd/mm/yyyy format.
   * @param dateTime        DateTime object created from startDate.
   * @throws                IllegalCommandArgumentException
   */
  public void checkEndDate(String endDate, DateTime dateTime)
      throws IllegalCommandArgumentException {
    int startDay = Integer.parseInt(endDate.split(dateOperator)[0]);
    if (dateTime.getEndDate().getDayOfMonth() != startDay) {
      throw new IllegalCommandArgumentException(
          Constants.FEEDBACK_INVALID_DATE, Constants.CommandParam.DATETIME);
    }
  }
  
}
