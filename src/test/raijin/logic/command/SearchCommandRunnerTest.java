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

  @Test
  public void processCommand_PositiveMatched_NumberOfMatched() throws UnableToExecuteCommandException, NoSuchTaskException {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.SEARCH).
        name("OP1 is my only ba").createParsedInput();
    tasksManager.addPendingTask(new Task("submit OP1 to Kuma", 1));
    tasksManager.addPendingTask(new Task("play OP1 with Djinn", 2));
    Status output = searchCommandRunner.execute(input);
    
    assertEquals("Result(2) matched found", output.getFeedback());
  }

}
