//@@author A0112213E

package raijin.logic.realtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.datatypes.Constants.TYPE_TASK;
import raijin.common.filter.DateFilter;

/**
 * Shows occupied time slot for a given date
 * @author papa
 *
 */
public class TimeSlot {
  
  /*Compares duration of a task*/
  private Comparator<DateTime> durationComparator = (d1, d2) -> 
    (int) (getDuration(d2.getStartTime(), d2.getEndTime()) 
    - getDuration(d1.getStartTime(), d1.getEndTime()));

  private DateTime dateTime;
  private DateFilter dateFilter;
  private List<Task> events;                                                    //list of events on given date 
  private List<DateTime> occupiedSlots;                                         //list of occupied time slots

  public TimeSlot(LocalDate endDate, List<Task> pendingTasks) {
    this.dateTime = new DateTime(endDate, endDate);
    dateFilter = new DateFilter(pendingTasks, dateTime);
    events = filterEvents(pendingTasks);
    occupiedSlots = events.stream().map(t -> t.getDateTime()).collect(
        Collectors.toList());
    /*sort time slots by duration*/
    Collections.sort(occupiedSlots, durationComparator);
    occupiedSlots = combineAllTimeSlots();
  }

  long getDuration(LocalTime start, LocalTime end) {
    long minute = start.until(end, ChronoUnit.MINUTES);
    return minute;
  }

  /**
   * Filters tasks for event that spans only for one day 
   * @param pendingTasks
   * @return result
   */
  List<Task> filterEvents(List<Task> pendingTasks) {
    List<Task> result = dateFilter.filter(pendingTasks).stream().filter(
        t -> t.getType().equals(Constants.TYPE_TASK.EVENT) 
        && t.getDateTime().getStartDate().equals(
           t.getDateTime().getEndDate())
        && t.getDateTime().getStartTime() != null).collect(Collectors.toList());

    return result;
  }

  /**
   * Merge two time slots based on result of comparing start time and end time
   * @param compareStartWithStart
   * @param compareEndWithEnd
   * @param source
   * @param target
   * @return
   */
  DateTime handleOverlapEvents(int compareStartWithStart, int compareEndWithEnd,
      DateTime source, DateTime target) {
    LocalTime startTime = source.getStartTime();
    LocalTime endTime = source.getEndTime();

    if (compareStartWithStart > 0) {
      startTime = target.getStartTime();
    } 

    if (compareEndWithEnd < 0) {
      endTime = target.getEndTime();
    }
    return new DateTime(startTime, endTime);
  }

  /*Check if there is any overlap between two events*/
  boolean isNoOverlap(int compareStartWithEnd, int compareEndWithStart) {
    return compareStartWithEnd > 0 || compareEndWithStart < 0;
  }
  
  /*Checks if the event lies within another event*/
  boolean isWithinDuration(int compareStartWithStart, int compareEndWithEnd) {
    return compareStartWithStart <= 0 && compareEndWithEnd >= 0;
  }

  /*Merge overlap two time slots into one single slot*/
  DateTime extendDuration(DateTime source, DateTime target) {

    int compareStartWithEnd = source.getStartTime().compareTo(target.getEndTime());
    int compareEndWithStart = source.getEndTime().compareTo(target.getStartTime());
    int compareStartWithStart = source.getStartTime().compareTo(target.getStartTime());
    int compareEndWithEnd = source.getEndTime().compareTo(target.getEndTime());

    if (isNoOverlap(compareStartWithEnd, compareEndWithStart)) {
      return source;
    } else if (isWithinDuration(compareStartWithStart, compareEndWithEnd)) {
      occupiedSlots.set(occupiedSlots.indexOf(target), null);
      return source;
    } else {
      occupiedSlots.set(occupiedSlots.indexOf(target), null);
      return handleOverlapEvents(compareStartWithStart, compareEndWithEnd, source, target);
    }
  }

  /*Merge iteratively with other time slots*/
  DateTime consolidateTimeSlots(DateTime source) {
    DateTime result = source;
    int index = occupiedSlots.indexOf(source);

    for (int i = index + 1 ; i < occupiedSlots.size() ; i++) {
      DateTime target = occupiedSlots.get(i);
      if (target != null) {
        result = extendDuration(result, target);
      }
    }
    return result;
  }

  /*Merge all occupied time slots*/
  List<DateTime> combineAllTimeSlots() {
    if (occupiedSlots.size() > 1) {
      List<DateTime> filtered = new ArrayList<DateTime>();

      for (DateTime source : occupiedSlots) {
        if (source != null && occupiedSlots.indexOf(source) != occupiedSlots.size() - 1) {
          filtered.add(consolidateTimeSlots(source));
        }
      }
      return filtered;
    }
    return occupiedSlots;
  }

  public List<Task> getEvents() {
    return events;
  }
  
  public List<DateTime> getOccupiedSlots() {
    return occupiedSlots;
  }


}
