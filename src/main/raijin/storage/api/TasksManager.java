package raijin.storage.api;

import java.util.HashMap;

import raijin.common.datatypes.DisplayContainer;
import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.utils.IDManager;

/**
 * 
 * @author papa
 */
public class TasksManager {

  private static TasksManager tasksManager;
  private HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
  private HashMap<Integer, Task> completedTasks = new HashMap<Integer, Task>();
  private DisplayContainer displayedTasks = new ListDisplayContainer();

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
    IDManager.getIdManager().returnId(task.getId());
  }

  public Task getPendingTask(int id) throws NoSuchTaskException {
    int taskId = displayedTasks.isEmpty() ? id : displayedTasks.getRealId(id);
    handleUnknownTask(pendingTasks, taskId);
    return pendingTasks.get(taskId);
  }

  public void deletePendingTask(int id) throws NoSuchTaskException {
    handleUnknownTask(pendingTasks, id);
    pendingTasks.remove(id);
    IDManager.getIdManager().returnId(id);
  }
  
  public void deleteCompletedTask(int id) throws NoSuchTaskException {
    handleUnknownTask(completedTasks, id);
    completedTasks.remove(id);
  }
  
  void handleUnknownTask(HashMap<Integer, Task> tasks, int id) throws NoSuchTaskException {
    if (!tasks.containsKey(id)) {
      throw new NoSuchTaskException("Task ID does not match", id);
    }
  }
  
}
