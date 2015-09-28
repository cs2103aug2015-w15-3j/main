package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.Memory;

public class AddCommandRunner implements CommandRunner, UndoableRedoable {

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
    memory.addTask(currentTask);
    memory.addToHistory(this);
    return createSuccessfulStatus();
  }

  public void undo() {
    memory.deleteTask(currentTask.getId());
  }

  public void redo() {
    memory.addTask(currentTask);
  }

}
