//@@author A0112213E

package raijin.common.filter;

import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Task;

/**
 * Filter tasks based on priority
 * @author papa
 *
 */
public class PriorityFilter extends TaskFilter {

  private String priority;
  
  public PriorityFilter(String priority) {
    this.priority = priority;
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    return tasks.stream().filter(task -> task.getPriority().equals(priority)).
        collect(Collectors.toList());
  }

}
