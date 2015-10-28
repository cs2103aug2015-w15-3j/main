package raijin.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junitx.framework.FileAssert;

import org.slf4j.Logger;

import static org.junit.Assert.*;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;
import raijin.logic.api.Logic;
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
  private ParserInterface parser = new SimpleParser();
  private static Logger logger = RaijinLogger.getLogger();
  
  public TasksManager tasksManager;

  public TestUtils(IDManager idManager, TasksManager tasksManager) {
    this.idManager = idManager;
    this.tasksManager = tasksManager;
  }
  
  public TestUtils() {
    this.idManager = IDManager.getIdManager();
    this.tasksManager = TasksManager.getManager();
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
  
  //===========================================================================
  // Assert helper
  //===========================================================================
  
  //Compare size and content of tasks
  public static void assertSimilarTasks(HashMap<Integer, Task> expected, 
      HashMap<Integer, Task> actual) {
    
    //Check that they are equal number of pending task
    assertEquals(expected.size(), actual.size());
    
    //Check that content is similar
    assertTrue(expected.values().equals(actual.values()));
  }
  
  //Compare JSON files produced after running test script
  public static void assertSimilarFiles(File expected, File actual) {
    FileAssert.assertEquals(expected, actual);
  }

  //===========================================================================
  // Running test
  //===========================================================================
  
  public static List<String> runCommands(String scriptPath, String outputPath, 
      Logic logic) {
    ArrayList<String> statusList = new ArrayList<String>();

    try(BufferedReader br = new BufferedReader(new InputStreamReader(
        new FileInputStream(scriptPath)))) {
      String userInput;
      
      //Run all commands found in script sequentially
      while ((userInput = br.readLine()) != null) {
        Status result = logic.executeCommand(userInput);
        statusList.add(result.getFeedback());
      }
      logger.info("Success");
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
      return statusList;
  }

}
