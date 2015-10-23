package raijin.common.utils.filter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.TaskUtils;
import raijin.storage.api.TasksManager;

public class DateFilter extends TaskFilter {

  private DateTime limit;

  public DateFilter(List<Task> tasks, DateTime limit) {
    inputTasks = tasks;
    this.limit = limit;
  }

  public DateFilter(List<Task> tasks) {
    inputTasks = tasks;
  }

  public void setDateTime(DateTime dateTime) {
    limit = dateTime;
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    tasks = TaskUtils.getOnlyNormalTasks(tasks);
    return tasks.stream().filter(task -> isMatched(task.getDateTime())).
        collect(Collectors.toList());
  }
  
  /**
   * Returns commonly used view 
   * @param tasks
   * @param view
   * @return
   */
  public List<Task> filter(List<Task> tasks, Constants.View view) {
    limit = view.getDateTime();
    tasks = TaskUtils.getOnlyNormalTasks(tasks);
    return tasks.stream().filter(task -> isMatched(task.getDateTime())).
        collect(Collectors.toList());
  }

  boolean isMatched(DateTime target) {
    //To return all pending tasks
    if (limit == null) {
      return true;
    } else if (limit.getStartTime() == null) {
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
