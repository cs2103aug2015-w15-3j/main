package raijin.logic.api;

import java.util.HashMap;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.TasksManager;

public interface CommandRunner {

  public TasksManager tasksManager = TasksManager.getManager();
  public History history = History.getHistory();
  public Status execute(ParsedInput input) throws NonExistentTaskException;
}
