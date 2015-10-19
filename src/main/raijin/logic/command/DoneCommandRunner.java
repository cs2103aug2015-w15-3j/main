package raijin.logic.command;

import java.util.LinkedList;
import java.util.Queue;
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
	Queue<Integer> idsDeleted = new LinkedList<Integer>();
	Queue<Task> tasksDeleted = new LinkedList<Task>();
	String taskDescription;
	Task task;
	
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {   
	idsToDelete = input.getIds();
    
    while(!idsToDelete.isEmpty()) {
	  int id = idsToDelete.pollFirst();
	  idsDeleted.offer(id);
	  try {
	    this.task = tasksManager.getPendingTask(id);
	    tasksDeleted.offer(task);
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
        int id = idsDeleted.poll();
        task = tasksDeleted.poll();

        tasksManager.deleteCompletedTask(id);
        
        task.setId(idManager.getId());
        idsUndone.add(task.getId());
        tasksManager.addPendingTask(task);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DONE);
      }
    }
  }

  public void redo() throws UnableToExecuteCommandException {
    while (!idsUndone.isEmpty()) {
      int id = idsUndone.pollFirst();
      idsDeleted.offer(id);
      try {
        task = tasksManager.getPendingTask(id);
        tasksDeleted.offer(task);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DONE);
      }
	  tasksManager.addCompletedTask(task);
    }
  }
}
