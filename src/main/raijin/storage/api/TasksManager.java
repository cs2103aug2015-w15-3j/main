package raijin.storage.api;

import java.util.HashMap;

import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;
import raijin.logic.api.UndoableRedoable;

/**
 * 
 * @author papa
 */
public class TasksManager {

  private static TasksManager tasksManager = new TasksManager();
  private TasksMap tasksMap;                    
  private History history;

  private TasksManager() {
    tasksMap = new TasksMap();
    history = History.getHistory();
  }

  /*Returns the single instace of Memory*/
  public static TasksManager getManager() {
    return tasksManager;
  }
  
  /*Set tasksMap from external source*/
  public void setTasksMap(TasksMap tasksMap) {
    this.tasksMap = tasksMap;
  }
  
  public TasksMap getTasksMap() {
    return tasksMap;
  }

  public void clearTasks() {
    tasksMap.clearTasks();
  }

  public boolean isEmptyTasks() {
    return tasksMap.isEmptyTasks();
  }

  //===========================================================================
  // Command related functions
  //===========================================================================

  public void addToHistory(UndoableRedoable commandRunner) {
    history.addCommand(commandRunner);
  }

  public void addTask(Task task) {
    tasksMap.addTask(task);
  }

  public void addCompletedTask(Task task) {
    tasksMap.addCompletedTask(task);
  }

  /*used for editing of tasks*/
  public Task getTask(int id) {
    return tasksMap.getTask(id);
  }

  public void deleteTask(int id) {
    tasksMap.deleteTask(id);
  }
  
  public void undo() {
    history.undo();
  }
  
  public void redo() {
    history.redo();
  }

  public HashMap<Integer, Task> getCompletedTasks() {
    return tasksMap.getCompletedTasks();
  }

  public HashMap<Integer, Task> getTasks() {
    return tasksMap.getTasks();
  }

  //===========================================================================
  // Utility functions
  //===========================================================================

  public String printTasksMap() {
    return tasksMap.toString();
  }

  
}
