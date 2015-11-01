package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
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
  
  List<DateTime> getOccupiedSlots() {
    return occupiedSlots;
  }

  long getDuration(LocalTime start, LocalTime end) {
    long minute = start.until(end, ChronoUnit.MINUTES);
    return minute;
  }

}
