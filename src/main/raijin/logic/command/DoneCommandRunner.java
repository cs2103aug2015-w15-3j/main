package raijin.logic.command;

import java.util.Stack;
import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class DoneCommandRunner extends CommandRunner implements UndoableRedoable {
	TreeSet<Integer> idsToDelete = new TreeSet<Integer>();
	TreeSet<Integer> idsUndone = new TreeSet<Integer>();
	Stack<Integer> idsDeleted = new Stack<Integer>();
	Stack<Task> tasksDeleted = new Stack<Task>();
	String taskDescription;
	Task task;
	
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {   
	idsToDelete = input.getIds();
    
    while(!idsToDelete.isEmpty()) {
	  int id = idsToDelete.pollFirst();
	  idsDeleted.push(id);
	  try {
	    this.task = tasksManager.getPendingTask(id);
	    tasksDeleted.push(task);
	  } catch (NoSuchTaskException e) {
	    wrapLowerLevelException(e, Constants.Command.DONE);
	  }
	  
	  if (taskDescription == null && idsToDelete.isEmpty()) {
	    taskDescription = "\""+ task.getName() +"\"";
	  } else if (taskDescription == null && !idsToDelete.isEmpty()) {
        taskDescription = id +", ";
      } else if (taskDescription != null && !idsToDelete.isEmpty()) {
	    taskDescription += id +", ";
	  } else {
	    taskDescription += "and " +id+ ".";
	  }

	  tasksManager.addCompletedTask(task);
	  
	}
	history.pushCommand(this);
    
    return new Status("Nicely done! You have completed the task - " 
    					+ taskDescription + 
    					" Give yourself a pat on the back!");
  }

  public void undo() throws UnableToExecuteCommandException {
    while (!idsDeleted.isEmpty()) {
      try {
        int id = idsDeleted.pop();
        task = tasksDeleted.pop();
        idsUndone.add(id);
        
        tasksManager.deleteCompletedTask(id);
        task.setId(idManager.getId());
        tasksManager.addPendingTask(task);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DONE);
      }
    }
  }

  public void redo() throws UnableToExecuteCommandException {
    while (!idsUndone.isEmpty()) {
      int id = idsUndone.pollFirst();
      idsDeleted.push(id);
      try {
        task = tasksManager.getPendingTask(id);
        tasksDeleted.push(task);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DONE);
      }
	  tasksManager.addCompletedTask(task);
    }
  }
}
