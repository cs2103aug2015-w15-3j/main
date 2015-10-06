package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.datatypes.Status;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class EditCommandRunner extends CommandRunner implements UndoableRedoable {

  private Task taskAfterChange;
  private Task taskBeforeChange;
  
  Task modifyTask(ParsedInput input) {
    String name = (input.getName() == null) ? taskBeforeChange.getName() : input.getName();
    DateTime dateTime = (input.getDateTime() == null) ? 
                        taskBeforeChange.getDateTime() : input.getDateTime();
    return new Task(name, dateTime);
  }
  
  Status editSuccessfulStatus() {
    int taskId = taskAfterChange.getId();
    return new Status(String.format(Constants.FEEDBACK_EDIT_SUCCESS, taskId));
  }
  
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    try {
    taskBeforeChange = tasksManager.getPendingTask(input.getId());
    tasksManager.deletePendingTask(taskBeforeChange.getId());
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.EDIT);
    }
    taskAfterChange = modifyTask(input);
    tasksManager.addPendingTask(taskAfterChange);
    history.pushCommand(this);
    return editSuccessfulStatus();
  }

  public void undo() throws UnableToExecuteCommandException  {
    try {
      tasksManager.deletePendingTask(taskAfterChange.getId());
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.EDIT);
    }
    tasksManager.addPendingTask(taskBeforeChange);
  }

  public void redo() throws UnableToExecuteCommandException {
    try {
      tasksManager.deletePendingTask(taskBeforeChange.getId());
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.EDIT);
    }
    taskAfterChange.resetId();
    tasksManager.addPendingTask(taskAfterChange);
    
  }

}
