package raijin.logic.api;


import raijin.common.datatypes.Status;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.TasksManager;

public abstract class CommandRunner {

  public TasksManager tasksManager = TasksManager.getManager();
  public History history = History.getHistory();
  public abstract Status execute(ParsedInput input) throws NonExistentTaskException;
}
