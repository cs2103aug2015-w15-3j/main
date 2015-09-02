package raijin.component.storage;

import java.util.Vector;

import raijin.util.Task.SimpleTask;

public abstract class BaseStorage {
  
  public abstract Vector<SimpleTask> getJSONFromFile(String json);
  public abstract void writeTasksToFile(Vector<SimpleTask> memory);

}
