package raijin.storage.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

public class TasksManagerTest {

  private TasksManager tasksManager;

  @Before
  public void setUp() throws Exception {
    tasksManager = TasksManager.getManager();
  }

  @Test
  public void testOnlySingleManagerInstance() {
    TasksManager tasksManagerNew = TasksManager.getManager();
    assertEquals(tasksManager, tasksManagerNew);
  }


}
