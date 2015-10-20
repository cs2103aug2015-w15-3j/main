package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInputTest;

public class TaskUtilsTest {

  //===========================================================================
  // Helper methods
  //===========================================================================
  
  public Task createTagTask(String[] tags) {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(null).name("me no more").
        tag(new TreeSet<String>(Arrays.asList(tags))).createParsedInput();
    return new Task(input.getName(), 1, input);
  }

  public Task createSpecificTask(String name, DateTime dateTime) {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(null).name(name).
        dateTime(dateTime).createParsedInput();
    return new Task(input.getName(), 1, input);
  }

  @Before
  public void setUp() throws Exception {}

  @Test
  public void filterTaskWithTags() {
    //Create tags
    TreeSet<String> tags = new TreeSet<String>();
    tags.add("cs2101");
    tags.add("cs2103");

    //Tasks
    HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
    pendingTasks.put(1, createTagTask(new String[]{"cs2101"}));
    pendingTasks.put(2, createTagTask(new String[]{"cs2103, cs2101"}));
    pendingTasks.put(3, createTagTask(new String[]{"cs2103"}));

    List<Task> result = TaskUtils.filterTaskWithTags(pendingTasks, tags);
    assertEquals(2, result.size());
  }

  @Test
  public void filterTaskWithNames() {
    HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
    pendingTasks.put(1, createSpecificTask("I am me", new DateTime("19/09/2011")));
    pendingTasks.put(2, createSpecificTask("I am me", new DateTime("21/09/2011")));
    pendingTasks.put(3, createSpecificTask("I am me", new DateTime("01/08/2011")));
    
    List<Task> result = TaskUtils.filterTaskWithName(pendingTasks, "I am me");
    
    assertEquals(3, result.size());
  }
}