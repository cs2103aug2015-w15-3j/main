package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.datatypes.Status;
import raijin.common.exception.NonExistentTaskException;
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
  
  public Status execute(ParsedInput input) throws NonExistentTaskException {
    taskBeforeChange = tasksManager.getPendingTask(input.getId());
    history.pushCommand(this);
    tasksManager.deletePendingTask(taskBeforeChange.getId());
    taskAfterChange = modifyTask(input);
    tasksManager.addPendingTask(taskAfterChange);
    return editSuccessfulStatus();
  }

  public void undo() throws NonExistentTaskException  {
    tasksManager.deletePendingTask(taskAfterChange.getId());
    tasksManager.addPendingTask(taskBeforeChange);
  }

  public void redo() throws NonExistentTaskException {
    tasksManager.deletePendingTask(taskBeforeChange.getId());
    taskAfterChange.resetId();
    tasksManager.addPendingTask(taskAfterChange);
    
  }

}
