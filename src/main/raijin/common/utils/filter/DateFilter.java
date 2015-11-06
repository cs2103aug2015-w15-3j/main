//@@author A0112213E

package raijin.common.utils.filter;

import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.TaskUtils;

/**
 * Filter tasks based on dates 
 * @author papa
 *
 */
public class DateFilter extends TaskFilter {

  /*Date and time used to filter tasks provided*/
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

  boolean isMatchedDate(DateTime target) {
    if (limit.getStartDate() == null) {
      /*compares specific deadline*/
      return limit.getEndDate().equals(target.getEndDate());
    } else {
      /*compare for bounded period of date*/
      return target.getStartDate() != null 
          && limit.getStartDate().compareTo(target.getStartDate()) <= 0
          && limit.getEndDate().compareTo(target.getEndDate()) >= 0;
    }
  }
  
  boolean isMatchedTime(DateTime target) {
    if (limit.getStartTime() == null) {
      /*compares specific time*/ 
      return limit.getEndTime().equals(target.getEndTime());
    } else {
      /*compare for bounded period of time*/
      return target.getStartTime() != null 
          && limit.getStartTime().compareTo(target.getStartTime()) <= 0
          && limit.getEndTime().compareTo(target.getEndTime()) >= 0;
    }
  }

  boolean isMatched(DateTime target) {
    if (limit == null) {               //If no limit provided, returns all tasks
      return true;
    } else if (limit.getEndTime() == null) {
      return isMatchedDate(target);
    } else if (limit.getEndDate() == null) {
      return isMatchedTime(target);
    } else {
      return isMatchedDate(target) && isMatchedTime(target);
    }
  }

  boolean isMatchedFutureTasks(DateTime target) {
    return target.getEndDate().compareTo(limit.getStartDate()) >= 0;
  }
  
  /**
   * Returns MAX number of tasks after tomorrow 
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
   * Filter tasks based on common dates such as tomorrow and today
   * @param tasks
   * @param view
   * @return tasks filtered with given view
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

}
