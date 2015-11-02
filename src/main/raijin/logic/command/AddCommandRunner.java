//@@author A0112213E

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
  private int addedTasks = 0;

  void createTasks(ParsedInput input) {
    listOfTasks.clear();
    for (String name : input.getNames()) {
      listOfTasks.add(new Task(name, idManager.getId(), input));
    }
    currentTask = listOfTasks.get(0);
  }

  Status createStatus() {
    if (addedTasks > 0) {
      return new Status("You have added the task(s) successfully");
    } else {
      return new Status("Some task(s) already exist", false);
    }
  }

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    createTasks(input);
    for (Task task : listOfTasks) {
      if (!tasksManager.getPendingTasks().containsValue(task)) {
        addedTasks++;
        tasksManager.addPendingTask(task);
      }
    }

    if (addedTasks > 0) {
      history.pushCommand(this);
    }

    return createStatus();
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
