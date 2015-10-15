package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class AddCommandRunner extends CommandRunner implements UndoableRedoable {

  private Task currentTask;

  Task createTask(ParsedInput input) throws UnableToExecuteCommandException {
    return new Task(input.getName(), idManager.getId(), input);
  }

  Status createStatus() {
    String taskName = currentTask.getName();
    if (tasksManager.getPendingTasks().containsValue(currentTask)) {
      return new Status(Constants.FEEDBACK_ADD_FAILURE);
    } 
    return new Status(String.format(Constants.FEEDBACK_ADD_SUCCESS, taskName));
  }

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    currentTask = createTask(input);
    Status feedback = createStatus();
    tasksManager.addPendingTask(currentTask);
    history.pushCommand(this);
    return feedback;
  }

  public void undo() throws UnableToExecuteCommandException {
    logger.info("Undoing task id {} with content {}", currentTask.getId(), currentTask.getName());
    try {
      tasksManager.deletePendingTask(currentTask.getId());
    } catch (NoSuchTaskException e) {
      wrapLowerLevelException(e, Constants.Command.ADD);
    }
  }

  public void redo() {
    currentTask.setId(idManager.getId());  //Previous id may be used by other task
    logger.info("Re-adding task id {} with content {}", currentTask.getId(), currentTask.getName());
    tasksManager.addPendingTask(currentTask);
  }

}
