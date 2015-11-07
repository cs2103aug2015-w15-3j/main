//@@author A0112213E

package raijin.common.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Task;
import raijin.common.filter.TagFilter;
import raijin.logic.parser.ParsedInput;
import edu.emory.mathcs.backport.java.util.Arrays;

public class TagFilterTest {

  //===========================================================================
  // Helper 
  //===========================================================================
  
  public TreeSet<String> getTags(String[] tags) {
    List<String> tmp = new ArrayList<String>(Arrays.asList(tags));
    return new TreeSet<String>(tmp);
  }

  public Task createTask(TreeSet<String> tags) {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(null).name("I am nope").
        tag(tags).createParsedInput();
    return new Task(input.getName(), 1, input);
  }

  @Before
  public void setUp() throws Exception {}

  @Test
  public void filter_DoesNotMatchAllTags() {
    TreeSet<String> limitTags = getTags(new String[] {"cs2101, cs2103"});
    TreeSet<String> input = getTags(new String[] {"cs2101"});
    List<Task> tasks = new ArrayList<Task>();
    
    //Setup tasks
    tasks.add(createTask(input));
    
    //Result
    List<Task> filtered = new TagFilter(limitTags).filter(tasks);

    assertEquals(0, filtered.size());
  }

  @Test
  public void filter_MatchAllTags() {
    TreeSet<String> limitTags = getTags(new String[] {"cs2101", "cs2103"});
    TreeSet<String> input = getTags(new String[] {"cs2101", "cs2103"});
    List<Task> tasks = new ArrayList<Task>();
    
    //Setup tasks
    tasks.add(createTask(input));
    
    //Result
    List<Task> filtered = new TagFilter(limitTags).filter(tasks);

    assertEquals(1, filtered.size());
  }

}
