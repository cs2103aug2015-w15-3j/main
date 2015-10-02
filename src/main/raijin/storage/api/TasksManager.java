package raijin.storage.api;

import java.security.SecurityPermission;
import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;

/**
 * 
 * @author papa
 */
public class TasksManager {

  private static TasksManager tasksManager;
  private HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
  private HashMap<Integer, Task> completedTasks = new HashMap<Integer, Task>();

  private TasksManager() {}

  public static TasksManager getManager() {
    if (tasksManager == null) {
      tasksManager = new TasksManager();
    }
    return tasksManager;
  }
  
  public HashMap<Integer, Task> getPendingTasks() {
    return pendingTasks;
  }

  public void setPendingTasks(HashMap<Integer, Task> pendingTasks) {
    this.pendingTasks = pendingTasks;
  }
  
  public HashMap<Integer, Task> getCompletedTasks() {
    return completedTasks;
  }

  public void setCompletedTasks(HashMap<Integer, Task> completedTasks) {
    this.completedTasks = completedTasks;
  }

  public boolean isEmptyPendingTasks() {
    return pendingTasks.isEmpty();
  }

  public boolean isEmptyCompletedTasks() {
    return completedTasks.isEmpty();
  }

  //===========================================================================
  // Command related functions
  //===========================================================================

  public void addPendingTask(Task task) {
    pendingTasks.put(task.getId(), task);
  }

  public void addCompletedTask(Task task) {
    completedTasks.put(task.getId(), task);
  }

  public Task getPendingTask(int id) throws NonExistentTaskException {
    if (pendingTasks.containsKey(id)) {
      return pendingTasks.get(id);
    } else {
      throw new NonExistentTaskException(String.format(Constants.EXCEPTION_NONEXISTENTTASK, id));
    }
  }

  public void deletePendingTask(int id) throws NonExistentTaskException {
    if (pendingTasks.containsKey(id)) {
      pendingTasks.remove(id);
    } else {
      throw new NonExistentTaskException(String.format(Constants.EXCEPTION_NONEXISTENTTASK, id));
    }
  }
  
  public void deleteCompletedTask(int id) throws NonExistentTaskException {
	if (completedTasks.containsKey(id)) {
	  completedTasks.remove(id);
	} else {
	  throw new NonExistentTaskException(String.format(Constants.EXCEPTION_NONEXISTENTTASK, id));
	}
  }
  
}
