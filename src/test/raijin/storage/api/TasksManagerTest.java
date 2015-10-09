package raijin.storage.api;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;

public class TasksManagerTest {

  private TasksManager tasksManager;
  private static IDManager idManager;

  @BeforeClass
  public static void setUpClass() {
    idManager = IDManager.getIdManager();
  }

  @Before
  public void setUp() throws Exception {
    tasksManager = TasksManager.getManager();
    /*Reset state of pending tasks*/
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    tasksManager.setCompletedTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
  }

  @Test
  public void getPendingTasks_ReturnValidTask() throws NoSuchTaskException {
    Task input = new Task("submit op1", idManager.getId());
    tasksManager.addPendingTask(input);
    assertEquals("submit op1", tasksManager.getPendingTask(1).getName());
  }


  @Test(expected = NoSuchTaskException.class)
  public void getPendingTasks_ThrowException() throws NoSuchTaskException {
    tasksManager.getPendingTask(1);
  }

}
