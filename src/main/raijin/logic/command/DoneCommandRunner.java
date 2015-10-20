package raijin.logic.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.TaskUtils;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class DoneCommandRunner extends CommandRunner implements UndoableRedoable {
	TreeSet<Integer> idsToDone = new TreeSet<Integer>();
	TreeSet<Integer> idsUndone = new TreeSet<Integer>();
	Queue<Integer> idsDone = new LinkedList<Integer>();
	Queue<Task> tasksDone = new LinkedList<Task>();
	String taskDescription;
	Task task;
	static final String FEEDBACK_DONE_FAILURE = "Failed to mark task(s) as done - It doesn't exist!";
	
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {   
	idsToDone = input.getIds();
    
	if (idsToDone.isEmpty()) {       //If no id specified, use tags
	  idsToDone = getIdsFromTags(input.getTags());
	}
	
	if (idsToDone.isEmpty()) {
      return new Status(FEEDBACK_DONE_FAILURE);
    }

	logger.debug(idsToDone.toString());
    while(!idsToDone.isEmpty()) {
	  int id = idsToDone.pollFirst();
	  idsDone.offer(id);
	  try {
	    this.task = tasksManager.getPendingTask(id);
	    tasksDone.offer(task);
	  } catch (NoSuchTaskException e) {
	    wrapLowerLevelException(e, Constants.Command.DONE);
	  }
	  
	  if (taskDescription == null && idsToDone.isEmpty()) {
	    taskDescription = "\""+ task.getName() +"\"";
	  } else if (taskDescription == null && !idsToDone.isEmpty()) {
        taskDescription = id +", ";
      } else if (taskDescription != null && !idsToDone.isEmpty()) {
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
    while (!idsDone.isEmpty()) {
      try {
        int id = idsDone.poll();
        task = tasksDone.poll();

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
      idsDone.offer(id);
      try {
        task = tasksManager.getPendingTask(id);
        tasksDone.offer(task);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DONE);
      }
	  tasksManager.addCompletedTask(task);
    }
  }
  
  TreeSet<Integer> getIdsFromTags(TreeSet<String> tags) {
    List<Task> filtered = TaskUtils.filterTaskWithTags(tasksManager.getPendingTasks(), 
        tags);
    return new TreeSet<Integer>(filtered.stream().map(
        t -> t.getId()).collect(Collectors.toList()));
  }

}
