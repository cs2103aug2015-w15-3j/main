package raijin.common.utils.filter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.RaijinLogger;
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
    if (limit == null) {
      return inputTasks;
    }
    tasks = TaskUtils.getOnlyNormalTasks(tasks);
    if (limit.getEndDate() == null) {
      return getFutureTasks(tasks, Constants.MAX_TASKS);
    }
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
    if (view == Constants.View.FUTURE) {
      return getFutureTasks(tasks, Constants.MAX_TASKS);
    }
    return tasks.stream().filter(task -> isMatched(task.getDateTime())).
        collect(Collectors.toList());
  }

  boolean isMatched(DateTime target) {
    //To return all pending tasks
    if (limit == null) {
      return true;
    } else if (limit.getEndTime() == null) {
      return isMatchedDate(target);
    } else if (limit.getEndDate() == null) {
      return isMatchedTime(target);
    } else {
      return isMatchedDate(target) && isMatchedTime(target);
    }
  }

  boolean isMatchedDate(DateTime target) {
    if (limit.getStartDate() == null) {
      //Compares specific date
      return limit.getEndDate().equals(target.getEndDate());
    } else {
      //Compare for bounded period of date
      return target.getStartDate() != null 
          && limit.getStartDate().compareTo(target.getStartDate()) <= 0
          && limit.getEndDate().compareTo(target.getEndDate()) >= 0;
    }
  }
  
  boolean isMatchedTime(DateTime target) {
    if (limit.getStartTime() == null) {
      //Compares specific time 
      return limit.getEndTime().equals(target.getEndTime());
    } else {
      //Compare for bounded period of time
      return target.getStartTime() != null 
          && limit.getStartTime().compareTo(target.getStartTime()) <= 0
          && limit.getEndTime().compareTo(target.getEndTime()) >= 0;
    }
  }
  
  boolean isMatchedFutureTasks(DateTime target) {
    return target.getEndDate().compareTo(limit.getStartDate()) >= 0;
  }
  
  /**
   * Returns N number of tasks after tomorrow 
   * @param pendingTasks
   * @return
   */
  List<Task> getFutureTasks(List<Task> pendingTasks, int MAX) {
     List<Task> result = pendingTasks.stream().filter(t -> isMatchedFutureTasks(
         t.getDateTime())).collect(Collectors.toList());
     if (result.size() > MAX) {
       return result.subList(0, MAX);
     } else {
       return result;
     }
  }
}
