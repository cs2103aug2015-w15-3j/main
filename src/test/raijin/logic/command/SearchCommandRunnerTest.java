package raijin.logic.command;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class SearchCommandRunnerTest {

  private TasksManager tasksManager;
  private SearchCommandRunner searchCommandRunner;

  @Before
  public void setUp() throws Exception {
    searchCommandRunner = new SearchCommandRunner();
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
  }

}
