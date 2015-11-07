//@@author A0112213E

package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.logic.parser.ParsedInput;

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
  
  @Test
  public void getOnlyNormalTasks() {
    List<Task> pendingTasks = new ArrayList<Task>();
    pendingTasks.add(createSpecificTask("I am me", new DateTime("19/09/2011")));
    pendingTasks.add(createSpecificTask("I am me", new DateTime("21/09/2011")));
    pendingTasks.add(new Task("floating task 1", 3));
    pendingTasks.add(new Task("floating task 2", 4));
    
    List<Task> result = TaskUtils.getOnlyNormalTasks(pendingTasks);
    
    assertEquals(2, result.size());
  }

  @Test
  public void getTags_MultipleSimilarTags() {
    HashMap<Integer, Task> pendingTasks = new HashMap<Integer, Task>();
    pendingTasks.put(1, createTagTask(new String[]{"cs2101"}));
    pendingTasks.put(2, createTagTask(new String[]{"cs2103"}));
    pendingTasks.put(3, createTagTask(new String[]{"cs2103"}));

    TreeSet<String> result = TaskUtils.getTags(pendingTasks);
    System.out.println(result.toString());
    assertEquals(2, result.size());
  }
}
