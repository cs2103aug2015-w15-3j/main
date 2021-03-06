//@@author A0112213E

package raijin.common.datatypes;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class TaskTest {
  

  private Task flexibleTask;
  private Task task;
  private TasksManager tasksManager;
  private static IDManager idManager;

  @BeforeClass
  public static void setUpClass() {
    idManager = IDManager.getIdManager();
  }

  @Before
  public void setUp(){
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
    task = new Task("submit op1", idManager.getId());
  }
  
  @Test
  public void testNameMustNotBeNull(){
    assertNotNull(task.getName());
  }

  @Test
  public void testIdMustBeValid(){
    assertTrue(task.getId() > 0);
  }

  @Test
  public void testUniqueId(){
    flexibleTask = new Task("submit op2", idManager.getId());
    assertNotEquals(flexibleTask.getId(), task.getId());
  }

  @Test
  public void testAddTag(){
    TreeSet<String> tags = new TreeSet<String>();
    tags.add("cs2101");
    task.addTags(tags);
    assertTrue(task.getTags().contains("cs2101"));
  }
  
  @Test
  public void testKeyWords() {
    assertEquals("submit", task.getKeywords().get(0));
  }
  
  @Test
  public void addSubTasks_ValidSubTasks() {
    task.addSubTask(2);
    task.addSubTask(3);
    tasksManager.addPendingTask(new Task("submit op1", idManager.getId()));
    tasksManager.addPendingTask(new Task("submit op2", idManager.getId()));
    assertEquals(2, task.getSubTasks().size());
  }
  
  @Test
  public void removeSubTasks_ValidChildOfTask() {
    task.addSubTask(5);
    task.removeSubTask(5);
    assertEquals(0, task.getSubTasks().size());
  }
  
  @Test
  public void lazyUpdateSubTasks_DeleteSubTasks() throws NoSuchTaskException {
    //Create initial subtasks
    task.addSubTask(2);
    task.addSubTask(3);
    //Add subtasks to pending list
    tasksManager.addPendingTask(new Task("submit op1", idManager.getId()));
    tasksManager.addPendingTask(new Task("submit op2", idManager.getId()));
    
    assertEquals(2, task.getSubTasks().size());
    //ID 1 is taken by initial task
    tasksManager.deletePendingTask(2);
    task.lazyUpdateSubTasks();
    assertEquals(1, task.getSubTasks().size());
  }

  @Test(expected = AssertionError.class)
  @Ignore
  public void testIdNotZero() {
    Task test = new Task("I am wrong", -1);
    assertEquals("I am wrong", test.getName());
  }
  
  @Test
  public void getType_FloatingTask_ReturnType() {
    Task floatingTask = new Task("submit op1", 1);
    assertEquals(Constants.TYPE_TASK.FLOATING, floatingTask.getType());
  }

  @Test
  public void getType_SpecificTask_ReturnType() {
    DateTime task = new DateTime("19/09/2015", "2359");
    ParsedInput specificTaskInput = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        dateTime(task).createParsedInput();

    Task specificTask = new Task("submit op1", 2, specificTaskInput);
    
    assertEquals(Constants.TYPE_TASK.SPECIFIC, specificTask.getType());
  }

  @Test
  public void getType_EventTask_ReturnType() {
    DateTime event = new DateTime("19/09/2015", "0800", "1000");
    ParsedInput eventInput = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        dateTime(event).createParsedInput();

    Task eventTask = new Task("submit op1", 3, eventInput);
    
    assertEquals(Constants.TYPE_TASK.EVENT, eventTask.getType());
  }
  
  @Test
  public void getPriority_LowPriority() {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        priority(Constants.PRIORITY_LOW).createParsedInput();

    assertEquals(Constants.PRIORITY_LOW, input.getPriority());
  }
}
