//@@author A0112213E

package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import raijin.common.utils.filter.DateFilter;

/**
 * Shows occupied time slot for that particular date
 * @author papa
 *
 */
public class TimeSlot {
  
  private static final LocalTime DEFAULT_START_TIME = LocalTime.of(5, 0);
  private static final LocalTime DEFAULT_END_TIME = LocalTime.of(11, 59);
  private Comparator<DateTime> durationComparator = (d1, d2) -> 
    (int) (getDuration(d2.getStartTime(), d2.getEndTime()) 
    - getDuration(d1.getStartTime(), d1.getEndTime()));

  private DateTime dateTime;
  private DateFilter dateFilter;
  private List<Task> events;               //Events scheduled for this date
  private List<DateTime> occupiedSlots;               //Stores occupied time slots

  public TimeSlot(LocalDate endDate, List<Task> pendingTasks) {
    this.dateTime = new DateTime(endDate, endDate);
    dateFilter = new DateFilter(pendingTasks, dateTime);
    events = filterEvents(pendingTasks);
    occupiedSlots = events.stream().map(t -> t.getDateTime()).collect(
        Collectors.toList());
    Collections.sort(occupiedSlots, durationComparator);
    streamlineEvents();
  }

  List<Task> filterEvents(List<Task> pendingTasks) {
    List<Task> result = dateFilter.filter(pendingTasks).stream().filter(
        t -> t.getType().equals(Constants.TYPE_TASK.EVENT) 
        && t.getDateTime().getStartDate().equals(
           t.getDateTime().getEndDate())
        && t.getDateTime().getStartTime() != null).collect(Collectors.toList());

    return result;
  }

  public List<Task> getEvents() {
    return events;
  }
  
  public List<DateTime> getOccupiedSlots() {
    return occupiedSlots;
  }

  List<DateTime> streamlineEvents() {
    List<DateTime> filtered = new ArrayList<DateTime>();
    
    for (DateTime source : occupiedSlots) {
      if (source != null && occupiedSlots.indexOf(source)
          != occupiedSlots.size() -1) {
        filtered.add(iterativeExtendDuration(source));
      }
    }

    return filtered;
  }

  long getDuration(LocalTime start, LocalTime end) {
    long minute = start.until(end, ChronoUnit.MINUTES);
    return minute;
  }


  DateTime iterativeExtendDuration(DateTime source) {
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

  //Extends duration of source if overlap occur with target
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

  //Check if there is any overlap between two events
  boolean isNoOverlap(int compareStartWithEnd, int compareEndWithStart) {
    return compareStartWithEnd > 0 || compareEndWithStart < 0;
  }
  
  //Checks if the event lies within another event
  boolean isWithinDuration(int compareStartWithStart, int compareEndWithEnd) {
    return compareStartWithStart <= 0 && compareEndWithEnd >= 0;
  }

}
