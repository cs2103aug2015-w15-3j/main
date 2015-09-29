package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class DoneCommandRunner implements CommandRunner,UndoableRedoable {
	int id;
	String taskDescription;
	
  public Status execute(ParsedInput input) throws NonExistentTaskException {   
	this.id = input.getId();
    Task task = tasksManager.getPendingTask(id);
    taskDescription = task.getName();
    
	tasksManager.addCompletedTask(task);
	history.pushCommand(this);
    
    return new Status("Nicely done! You have completed the task - " 
    					+ taskDescription + 
    					" Give yourself a pat on the back!");
  }

  public void undo() throws NonExistentTaskException {
    // TODO Auto-generated method stub
    
  }

  public void redo() {
    // TODO Auto-generated method stub
    
  }
}
