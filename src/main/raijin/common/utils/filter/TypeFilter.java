package raijin.common.utils.filter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

public class TypeFilter extends TaskFilter {

  private Constants.TYPE_TASK limit;        //Limiting criteria for filter
  
  public TypeFilter(Constants.TYPE_TASK limit) {
    this.limit = limit;
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    switch (limit) {
      
      //Uses a date filter when overdue is specified
      case OVERDUE:
        return getOverdue(tasks);
        
      default:
        return getType(tasks);
    }
  }
  
  List<Task> getOverdue(List<Task> tasks) {
    //Generates date time object for comparison
    DateTime current = new DateTime(null, null, LocalDate.now(), LocalTime.now());

    return tasks.stream().filter(t -> t.getType() != Constants.TYPE_TASK.FLOATING
        && t.getDateTime().compareTo(current) < 0)
        .collect(Collectors.toList());
  }

  List<Task> getType(List<Task> tasks) {
    return tasks.stream().filter(t -> t.getType() == limit).collect(Collectors
        .toList());
  }

}
