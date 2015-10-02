package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class DoneCommandRunner extends CommandRunner implements UndoableRedoable {
	int id;
	String taskDescription;
	Task task;
	
  public Status execute(ParsedInput input) throws NonExistentTaskException {   
	this.id = input.getId();
    this.task = tasksManager.getPendingTask(id);
    taskDescription = task.getName();
    
	tasksManager.addCompletedTask(task);
	history.pushCommand(this);
    
    return new Status("Nicely done! You have completed the task - " 
    					+ taskDescription + 
    					" Give yourself a pat on the back!");
  }

  public void undo() throws NonExistentTaskException {
	  tasksManager.deleteCompletedTask(id);
  }

  public void redo() {
	  tasksManager.addCompletedTask(task);
  }
}
