package raijin.common.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInputTest;
import raijin.storage.api.TasksManager;
import raijin.storage.handler.StorageHandler;

public class AutoCompleteTest {

  private TasksManager tasksManager;
  private AutoComplete autoComplete;

  //===========================================================================
  // Helper method
  //===========================================================================
  
  
  public Task generateTagTask(String name, TreeSet<String> tags) {
    ParsedInput parsedInput = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(name).tag(tags).createParsedInput();
    return new Task(name, 1, parsedInput);
  }

  public void generateTasksManager() {
    TreeSet<String> tag = new TreeSet<String>();
    HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
    tag.add("cs2101");
    pendingTasks.put(1, generateTagTask("I am cute", tag));
    tag.clear();
    tag.add("cs2103");
    tag.add("anime");
    pendingTasks.put(2, generateTagTask("I am batu", tag));
    tag.clear();
    tag.add("ant");
    pendingTasks.put(3, generateTagTask("I am bubble", tag));
    tasksManager.setPendingTasks(pendingTasks);
  }

  @Before
  public void setUp() throws Exception {
    tasksManager = TasksManager.getManager();
    autoComplete = new AutoComplete(tasksManager);
  }

  @Test
  public void getLastWord_SingleString() {
    String input = "add";
    String lastWord = autoComplete.getLastWord(autoComplete.getTokens(input));
    assertEquals("add", lastWord);
  }
  
  
  @Test
  public void getLastWord_NormalAddCommand() {
    String input = "add submit op";
    String lastWord = autoComplete.getLastWord(autoComplete.getTokens(input));
    assertEquals("op", lastWord);
  }

  @Test
  public void updateSuggestions_AddCommand() {
    String input = "ad";
    autoComplete.updateSuggestions(input);
    assertEquals("add", autoComplete.suggestions.get(0));
  }

  @Test
  public void updateSuggestions_MultipleCommand() {
    String input = "d";
    autoComplete.updateSuggestions(input);
    assertEquals(3, autoComplete.suggestions.size());
  }
  
  @Test
  public void updateSuggestions_Tag() {
    generateTasksManager();
    autoComplete.loadTagList(TaskUtils.getTags(tasksManager.getPendingTasks()));
    String input = "add submit op1 #cs2101";
    autoComplete.updateSuggestions(input);
    assertEquals(1, autoComplete.suggestions.size());
    input = "add submit op1 #an";
    autoComplete.updateSuggestions(input);
    assertEquals(2, autoComplete.suggestions.size());
  }

  @Test
  public void updateSuggestions_Task() {
    generateTasksManager();
    autoComplete.loadTaskList(TaskUtils.getTaskNames(tasksManager.getPendingTasks()));
    String input = "I am";
    autoComplete.updateSuggestions(input);
    assertEquals(3, autoComplete.suggestions.size());
  }
}

