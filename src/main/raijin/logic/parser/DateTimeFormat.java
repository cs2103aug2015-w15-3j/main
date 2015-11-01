/**
 * Class that takes care of formatting date to dd/mm/yyyy format and time to 24hrs format.
 * 
 * @author LingJie
 */
package raijin.logic.parser;

import java.text.DecimalFormat;
import java.time.LocalDate;

import raijin.common.datatypes.Constants;

public class DateTimeFormat {
  public DateTimeFormat() {
    
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
    String[] dayMonth = date.split(Constants.DATE_OPERATOR);
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
   * Checks whether it is 12/24hr formatting before proceeding.
   * 
   * @param time    String of time input by user.
   * @return        The proper time format after formatting the string input.     
   */
  public String formatTime(String time) {
    if (time.matches(Constants.TIME12_PATTERN)) {
      return format12HourTime(time);
      
    } else if (time.length() == 3) {
      if (time.charAt(0) == '0') {
        // Assuming the String time starts with "00" as it passed the format matching already.
        time += "0";
      } else {
        time = "0" + time;
      }
    }

    return time;
  }
  
  
  public String format12HourTime(String time) {
    int oprIndex = time.indexOf(":") > 0 ? time.indexOf(":") : time.indexOf(".");
    boolean isPM = time.contains("p");
    
    if (oprIndex == -1) {
      int periodIndex = time.indexOf("p") > 0 ? time.indexOf("p") : time.indexOf("a");
      if (periodIndex == 1) {
        if (isPM) {
          time = Integer.parseInt(time.substring(0,1))+12 + "00";
        } else {
          time = "0" + time.charAt(0) + "00";
        }
      } else {
        if (isPM) {
          time = Integer.parseInt(time.substring(0,2))+12 + "00";
        } else {
          time = time.substring(0,2) + "00";
        }
      }
    } else if (oprIndex == 1) {
      time = "0" + time.charAt(0) + time.substring(2,4);
    } else if (oprIndex == 2) {
      if (isPM) {
        time = Integer.parseInt(time.substring(0,2))+12 + time.substring(3,5);
      } else {
        time = time.substring(0,2) + time.substring(3,5);
      }
    }
    
    return time;
  }
}