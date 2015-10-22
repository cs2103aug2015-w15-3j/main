package raijin.common.utils.filter;

import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

public class DateFilter extends TaskFilter {

  private DateTime limit;

  public DateFilter(List<Task> tasks, DateTime limit) {
    inputTasks = tasks;
    this.limit = limit;
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    return tasks.stream().filter(task -> isMatched(task.getDateTime())).
        collect(Collectors.toList());
  }
  
  boolean isMatched(DateTime target) {
    if (limit.getStartTime() == null) {
      return isMatchedDate(target);
    } else if (limit.getStartDate() == null) {
      return isMatchedTime(target);
    } else {
      return isMatchedDate(target) && isMatchedTime(target);
    }
  }

  boolean isMatchedDate(DateTime target) {
    if (limit.getEndDate() == null) {
      //Compares start of deadline date
      return limit.getStartDate().equals(target.getStartDate());
    } else {
      //Compare for bounded period of date
      return target.getEndDate() != null 
          && limit.getStartDate().compareTo(target.getStartDate()) <= 0
          && limit.getEndDate().compareTo(target.getEndDate()) >= 0;
    }
  }
  
  boolean isMatchedTime(DateTime target) {
    if (limit.getEndTime() == null) {
      //Compares start of deadline time
      return limit.getStartTime().equals(target.getStartTime());
    } else {
      //Compare for bounded period of time
      return target.getEndTime() != null 
          && limit.getStartTime().compareTo(target.getStartTime()) <= 0
          && limit.getEndTime().compareTo(target.getEndTime()) >= 0;
    }
  }

}
