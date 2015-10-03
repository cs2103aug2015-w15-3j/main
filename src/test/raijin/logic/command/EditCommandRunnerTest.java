package raijin.logic.command;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class EditCommandRunnerTest {

  private AddCommandRunner addCommandRunner;
  private EditCommandRunner editCommandRunner;
  private TasksManager tasksManager;
  
  @Before
  public void setUp() throws Exception {
    addCommandRunner = new AddCommandRunner();
    editCommandRunner = new EditCommandRunner();
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
    addTask("First entry.", new DateTime("03/10/2015"));
  }
  
  @Test
  public void execute_EditTaskName() throws NonExistentTaskException {
    Status returnStatus = editTask(1, "First entry changed.", null);
    String expectedStatusLine = String.format(Constants.FEEDBACK_EDIT_SUCCESS, 1);
    assertEquals(expectedStatusLine, returnStatus.getFeedback());
    assertEquals("First entry changed.", tasksManager.getPendingTask(1).getName());
  }
  
  @Test
  public void execute_EditTaskDate() throws NonExistentTaskException {
    Status returnStatus = editTask(1, null, new DateTime("05/10/2015"));
    String expectedStatusLine = String.format(Constants.FEEDBACK_EDIT_SUCCESS, 1);
    assertEquals(expectedStatusLine, returnStatus.getFeedback());
    assertEquals("2015-10-05", tasksManager.getPendingTask(1).getDateTime()
                .getStartDate().toString());
  }

  @Test
  public void execute_EditTaskNameDate() throws NonExistentTaskException {
    Status returnStatus = editTask(1, "First entry changed again.", new DateTime("05/10/2015"));
    String expectedStatusLine = String.format(Constants.FEEDBACK_EDIT_SUCCESS, 1);
    assertEquals(expectedStatusLine, returnStatus.getFeedback());
    assertEquals("First entry changed again.", tasksManager.getPendingTask(1).getName());
    assertEquals("2015-10-05", tasksManager.getPendingTask(1).getDateTime()
                .getStartDate().toString());
  }
  
  @Test
  public void undo_EditTaskNameDate() throws NonExistentTaskException {
    editTask(1, "First entry changed.", new DateTime("05/10/2015"));
    editCommandRunner.undo();
    assertEquals("First entry.", tasksManager.getPendingTask(1).getName());
    assertEquals("2015-10-03", tasksManager.getPendingTask(1).getDateTime()
                .getStartDate().toString());
  }
  
  @Test
  public void redo_EditTaskNameDate() throws NonExistentTaskException {
    editTask(1, "First entry changed.", new DateTime("05/10/2015"));
    editCommandRunner.undo();
    editCommandRunner.redo();
    assertEquals("First entry changed.", tasksManager.getPendingTask(1).getName());
    assertEquals("2015-10-05", tasksManager.getPendingTask(1).getDateTime()
                .getStartDate().toString());
  }
  
  //===========================================================================
  // Helper methods
  //===========================================================================
  
  public Status addTask(String inputName, DateTime dateTime) {
    ParsedInput parsedInput = createSpecificTask(inputName, dateTime);
    Status returnStatus = addCommandRunner.execute(parsedInput);
    return returnStatus;
  }
  
  public ParsedInput createSpecificTask(String inputName, DateTime dateTime) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
      name(inputName).dateTime(dateTime).createParsedInput();
  }
  
  public Status editTask(int id, String inputName, DateTime dateTime) 
      throws NonExistentTaskException {
    ParsedInput parsedInput;
    if (inputName == null) {
      parsedInput = modifyTaskDate(id, dateTime);
    } else if (dateTime == null) {
      parsedInput = modifyTaskName(id, inputName);
    } else {
      parsedInput = modifyTaskNameDate(id, inputName, dateTime);
    }
    return editCommandRunner.execute(parsedInput);
  }

  public ParsedInput modifyTaskName(int id, String inputName) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT).
        id(id).name(inputName).createParsedInput();
  }
  
  public ParsedInput modifyTaskDate(int id, DateTime dateTime) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT).
        id(id).dateTime(dateTime).createParsedInput();
  }
  
  public ParsedInput modifyTaskNameDate(int id, String inputName, DateTime dateTime) {
    return new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT).
        id(id).name(inputName).dateTime(dateTime).createParsedInput();
  }
}