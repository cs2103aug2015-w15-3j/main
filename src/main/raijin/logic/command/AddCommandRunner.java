package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class AddCommandRunner extends CommandRunner implements UndoableRedoable {

  private Task currentTask;

  Task createTask(ParsedInput input) {
    return new Task(input.getName(), input.getDateTime());
  }

  Status createSuccessfulStatus() {
    String taskName = currentTask.getName();
    return new Status(String.format(Constants.FEEDBACK_ADD_SUCCESS, taskName));
  }
  public Status execute(ParsedInput input) {
    currentTask = createTask(input);
    tasksManager.addPendingTask(currentTask);
    history.pushCommand(this);
    return createSuccessfulStatus();
  }

  public void undo() throws NoSuchTaskException {
    tasksManager.deletePendingTask(currentTask.getId());
  }

  public void redo() {
    currentTask.resetId();  //Previous id may be used by other task
    tasksManager.addPendingTask(currentTask);
  }

}
