package raijin.logic.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class DeleteCommandRunnerTest {

	 private DeleteCommandRunner deleteCommandRunner;
	 private AddCommandRunner addCommandRunner;
	 private TasksManager tasksManager;
	 
	//===========================================================================
	// Helper methods
	//===========================================================================

	 public Status addTask(String inputName, DateTime dateTime) throws UnableToExecuteCommandException {
		    ParsedInput parsedInput = createSpecificTask(inputName, dateTime);
		    Status returnStatus = addCommandRunner.processCommand(parsedInput);
		    return returnStatus; 
     }
	  
     public ParsedInput createSpecificTask(String inputName, DateTime dateTime) {
		    return new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
		      name(inputName).dateTime(dateTime).createParsedInput();
	 }
	 
     public Status deleteTask(int id) throws UnableToExecuteCommandException {
    	 ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE).
    		        id(id).createParsedInput();
    	 
    	 return deleteCommandRunner.processCommand(input);
     }
     
	//===========================================================================
	// Test Cases
	//===========================================================================

	 @Before
	  public void setUp() throws Exception {
	    addCommandRunner = new AddCommandRunner();
	    deleteCommandRunner = new DeleteCommandRunner();
	    tasksManager = TasksManager.getManager();
	    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
	    IDManager.getIdManager().flushIdPool();
	    addTask("Ice ice baby", new DateTime("31/08/2015"));
	  }

	 @Test
	  public void processCommand_DeleteTask() throws UnableToExecuteCommandException {
	    Status returnStatus = deleteTask(1);
	    String expectedStatusLine = String.format(Constants.FEEDBACK_DELETE_SUCCESS, "Ice ice baby");
	    assertEquals(expectedStatusLine, returnStatus.getFeedback());
	  }
	  
	  @Test
	  public void undo_DeleteTask() throws UnableToExecuteCommandException {
	    deleteTask(1);
		deleteCommandRunner.undo();
	    assertTrue(!tasksManager.isEmptyPendingTasks());
	  }

	  @Test
	  public void redo_DeleteTask() throws UnableToExecuteCommandException, NoSuchTaskException {
	    deleteTask(1);
		deleteCommandRunner.undo();
	    deleteCommandRunner.redo();
	    assertTrue(tasksManager.isEmptyPendingTasks());
	  }
}
