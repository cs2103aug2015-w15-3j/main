package raijin.storage.handler;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.SimpleDateTime;
import raijin.common.datatypes.SimpleTask;
import raijin.storage.handler.SimpleStorage;

public class SimpleStorageTest {

  private SimpleStorage storage;
  private static final String filePath = "/tmp";

  @Before
  public void setUp() throws Exception {

    this.storage = new SimpleStorage(filePath);
  }

  @Test
  public void checkVectorToJSON() {
    Vector<SimpleTask> memory = new Vector<SimpleTask>();
    memory.add(new SimpleTask(1, "me", new SimpleDateTime()));
    memory.add(new SimpleTask(2, "you", new SimpleDateTime()));
    memory.add(new SimpleTask(3, "you", new SimpleDateTime()));
    storage.writeTasksToFile(memory);
  }

  @Test
  public void checkVectorFromJSON() {
    Vector<SimpleTask> memory = storage.getJSONFromFile(filePath);
  }

}
