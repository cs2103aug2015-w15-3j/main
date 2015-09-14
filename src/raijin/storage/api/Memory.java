package raijin.storage.api;

import java.util.ArrayList;
import java.util.List;

import raijin.common.datatypes.Task;

/**
 * 
 * @author papa
 */
public class Memory {

  private static Memory memory = new Memory();
  private TasksMap tasksMap;                    //Stores current tasks

  private Memory() {
    tasksMap = new TasksMap();
  }
  
  public static Memory getMemory() {
    return memory;
  }

  public void addTask(Task task) {
    tasksMap.addTask(task);
  }

  public Task getTask(int id) {
    return tasksMap.getTask(id);
  }

  public void deleteTask(int id) {
    tasksMap.deleteTask(id);
  }
  
  public String printTasksMap() {
    return tasksMap.toString();
  }

  //===========================================================================
  // Undo/Redo related methods
  //===========================================================================


  
}
