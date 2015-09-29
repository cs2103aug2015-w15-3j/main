package raijin.logic.command;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProviderRunner;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
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

  public Status addTask(String inputName, DateTime dateTime) {
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
  }

  @Test
  public void execute_SpecificDeadline_ReturnSuccessfulStatus() {
    String inputName = "submit op2 to ms lee";
    DateTime dateTime = new DateTime("19/09/2015");
    Status returnStatus = addTask(inputName, dateTime);
    String expected = String.format(Constants.FEEDBACK_ADD_SUCCESS, inputName);
    
    assertEquals(expected, returnStatus.getFeedback());
  }
  
  @Test
  public void undo_SpecifiedDeadline() throws NonExistentTaskException {
    addTask("submit op2 to ms lee", new DateTime("19/09/2015"));
    addCommandRunner.undo();
    assertTrue(tasksManager.isEmptyPendingTasks());
  }

  @Test
  public void redo_SpecifiedDeadline() throws NonExistentTaskException {
    addTask("submit op2 to ms lee", new DateTime("19/09/2015"));
    addCommandRunner.undo();
    addCommandRunner.redo();
    assertEquals(tasksManager.getPendingTask(1).getName(), "submit op2 to ms lee");
  }

}
