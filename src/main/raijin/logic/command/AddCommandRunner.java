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
    /*At least need a name to create flexible task*/
    if (input.getName() == null) {
      throw new UnableToExecuteCommandException("Empty task name", Constants.Command.ADD,
          new IllegalArgumentException());
    }
    return new Task(input.getName(), idManager.getId(), input.getDateTime());
  }

  Status createSuccessfulStatus() {
    String taskName = currentTask.getName();
    return new Status(String.format(Constants.FEEDBACK_ADD_SUCCESS, taskName));
  }

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    currentTask = createTask(input);
    tasksManager.addPendingTask(currentTask);
    history.pushCommand(this);
    return createSuccessfulStatus();
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
