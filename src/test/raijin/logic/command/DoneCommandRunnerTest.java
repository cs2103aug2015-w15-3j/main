//@@author A0112213E
package raijin.logic.command;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.SimpleParser;
import raijin.storage.api.TasksManager;

public class DoneCommandRunnerTest {

  private DoneCommandRunner doneCommandRunner;
  private AddCommandRunner addCommandRunner;
  private TasksManager tasksManager;

  //===========================================================================
  // Helper methods
  //===========================================================================

  // All of these test cases are based on testing use cases and combining multiple inputs
  public Status addTask(String inputName, DateTime dateTime) throws UnableToExecuteCommandException {
    ParsedInput parsedInput = createSpecificTask(inputName, dateTime);
    Status returnStatus = addCommandRunner.processCommand(parsedInput);
    return returnStatus; 
  }

  public ParsedInput createSpecificTask(String inputName, DateTime dateTime) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(inputName).dateTime(dateTime).createParsedInput();
  }

  public Status doneTask(int id) throws UnableToExecuteCommandException {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.DONE).
        id(id).createParsedInput();

    return doneCommandRunner.processCommand(input);
  }

  public Status doneTaskIDS(String input) 
      throws UnableToExecuteCommandException, FailedToParseException {
    ParsedInput parsed = new SimpleParser().parse(input);  
    return doneCommandRunner.processCommand(parsed);
  }

  //===========================================================================
  // Test Cases
  //===========================================================================

  @Before
  public void setUp() throws Exception {
    addCommandRunner = new AddCommandRunner();
    doneCommandRunner = new DoneCommandRunner();
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
    addTask("Ice ice baby", new DateTime("31/08/2015"));
  }

  @Test
  public void processCommand_DoneTask() throws UnableToExecuteCommandException {
    Status returnStatus = doneTask(1);
    String expectedStatusLine = "Nicely done! Give yourself a pat on the back!\n";
    assertEquals(expectedStatusLine, returnStatus.getFeedback());
  }

  @Test
  public void undo_DoneTask() throws UnableToExecuteCommandException {
    doneTask(1);
    doneCommandRunner.undo();
    assertTrue(!tasksManager.isEmptyPendingTasks());
  }

  @Test
  public void redo_DoneTask() throws UnableToExecuteCommandException, NoSuchTaskException {
    doneTask(1);
    doneCommandRunner.undo();
    doneCommandRunner.redo();
    assertTrue(tasksManager.isEmptyPendingTasks());
  }

  //@@author A0112213E
  @Test
  public void processCommand_DoneMultipleTasks() 
      throws UnableToExecuteCommandException, FailedToParseException { 
    addTask("Burn burn baby", new DateTime("31/08/2015"));
    addTask("Chill Chill baby", new DateTime("31/08/2015"));
    Status returnStatus = doneTaskIDS("done 1 2 3");
    String expectedStatusLine = String.format(Constants.FEEDBACK_DONE_SUCCESS, "Ice ice baby")
        +"\n"+ String.format(Constants.FEEDBACK_DONE_SUCCESS, "Burn burn baby") +"\n"+
        String.format(Constants.FEEDBACK_DONE_SUCCESS, "Chill Chill baby") +"\n";
    assertEquals(expectedStatusLine, returnStatus.getFeedback());
    assertTrue(tasksManager.isEmptyPendingTasks());
  }


  public void undo_DoneMultipleTasks() 
      throws UnableToExecuteCommandException, NoSuchTaskException, FailedToParseException {
    addTask("Burn burn baby", new DateTime("31/08/2015"));
    addTask("Chill Chill baby", new DateTime("31/08/2015"));
    doneTaskIDS("done 1 2 3");
    doneCommandRunner.undo();
    assertTrue(tasksManager.isEmptyCompletedTasks());
    assertEquals("Ice ice baby", tasksManager.getPendingTask(1).getName());
    assertEquals("Burn burn baby", tasksManager.getPendingTask(2).getName());
    assertEquals("Chill Chill baby", tasksManager.getPendingTask(3).getName());
  }


  public void redo_DoneMultipleTasks() 
      throws UnableToExecuteCommandException, NoSuchTaskException, FailedToParseException {
    addTask("Burn burn baby", new DateTime("31/08/2015"));
    addTask("Chill Chill baby", new DateTime("31/08/2015"));
    doneTaskIDS("done 1 2 3");
    doneCommandRunner.undo();
    doneCommandRunner.redo();
    assertTrue(!tasksManager.isEmptyCompletedTasks());
    assertEquals("Ice ice baby", tasksManager.getCompletedTasks().get(1).getName());
    assertEquals("Burn burn baby", tasksManager.getCompletedTasks().get(2).getName());
    assertEquals("Chill Chill baby", tasksManager.getCompletedTasks().get(3).getName());
  }

}
