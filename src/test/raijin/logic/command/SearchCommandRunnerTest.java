package raijin.logic.command;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.IDManager;
import raijin.helper.TestUtils;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class SearchCommandRunnerTest {

  private TasksManager tasksManager;
  private SearchCommandRunner searchCommandRunner;
  private static TestUtils testUtils;

  @BeforeClass
  public static void setUpClass() {
    testUtils = new TestUtils();
  }

  @Before
  public void setUp() throws Exception {
    searchCommandRunner = new SearchCommandRunner();
    tasksManager = TasksManager.getManager();
    tasksManager.setPendingTasks(new HashMap<Integer, Task>());
    IDManager.getIdManager().flushIdPool();
  }

  @Test
  public void handlePriority_Null_ReturnTrue() throws FailedToParseException {
    ParsedInput input = testUtils.createInputFromText("add nothing");
    assertTrue(searchCommandRunner.handlePriority(Mockito.mock(Task.class), null));
  }

  @Test
  public void handlePriority_Mid_ReturnFalse() throws FailedToParseException {
    Task midPriorityTask = testUtils.createTask("i know nothing", new DateTime("19/11/2011"));
    assertFalse(searchCommandRunner.handlePriority(midPriorityTask, "h"));
  }

  @Test
  public void matchOnlyTags_OneTagMatched_ReturnFalse() throws FailedToParseException {
    ParsedInput input = testUtils.createInputFromText("add nothing #me");
    Task test = new Task(input.getName(), 1, input);
    TreeSet<String> searchTags = new TreeSet<String>();
    searchTags.add("me");
    searchTags.add("you");
    
    boolean result = searchCommandRunner.matchOnlyTags(test, searchTags);
    assertFalse(result);
  }

  @Test
  public void matchOnlyTags_TwoTagMatched_ReturnTrue() throws FailedToParseException {
    ParsedInput input = testUtils.createInputFromText("add nothing #me #you");
    Task test = new Task(input.getName(), 1, input);
    TreeSet<String> searchTags = new TreeSet<String>();
    searchTags.add("me");
    searchTags.add("you");
    
    boolean result = searchCommandRunner.matchOnlyTags(test, searchTags);
    assertTrue(result);
  }

  @Test
  public void matchOnlyKeywords_Matched() throws FailedToParseException {
    Task test = testUtils.createFloatingTask("this is weird");
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("this");
    
    boolean result = searchCommandRunner.matchOnlyKeyword(keywords, test);
    assertTrue(result);
  }

  @Test
  public void matchOnlyKeywords_NotMatched() throws FailedToParseException {
    Task test = testUtils.createFloatingTask("this is weird");
    ArrayList<String> keywords = new ArrayList<String>();
    keywords.add("something");
    
    boolean result = searchCommandRunner.matchOnlyKeyword(keywords, test);
    assertFalse(result);
  }


  @Test
  public void processCommand_TagAndKeywordSearch() throws FailedToParseException, 
    UnableToExecuteCommandException {
    ParsedInput input = testUtils.createInputFromText("search this #house");
    ParsedInput task2 = testUtils.createInputFromText("add cat it is #house");
    HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    tasks.put(1, new Task("this is cat", 1));
    tasks.put(2, new Task("cat it is", 2, task2));
    tasks.put(2, new Task("this is cat", 2, task2));
    tasksManager.setPendingTasks(tasks);
    
    Status result = searchCommandRunner.processCommand(input);
    String expected = "Search results: 1 found";
    assertEquals(expected, searchCommandRunner.displayMessageSent);
  }

}
