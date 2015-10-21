package raijin.logic.command;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.TaskUtils;

public class DeleteCommandRunner extends CommandRunner implements  UndoableRedoable {

  TreeSet<Integer> idsToDelete = new TreeSet<Integer>();
  TreeSet<Integer> idsUndone = new TreeSet<Integer>();
  Queue<Integer> idsDeleted = new LinkedList<Integer>();
  Queue<Task> tasksDeleted = new LinkedList<Task>();
  String taskDescription;
  Task task;

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    idsToDelete = input.getIds();
    taskDescription = "";
    
    if (idsToDelete.isEmpty()) {
      idsToDelete = getIdsFromTags(input.getTags());
    }

    if (idsToDelete.isEmpty()) {
      return new Status(Constants.FEEDBACK_DELETE_FAILURE);
    }

    while(!idsToDelete.isEmpty()) {
      int id = idsToDelete.pollFirst();
      try {
        this.task = tasksManager.getPendingTask(id);
        tasksDeleted.offer(task);
            
        tasksManager.deletePendingTask(id);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DELETE);
      }
      /*
      if (taskDescription == "" && idsToDelete.isEmpty()) {
        taskDescription = "\""+ task.getName() + "\"";
      } else if (!idsToDelete.isEmpty()) {
        taskDescription += "\""+ task.getName() +"\", ";
      } else {
        taskDescription += "& \""+ task.getName() +"\"";
      }*/
      
      taskDescription += String.format(Constants.FEEDBACK_DELETE_SUCCESS, task.getName())+"\n";
    }
    
    history.pushCommand(this);
    
    return new Status(taskDescription);
  }

  public void undo() {
    while (!tasksDeleted.isEmpty()) {
      task = tasksDeleted.poll();
      
      task.setId(idManager.getId());  
      idsUndone.add(task.getId());
      tasksManager.addPendingTask(task);
    }
  }

  public void redo() throws UnableToExecuteCommandException {
    while (!idsUndone.isEmpty()) {
      int id = idsUndone.pollFirst();
      idsDeleted.offer(id);
      try {
        task = tasksManager.getPendingTask(id);
        tasksDeleted.offer(task);
        tasksManager.deletePendingTask(id);
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.DELETE);
      }
    }
  }


  /**
   * Get id from filtered tasks when tag is chosen 
   * @param tags
   * @return
   */
  TreeSet<Integer> getIdsFromTags(TreeSet<String> tags) {
    List<Task> filtered = TaskUtils.filterTaskWithTags(tasksManager.getPendingTasks(), 
        tags);
    System.out.println(filtered.toString());
    return new TreeSet<Integer>(filtered.stream().map(
        t -> t.getId()).collect(Collectors.toList()));
  }

}
