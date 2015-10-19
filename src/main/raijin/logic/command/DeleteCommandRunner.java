package raijin.logic.command;

import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;

public class DeleteCommandRunner extends CommandRunner implements  UndoableRedoable {
  TreeSet<Integer> idsToDone = new TreeSet<Integer>();
  TreeSet<Integer> idsUndone = new TreeSet<Integer>();
  Queue<Integer> idsDone = new LinkedList<Integer>();
  Queue<Task> tasksDone = new LinkedList<Task>();
  String taskDescription;
  Task task;

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    idsToDone = input.getIds();
    
    while(!idsToDone.isEmpty()) {
      int id = idsToDone.pollFirst();
      try {
        this.task = tasksManager.getPendingTask(id);
        tasksDone.offer(task);
            
        tasksManager.deletePendingTask(id);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DELETE);
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
      
    }
    
    history.pushCommand(this);
    
    return new Status(String.format(Constants.FEEDBACK_DELETE_SUCCESS, taskDescription));
  }

  public void undo() {
    while (!tasksDone.isEmpty()) {
      task = tasksDone.poll();
      
      task.setId(idManager.getId());  
      idsUndone.add(task.getId());
      tasksManager.addPendingTask(task);
    }
  }

  public void redo() throws UnableToExecuteCommandException {
    while (!idsUndone.isEmpty()) {
      int id = idsUndone.pollFirst();
      idsDone.offer(id);
      try {
        task = tasksManager.getPendingTask(id);
        tasksDone.offer(task);
        tasksManager.deletePendingTask(id);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DELETE);
      }
    }
  }

}
