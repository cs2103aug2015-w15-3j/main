// @@author A0112213E

package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTime implements Comparable<DateTime> {

  private static DateTimeFormatter dateFormatter = DateTimeFormatter
      .ofPattern(Constants.FORMAT_DATE);
  private static DateTimeFormatter timeFormatter = DateTimeFormatter
      .ofPattern(Constants.FORMAT_TIME);

  private LocalDate startDate;
  private LocalDate endDate;
  private LocalTime startTime;
  private LocalTime endTime;

  // =========================
  // Constructors for testing
  // =========================

  public DateTime(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public DateTime(LocalTime startTime, LocalTime endTime) {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public DateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
  }


  // ============================
  // Constructors used by Parser
  // ============================

  /**
   * Constructs deadline when only end date is specified
   * @param endDate
   */
  public DateTime(String endDate) {
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);
    /* Set time to 2359 when not specified by user */
    this.endTime = LocalTime.of(23, 59);
  }

  /**
   * Constructs deadline when both time and date is specified
   * @param endDate
   * @param endTime
   */
  public DateTime(String endDate, String endTime) {
    /* Set start date to end date to avoid deadling with null */
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);
    this.endTime = LocalTime.parse(endTime, timeFormatter);
  }

  /**
   * Constructs deadline for task that spans certain duration on the same day
   * @param endDate
   * @param startTime
   * @param endTime
   */
  public DateTime(String endDate, String startTime, String endTime) {
    this.startDate = LocalDate.parse(endDate, dateFormatter);
    this.endDate = LocalDate.parse(endDate, dateFormatter);
    this.startTime = LocalTime.parse(startTime, timeFormatter);
    this.endTime = LocalTime.parse(endTime, timeFormatter);
  }

  /* Creates event that spans multiple days */
  public DateTime(String startDate, String startTime, String endDate, String endTime) {
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

  /**
   * Handles comparison of null fields
   * @param source 
   * @param target
   * @return
   */
  boolean compare(Object source, Object target) {
    if (source == null) {
      return target == null;
    } else {
      return source.equals(target);
    }
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
   * Compare time when both shared the same end time.
   * @param source
   * @param target
   * @return
   */
  int compareStartTime(LocalTime source, LocalTime target) {
    if (source == null && target == null) {
      return 0;
    } else if (source == null) {
      return 1;
    } else if (target == null) {
      return -1;
    } else {
      return source.compareTo(target);
    }
  }

  /**
   * Comparator for DateTime objects
   * 
   * @param DateTime compared
   * @return
   */

  @Override
  public int compareTo(DateTime compared) {
    int result = getEndDate().compareTo(compared.getEndDate());

    // When both end dates are different
    if (result != 0) {
      return result;
    } else {
      result = getStartDate().compareTo(compared.getStartDate());

      // When both start dates are different
      if (result != 0) {
        return result;
      } else {
        result = getEndTime().compareTo(compared.getEndTime());

        // When both end times are different
        if (result != 0) {
          return result;
        } else {
          return compareStartTime(getStartTime(), compared.getStartTime());
        }
      }
    }


  }


}
