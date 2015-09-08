package raijin.storage.handler;

import java.util.Vector;

import raijin.common.datatypes.SimpleTask;

public abstract class BaseStorage {
  
  public abstract Vector<SimpleTask> getJSONFromFile(String json);
  public abstract void writeTasksToFile(Vector<SimpleTask> memory);

}
