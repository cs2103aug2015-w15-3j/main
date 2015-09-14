package raijin.storage.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;

public class TasksMap {

  protected HashMap<Integer, Task> tasks;
  protected IDManager idManager;
  
  public TasksMap() {
    tasks = new HashMap<Integer, Task>();   //Stores current tasks
    idManager = IDManager.getIdManager();   //Stores available idPool
  }
  
  void addTask(Task task) {
    tasks.put(task.getId(), task);
  }

  //@TODO implements more specific exception so that commandRunner can catch
  Task getTask(int id) {
    if (tasks.containsKey(id)) {
      return tasks.get(id);
    } else {
      throw new IllegalArgumentException();
    }
  }

  void deleteTask(int id) {
    tasks.remove(id);
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Iterator iter = tasks.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry pair = (Entry) iter.next();
      sb.append(((Task) pair.getValue()).getName()+"\n");
    }
    return sb.toString();
  }
}
