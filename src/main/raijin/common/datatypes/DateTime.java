package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTime {

  private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE);
  private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_TIME);
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;
  
  public DateTime(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public DateTime(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /*Create deadline with specific date*/
  public DateTime(String startDate){
    this.startDate = LocalDate.parse(startDate, dateFormatter);
    this.endDate = LocalDate.parse(startDate, dateFormatter);   //When no end date provided, same as start date
    this.startTime = LocalTime.of(23,59);                       //Default to 2359 when no time specified
  }

  /*Create deadline with specific date and time*/
  public DateTime(String startDate, String startTime){
    this.startDate = LocalDate.parse(startDate, dateFormatter);
    this.endDate = LocalDate.parse(startDate, dateFormatter);   //When no end date provided, same as start date
    this.startTime = LocalTime.parse(startTime, timeFormatter);               
  }

  //@TODO must provide date input 
  /*Create event deadline*/
  public DateTime(String startDate, String startTime, String endTime){
    this.startDate = LocalDate.parse(startDate, dateFormatter);
    this.endDate = LocalDate.parse(startDate, dateFormatter);   
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

  boolean compare(Object source, Object target) {
    if (source == null) {
      return target == null;
    } else {
      return source.equals(target);
    }
  }

}
