package raijin.component.storage;

import java.util.Vector;

import raijin.util.Task.SimpleTask;

public abstract class BaseStorage {
  
  private Vector<SimpleTask> memory;
  public abstract Vector<SimpleTask> getTasksFromFile();
  public abstract Vector<SimpleTask> writeTasksToFile();

}
