package raijin.storage.api;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;

public class TasksManagerTest {

  private TasksManager tasksManager;

  @Before
  public void setUp() throws Exception {
    tasksManager = TasksManager.getManager();
    /*Reset state of pending tasks*/
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    tasksManager.setCompletedTasks(new HashMap<Integer, Task>());
  }

  @Test
  public void getPendingTasks_ReturnValidTask() throws NonExistentTaskException {
    Task input = new Task("submit op1", new DateTime("19/09/2015"));
    tasksManager.addPendingTask(input);
    assertEquals("submit op1", tasksManager.getPendingTask(1).getName());
  }


  @Test(expected = NonExistentTaskException.class)
  public void getPendingTasks_ThrowException() throws NonExistentTaskException {
    tasksManager.getPendingTask(1);
  }

}