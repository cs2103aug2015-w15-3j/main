//@@author A0112213E

package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.filter.SortFilter;
import raijin.logic.parser.ParsedInput;

public class SortFilterTest {

  private static ArrayList<Task> tasks;
  private static ArrayList<Task> tasks2;

  //===========================================================================
  // Helper
  //===========================================================================
  
  static ParsedInput createNormalTask(DateTime dateTime, String priority, TreeSet<String> tags) {
    return new ParsedInput.ParsedInputBuilder(null).dateTime(dateTime)
        .priority(priority).tag(tags).createParsedInput();
  }

  static TreeSet<String> createTags(String[] tags) {
    return new TreeSet<String>(Arrays.asList(tags));
  }


  @BeforeClass
  public static void setUpClass() {
    tasks = new ArrayList<Task>();
    tasks2 = new ArrayList<Task>();

    ParsedInput input1 = createNormalTask(new DateTime("13/03/2011"), "l", createTags(
        new String[] {"cs2101"}));

    ParsedInput input2 = createNormalTask(new DateTime("18/03/2011"), "h", createTags(
        new String[] {"cs2101", "cs2103", "cs1001"}));

    ParsedInput input3 = createNormalTask(new DateTime("15/03/2011"), "m", createTags(
        new String[] {"cs2101", "cs2103"}));

    ParsedInput input4 = createNormalTask(new DateTime("13/03/2011", "1000"), "l", createTags(
        new String[] {"cs2101"}));

    ParsedInput input5 = createNormalTask(new DateTime("13/03/2011", "1100", "1300"), "l", createTags(
        new String[] {"cs2101"}));

    ParsedInput input6 = createNormalTask(new DateTime("13/03/2011", "1100", "1200"), "l", createTags(
        new String[] {"cs2101"}));

    tasks.add(new Task("I like goku", 1, input1));
    tasks.add(new Task("I like babu", 2, input2));
    tasks.add(new Task("I like moon", 3, input3));
    
    tasks2.add(new Task("submit op1", 4, input4));
    tasks2.add(new Task("submit op1", 5, input5));
    tasks2.add(new Task("submit op1", 6, input6));
  }

  @Test
  public void sortByDeadline() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.DEADLINE);
    List<Task> result = sort.filter(tasks);
    
    assertEquals(1, result.get(0).getId());
    assertEquals(3, result.get(1).getId());
    assertEquals(2, result.get(2).getId());
  }

  @Test
  public void sortByDeadline_Reversed() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.DEADLINE);
    sort.setReverse();
    List<Task> result = sort.filter(tasks);
    
    assertEquals(2, result.get(0).getId());
    assertEquals(3, result.get(1).getId());
    assertEquals(1, result.get(2).getId());
  }

  @Test
  public void sortByPriority() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.PRIORITY);
    List<Task> result = sort.filter(tasks);
    
    assertEquals(2, result.get(0).getId());
    assertEquals(3, result.get(1).getId());
    assertEquals(1, result.get(2).getId());
  }

  @Test
  public void sortByName() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.NAME);
    List<Task> result = sort.filter(tasks);
    
    assertEquals(2, result.get(0).getId());
    assertEquals(1, result.get(1).getId());
    assertEquals(3, result.get(2).getId());
  }

  @Test
  public void sortByTags() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.TAG);
    List<Task> result = sort.filter(tasks);
    
    assertEquals(1, result.get(0).getId());
    assertEquals(3, result.get(1).getId());
    assertEquals(2, result.get(2).getId());
  }

  @Test
  public void sortByTime() {
    SortFilter sort = new SortFilter(Constants.SORT_CRITERIA.DEADLINE);
    List<Task> result = sort.filter(tasks2);
    
    assertEquals(4, result.get(0).getId());
    assertEquals(6, result.get(1).getId());
    assertEquals(5, result.get(2).getId());
  }

}
