package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class DoneCommandRunner extends CommandRunner implements UndoableRedoable {
	int id;
	String taskDescription;
	Task task;
	
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {   
	this.id = input.getId();
	try {
      this.task = tasksManager.getPendingTask(id);
	} catch (NoSuchTaskException e) {
	  wrapLowerLevelException(e, Constants.Command.DONE);
	}
    taskDescription = task.getName();
    
	tasksManager.addCompletedTask(task);
	history.pushCommand(this);
    
    return new Status("Nicely done! You have completed the task - " 
    					+ taskDescription + 
    					" Give yourself a pat on the back!");
  }

  public void undo() throws UnableToExecuteCommandException {
    try {
	  tasksManager.deleteCompletedTask(id);
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.DONE);
    }
  }

  public void redo() {
	  tasksManager.addCompletedTask(task);
  }
}
