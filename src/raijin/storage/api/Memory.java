package raijin.storage.api;

import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;
import raijin.logic.api.UndoableRedoable;

/**
 * 
 * @author papa
 */
public class Memory {

  private static Memory memory = new Memory();
  private TasksMap tasksMap;                    //Stores current tasks
  private History history;

  private Memory() {
    tasksMap = new TasksMap();
    history = History.getHistory();
  }

  /*Returns the single instace of Memory*/
  public static Memory getMemory() {
    return memory;
  }
  
  /*Set tasksMap from external source*/
  public void setTasksMap(TasksMap tasksMap) {
    this.tasksMap = tasksMap;
  }
  
  public TasksMap getTasksMap() {
    return tasksMap;
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

  //===========================================================================
  // Utility functions
  //===========================================================================

  public String printTasksMap() {
    return tasksMap.toString();
  }

  
}
