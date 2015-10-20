package raijin.logic.command;

import java.util.ArrayList;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

public class AddCommandRunner extends CommandRunner implements UndoableRedoable {

  private Task currentTask;                                        //First task 
  private ArrayList<Task> listOfTasks = new ArrayList<Task>();
  private boolean isSuccess = false;                              //Determinse success of operation

  void createTasks(ParsedInput input) {
    listOfTasks.clear();
    for (String name : input.getNames()) {
      listOfTasks.add(new Task(name, idManager.getId(), input));
    }
    currentTask = listOfTasks.get(0);
  }

  Status createStatus() {
    StringBuilder strBuilder = new StringBuilder();
    for (Task task : listOfTasks) {
      String taskName = task.getName();
      if (tasksManager.getPendingTasks().containsValue(task)) {
        strBuilder.append(String.format(Constants.FEEDBACK_ADD_FAILURE, taskName) + "\n");
      } else {
        strBuilder.append(String.format(Constants.FEEDBACK_ADD_SUCCESS, taskName) + "\n");
        isSuccess = true;
      }
    }
    String output = strBuilder.toString();
    return new Status(output.substring(0, output.length()-1));
  }

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    createTasks(input);
    Status feedback = createStatus();
    for (Task task : listOfTasks) {
      if (!tasksManager.getPendingTasks().containsValue(task)) {
        tasksManager.addPendingTask(task);
      }
    }

    if (isSuccess) {
      history.pushCommand(this);
    }

    return feedback;
  }

  public void undo() throws UnableToExecuteCommandException {
    for (Task task : listOfTasks) {
      logger.info("Undoing task id {} with content {}", task.getId(), task.getName());
      try {
        tasksManager.deletePendingTask(task.getId());
      } catch (NoSuchTaskException e) {
        wrapLowerLevelException(e, Constants.Command.ADD);
      }
    }
  }

  public void redo() {
    for (Task task : listOfTasks) {
      task.setId(idManager.getId()); // Previous id may be used by other task
      logger.info("Re-adding task id {} with content {}", task.getId(),
          task.getName());
      tasksManager.addPendingTask(task);
    }
  }

  boolean isMultipleTasks(ParsedInput input) {
    return input.getNames().size() > 1;
  }
}
