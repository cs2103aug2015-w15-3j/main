package raijin.storage.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;

public class TasksMap {

  private HashMap<Integer, Task> tasks;
  //@TODO add a limiter to completed tasks
  private HashMap<Integer, Task> completed; //List of completed tasks
  private IDManager idManager;
  
  public TasksMap() {
    tasks = new HashMap<Integer, Task>();   //Stores current tasks
    idManager = IDManager.getIdManager();   //Stores available idPool
  }
  
  void addTask(Task task) {
    tasks.put(task.getId(), task);
  }

  void addCompletedTask(Task task) {
    completed.put(task.getId(), task);
  }

  //@TODO implements more specific exception so that commandRunner can catch
  Task getTask(int id) {
    if (tasks.containsKey(id)) {
      return tasks.get(id);
    } else {
      throw new IllegalArgumentException();
    }
  }

  HashMap<Integer, Task> getCompletedTasks() {
    return completed;
  }

  HashMap<Integer, Task> getTasks() {
    return tasks;
  }

  void deleteTask(int id) {
    tasks.remove(id);
  }
  
  public TreeSet<Integer> getIdPool() {
    return idManager.getIdPool();
  }
  
  // For DisplayController.java
  public ArrayList<Task> getPendingList() {
	  if (!tasks.isEmpty()) {
		  ArrayList<Task> list = new ArrayList<Task>(tasks.values());
		  return list;
	  } else {
		  ArrayList<Task> list = new ArrayList<Task>();
		  return list;
	  }
  }
  
  // For DisplayController.java
  public ArrayList<Task> getCompletedList() {
	  try {
	      if (!completed.isEmpty()) {
	          ArrayList<Task> list = new ArrayList<Task>(completed.values());
		      return list;
	      } else {
		      ArrayList<Task> list = new ArrayList<Task>();
		      return list;
	      }
	  } catch (NullPointerException e) {
		  ArrayList<Task> list = new ArrayList<Task>();
		  return list;
	  }
	  
  }
  

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    @SuppressWarnings("rawtypes")
    Iterator iter = tasks.entrySet().iterator();
    while (iter.hasNext()) {
      @SuppressWarnings("rawtypes")
      Map.Entry pair = (Entry) iter.next();
      sb.append(((Task) pair.getValue()).getId() + " "
          + ((Task) pair.getValue()).getName()+"\n");
    }
    return sb.toString();
  }
}
