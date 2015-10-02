package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;

public class DeleteCommandRunner extends CommandRunner implements  UndoableRedoable {
  int id;
  String taskDescription;
  Task task;

  public Status execute(ParsedInput input) throws NonExistentTaskException {
    this.id = input.getId();
    this.task = tasksManager.getPendingTask(id);
    taskDescription = task.getName();

    tasksManager.deletePendingTask(id);
    history.pushCommand(this);

    return new Status("You have just deleted " + taskDescription + "!");
  }

  public void undo() {
    tasksManager.addPendingTask(task);
  }

  public void redo() {
    try {
      tasksManager.deletePendingTask(id);
    } catch (NonExistentTaskException e) {
      e.printStackTrace();
    }
  }

}
