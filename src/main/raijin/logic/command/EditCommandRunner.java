package raijin.logic.command;

import java.util.TreeSet;

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

  private ParsedInput inputBeforeChange;
  private Task taskAfterChange;
  private Task taskBeforeChange;
  
  Task modifyTask(ParsedInput input) {
    String name = (input.getName() == "") ? taskBeforeChange.getName() : input.getName();
    String priority = (input.getPriority() != null) 
        ? input.getPriority() : taskBeforeChange.getPriority();
    TreeSet<String> tags = (input.getTags().isEmpty()) 
        ? taskBeforeChange.getTags() : input.getTags();
    DateTime date = (input.getDateTime() == null) 
        ? taskBeforeChange.getDateTime() : input.getDateTime();
    //int id = idManager.getId();    
        
    ParsedInput modifiedInput = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT)
        .name(name).dateTime(date).priority(priority).tag(tags).createParsedInput();
    
    return new Task(name, taskBeforeChange.getId(), modifiedInput);
  }
  
  Status editSuccessfulStatus() {
    String taskName = taskAfterChange.getName();
    return new Status(String.format(Constants.FEEDBACK_EDIT_SUCCESS, taskName));
  }
  
  public Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    try {
      inputBeforeChange = input;
      taskBeforeChange = tasksManager.getPendingTask(input.getId());
      tasksManager.editPendingTask(taskBeforeChange.getId());
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
      tasksManager.editPendingTask(taskAfterChange.getId());
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
    taskAfterChange.setId(idManager.getId());
    tasksManager.addPendingTask(taskAfterChange);
    
  }

}
