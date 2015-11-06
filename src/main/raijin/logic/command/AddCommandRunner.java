//@@author A0112213E

package raijin.logic.command;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;

/**
 * Command to add task(s)
 * @author papa
 *
 */
public class AddCommandRunner extends CommandRunner implements UndoableRedoable {

  static final String SUCCESS_SINGLE_MSG = "You have added %s successfully";
  static final String SUCCESS_MSG = "You have added the tasks successfully";
  static final String FAILURE_MSG = "Duplicate task(s) not added";
  private ArrayList<Task> listOfTasks = new ArrayList<Task>();
  private int addedTasks = 0;

  /*Generates task object from parsed user input*/
  void createTasks(ParsedInput input) {
    listOfTasks.clear();
    for (String name : input.getNames()) {
      listOfTasks.add(new Task(name, idManager.getId(), input));
    }
  }

  /*Generates status for single task*/
  Status createSingleStatus(String taskName) {
    taskName = normalizeTaskName(taskName);
    return new Status(String.format(SUCCESS_SINGLE_MSG, taskName));
  }

  /*Generates status of command execution*/
  Status createStatus() {
    /*Compares number of tasks added with those specified by user*/
    if (addedTasks == listOfTasks.size()) {                 
      if (listOfTasks.size() == 1) {
        return createSingleStatus(listOfTasks.get(0).getName());
      }
      return new Status(SUCCESS_MSG);
    } else {
      return new Status(FAILURE_MSG, false);
    }
  }

  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    createTasks(input);

    for (Task task : listOfTasks) {
      /*Adds a task if it is not a duplicate*/
      if (!tasksManager.getPendingTasks().containsValue(task)) {
        addedTasks++;
        tasksManager.addPendingTask(task);
      }
    }

    /*Push command to undo stack provided one task can be added successfully*/
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
      /*Need to get new id as previous id may be used by other task*/
      task.setId(idManager.getId()); 
      logger.info("Re-adding task id {} with content {}", task.getId(),
          task.getName());
      tasksManager.addPendingTask(task);
    }
  }

}
