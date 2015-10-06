package raijin.storage.api;

import java.util.HashMap;
import java.util.List;

import raijin.common.datatypes.DisplayContainer;
import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.utils.EventBus;
import raijin.common.utils.IDManager;

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

  /*Sync pending and completed tasks to target tasksManager retrieved from Json file*/
  public void sync(TasksManager target) {
    //@TODO fix checking null
    if (null != target) {
      pendingTasks = target.getPendingTasks();
      completedTasks = target.getCompletedTasks();
    }
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
    pendingTasks.remove(task.getId());
  }

  public Task getPendingTask(int id) throws NoSuchTaskException {
    List<Task> displayedTasks = EventBus.getEventBus().getDisplayedTasks();
    int taskId = displayedTasks.isEmpty() ? id : displayedTasks.get(id-1).getId();
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
