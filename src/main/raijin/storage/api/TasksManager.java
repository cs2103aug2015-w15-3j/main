package raijin.storage.api;

import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.api.UndoableRedoable;

/**
 * 
 * @author papa
 */
public class TasksManager {

  private static TasksManager tasksManager;
  private HashMap<Integer, Task> pendingTasks;
  private HashMap<Integer, Task> completedTasks;
  private History history;

  private TasksManager() {}

  public static TasksManager getManager(HashMap<Integer, Task> pendingTasks) {
    if (tasksManager == null) {
      tasksManager = new TasksManager();
    }
    return tasksManager;
  }
  
  public void setPendingTasks(HashMap<Integer, Task> pendingTasks) {
    this.pendingTasks = pendingTasks;
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
    try {
      return pendingTasks.get(id);
    } catch (NullPointerException e) {
      throw new NonExistentTaskException(String.format(Constants.EXCEPTION_NONEXISTENTTASK, id));
    }
  }

  public void deletePendingTask(int id) throws NonExistentTaskException {
    try {
      pendingTasks.remove(id);
    } catch (NullPointerException e) {
      throw new NonExistentTaskException(String.format(Constants.EXCEPTION_NONEXISTENTTASK, id));
    }
  }
  

  
}
