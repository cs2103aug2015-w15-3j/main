package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;

public class DeleteCommandRunner extends CommandRunner implements  UndoableRedoable {
  int id;
  String taskDescription;
  Task task;

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    this.id = input.getId();
    try {
      this.task = tasksManager.getPendingTask(id);
      taskDescription = task.getName();

      tasksManager.deletePendingTask(id);
      history.pushCommand(this);
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.DELETE);
    }
    return new Status(String.format(Constants.FEEDBACK_DELETE_SUCCESS, taskDescription));
  }

  public void undo() {
	task.resetId();  
	this.id = task.getId();
    tasksManager.addPendingTask(task);
  }

  public void redo() throws UnableToExecuteCommandException {
    try {
      tasksManager.deletePendingTask(id);
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.DELETE);
    }
  }

}
