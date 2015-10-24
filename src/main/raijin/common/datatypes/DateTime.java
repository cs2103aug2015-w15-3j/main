package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTime implements Comparable<DateTime> {

  private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE);
  private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_TIME);
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  
  //===========================================================================
  // Uses Java Time API for construction
  //===========================================================================
  
  public DateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, 
      LocalTime endTime) {
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
  }

  public DateTime(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public DateTime(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  //===========================================================================
  // Specific deadlin
  //===========================================================================

  /*Create deadline with specific date*/
  public DateTime(String endDate){
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);   //When no end date provided, same as start date
    this.endTime = LocalTime.of(23,59);                       //Default to 2359 when no time specified
  }

  /*Create deadline with specific date and time*/
  public DateTime(String endDate, String endTime){
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);   //When no end date provided, same as start date
    this.endTime = LocalTime.parse(endTime, timeFormatter);               
  }

  //===========================================================================
  // Event
  //===========================================================================

  /*Create event deadline*/
  public DateTime(String endDate, String startTime, String endTime){
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);   
    this.startTime = LocalTime.parse(startTime, timeFormatter);               
    this.endTime = LocalTime.parse(endTime, timeFormatter);               
  }

  /*Creates event that spans multiple days*/
  public DateTime(String startDate, String startTime, 
      String endDate, String endTime){
    this.startDate = LocalDate.parse(startDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);   
    this.startTime = LocalTime.parse(startTime, timeFormatter);               
    this.endTime = LocalTime.parse(endTime, timeFormatter);               
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalTime getStartTime() {
    return startTime;
  }
  
  @Override
  public boolean equals(Object ob2) {
    if (ob2 instanceof DateTime) {
      return compare(startDate, ((DateTime) ob2).getStartDate())
          && compare(endDate, ((DateTime) ob2).getEndDate()) 
          && compare(startTime, ((DateTime) ob2).getStartTime())
          && compare(endTime, ((DateTime) ob2).getEndTime());
    }
    return false;
  }


  /**
   * Compare only start date, if same start date is the same
   * Compare time using the same manner 
   * Compare end date, null is sorted first  
   * @param DateTime compared
   * @return
   */

  @Override
  public int compareTo(DateTime compared) {
    int result = getEndDate().compareTo(compared.getEndDate());

    //When both end dates are different
    if (result != 0) {
      return result;
    } else {
      result = compareStartDate(getStartDate(), compared.getStartDate());
      
      //When both start dates are different
      if (result != 0) {
        return result;
      } else {
        result = getEndTime().compareTo(compared.getEndTime());
        
        //When both start times are different 
        if (result != 0) {
          return result;
        } else {
          return compareStartTime(getStartTime(), compared.getStartTime());
        }
      }
    }
      
    
  }

  boolean compare(Object source, Object target) {
    if (source == null) {
      return target == null;
    } else {
      return source.equals(target);
    }
  }
  
  /**
   * Compare time when both shared the same start time
   * @param source
   * @param target
   * @return
   */
  int compareStartTime(LocalTime source, LocalTime target) {
    if (source == null && target == null) {
      return 0;
    } else if (source == null) {
      return -1;
    } else if (target == null) {
      return 1;
    } else {
      return source.compareTo(target);
    }
  }

  /**
   * Compare date when both shared the same start date 
   * @param source
   * @param target
   * @return
   */
  int compareStartDate(LocalDate source, LocalDate target) {
    if (source == null && target == null) {
      return 0;
    } else if (source == null) {
      return -1;
    } else if (target == null) {
      return 1;
    } else {
      return source.compareTo(target);
    }
  }

}
