//@@author A0112213E

package raijin.logic.command;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.IDManager;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public class AddCommandRunnerTest {

  private AddCommandRunner addCommandRunner;
  private TasksManager tasksManager;

  //===========================================================================
  // Helper methods
  //===========================================================================
  
  public ParsedInput createSpecificTask(String inputName, DateTime dateTime) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
      name(inputName).dateTime(dateTime).createParsedInput();
  }

  public Status addTask(String inputName, DateTime dateTime) throws UnableToExecuteCommandException {
    ParsedInput parsedInput = createSpecificTask(inputName, dateTime);
    
    Status returnStatus = addCommandRunner.execute(parsedInput);
    return returnStatus;
  }

  //===========================================================================
  // Test Cases
  //===========================================================================

  @Before
  public void setUp() throws Exception {
    addCommandRunner = new AddCommandRunner();
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
  }

  @Test
  public void execute_SpecificDeadline_ReturnSuccessfulStatus() throws UnableToExecuteCommandException {
    String inputName = "submit op2 to ms lee";
    DateTime dateTime = new DateTime("19/09/2015");
    Status returnStatus = addTask(inputName, dateTime);
    String expected = String.format(Constants.FEEDBACK_ADD_SUCCESS, inputName);
    
    assertEquals(expected, returnStatus.getFeedback());
  }
  
  @Test
  public void undo_SpecifiedDeadline() throws UnableToExecuteCommandException {
    addTask("submit op2 to ms lee", new DateTime("19/09/2015"));
    addCommandRunner.undo();
    assertTrue(tasksManager.isEmptyPendingTasks());
  }

  @Test
  public void redo_SpecifiedDeadline() throws UnableToExecuteCommandException, NoSuchTaskException {
    addTask("submit op2 to ms lee", new DateTime("19/09/2015"));
    addCommandRunner.undo();
    addCommandRunner.redo();
    assertEquals(tasksManager.getPendingTask(1).getName(), "submit op2 to ms lee");
  }

  @Test
  public void execute_WithHighPriority() throws UnableToExecuteCommandException, NoSuchTaskException {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name("submit test").priority("h").createParsedInput();
    addCommandRunner.execute(input);
    assertEquals("h", tasksManager.getPendingTask(1).getPriority());
  }
  
  @Test
  public void execute_DuplicateTask_ReturnWarning() throws UnableToExecuteCommandException {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name("submit op2 to ms lee").dateTime(new DateTime("19/09/2015")).createParsedInput();
    tasksManager.addPendingTask(new Task(input.getName(), IDManager.getIdManager().getId(), input));
    Status status = addTask("submit op2 to ms lee", new DateTime("19/09/2015"));
    assertEquals(String.format(Constants.FEEDBACK_ADD_FAILURE, "submit op2 to ms lee"), status.getFeedback());
  }

  @Test
  public void isMultipleTasks_MultipleTasks_ReturnTrue() {
    //Setup multiple tasks
    TreeSet<String> testNames = new TreeSet<String>();
    testNames.add("I am cute");
    testNames.add("wash batu");
    testNames.add("watch monty");
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(testNames).priority("h").createParsedInput();
    assertTrue(addCommandRunner.isMultipleTasks(input));
  }

  @Test
  public void undo_MultipleTasks() throws UnableToExecuteCommandException {
    TreeSet<String> testNames = new TreeSet<String>();
    testNames.add("I am cute");
    testNames.add("wash batu");
    testNames.add("watch monty");
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(testNames).priority("h").createParsedInput();

    addCommandRunner.execute(input);
    int afterAdd = tasksManager.getPendingTasks().size();
    addCommandRunner.undo();
    int afterUndo = tasksManager.getPendingTasks().size();
    
    assertEquals(3, afterAdd);
    assertEquals(0, afterUndo);
  }

  @Test
  public void redo_MultipleTasks() throws UnableToExecuteCommandException {
    TreeSet<String> testNames = new TreeSet<String>();
    testNames.add("I am cute");
    testNames.add("wash batu");
    testNames.add("watch monty");
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(testNames).priority("h").createParsedInput();

    addCommandRunner.execute(input);
    addCommandRunner.undo();
    int afterUndo = tasksManager.getPendingTasks().size();
    addCommandRunner.redo();
    int afterRedo = tasksManager.getPendingTasks().size();
    
    assertEquals(0, afterUndo);
    assertEquals(3, afterRedo);
  }

  @Test
  public void createStatus_MultipleTasks() throws UnableToExecuteCommandException {
    
    //Add some existing task
    tasksManager.addPendingTask(new Task("I am cute", 1));

    TreeSet<String> testNames = new TreeSet<String>();
    testNames.add("I am cute");
    testNames.add("wash batu");
    testNames.add("watch monty");
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(testNames).priority("h").createParsedInput();
    
    Status status = addCommandRunner.execute(input);
    String expected = "You have added the task(s) successfully";
    assertEquals(expected, status.getFeedback());
  }
  
  @Test
  public void processCommand_MultipleTasks() throws UnableToExecuteCommandException {
    addTask("I am weird", new DateTime("19/09/2011"));
    addTask("I am new", new DateTime("19/09/2011"));
    addTask("I am old", new DateTime("19/09/2011"));
  }
}
