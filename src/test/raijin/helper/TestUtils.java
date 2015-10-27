package raijin.helper;

import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInput.ParsedInputBuilder;
import raijin.logic.parser.ParserInterface;
import raijin.logic.parser.SimpleParser;
import raijin.storage.api.History;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

/**
 * Contains utility functions that help to generate relevant test objects 
 * for software under test
 * @author papa
 * 
 */
public class TestUtils {


  private IDManager idManager;
  private TasksManager tasksManager;
  private Session session;
  private ParserInterface parser = new SimpleParser();
  
  /**
   * Allow one to inject assets used for testing 
   * @param idManager
   * @param tasksManager
   * @param session
   */
  public TestUtils(IDManager idManager, TasksManager tasksManager, 
      Session session) {
    this.idManager = idManager;
    this.tasksManager = tasksManager;
    this.session = session;
  }
  
  public TestUtils() {
    idManager = IDManager.getIdManager();
    tasksManager = TasksManager.getManager();
    session = Session.getSession();
  }
  
  //===========================================================================
  // Methods to create object
  //===========================================================================
  
  ParsedInputBuilder getBuilder(Constants.Command cmd) {
    return new ParsedInputBuilder(cmd);
  }

  //Directly generates an input from text
  public ParsedInput createInputFromText(String userInput) throws FailedToParseException {
    return parser.parse(userInput);
  }
  
  //Creates a task given a specific deadline
  public Task createTask(String name, DateTime deadline) {
    ParsedInput input = getBuilder(null).name(name).dateTime(deadline)
        .createParsedInput();
    return new Task(name, idManager.getId(), input);
  }

  public Task createFloatingTask(String name) {
    return new Task(name, idManager.getId());
  }

  //===========================================================================
  // Set test properties
  //===========================================================================
  
  /**
   * Reset idManager to full id pool
   * Clear tasks manager 
   * Clear history
   */
  public void reset() {
    idManager.flushIdPool();                
    tasksManager.setCompletedTasks(new HashMap<Integer, Task>());
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    History.getHistory().clear();
  }
  

}
