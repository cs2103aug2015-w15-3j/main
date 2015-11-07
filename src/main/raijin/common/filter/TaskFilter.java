//@@author A0112213E

package raijin.common.filter;

import java.util.List;

import raijin.common.datatypes.Task;

public abstract class TaskFilter {
  
  protected List<Task> inputTasks;
  
  protected List<Task> and(TaskFilter other) {
    return filter(other.filter(inputTasks));
  }
  public abstract List<Task> filter(List<Task> tasks);
  
}
