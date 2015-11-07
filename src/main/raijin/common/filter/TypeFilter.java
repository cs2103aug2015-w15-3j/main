//@@author A0112213E

package raijin.common.filter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

/**
 * Filter by type of task
 * @author papa
 *
 */
public class TypeFilter extends TaskFilter {

  private Constants.TYPE_TASK limit;                                            //type of task to be filtered
  
  public TypeFilter(Constants.TYPE_TASK limit) {
    this.limit = limit;             
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    if (limit == Constants.TYPE_TASK.OVERDUE) {
      return getOverdueTasks(tasks);
    }
    return getFloatingTasks(tasks);
  }
  
  List<Task> getOverdueTasks(List<Task> tasks) {
    /*current date and time used for comparison*/
    DateTime current = new DateTime(LocalDate.now(), null, LocalDate.now(), 
        LocalTime.now());

    List<Task> result = tasks.stream().filter(t -> t.getType() != Constants.TYPE_TASK.FLOATING
        && t.getDateTime().compareTo(current) < 0)
        .collect(Collectors.toList());
    return result;
  }

  List<Task> getFloatingTasks(List<Task> tasks) {
    List<Task> floating = tasks.stream().filter(t -> t.getType() == limit).collect(Collectors
        .toList());
    return new SortFilter(Constants.SORT_CRITERIA.PRIORITY).filter(floating);
  }

}
