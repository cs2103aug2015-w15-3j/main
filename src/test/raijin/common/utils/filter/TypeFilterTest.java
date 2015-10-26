package raijin.common.utils.filter;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInputTest;

public class TypeFilterTest {

  private static ArrayList<Task> tasks;

  //===========================================================================
  // Helper
  //===========================================================================
  
  static Task createTask(String name, DateTime dateTime) {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(null).name(name)
        .dateTime(dateTime).priority("m").createParsedInput();
    return new Task(name, 1, input);
  }

  //===========================================================================
  // Tests
  //===========================================================================
  
  @BeforeClass
  public static void setUpClass() {
    tasks = new ArrayList<Task>();
    ParsedInput input = new ParsedInput.ParsedInputBuilder(null).priority("m")
        .createParsedInput();
    tasks.add(createTask("I am weird", new DateTime("23/10/2010", "2300")));
    tasks.add(createTask("I am weird", new DateTime("23/10/2010", "2300", "2340")));
    tasks.add(new Task("I am floating", 1, input));
    tasks.add(new Task("I am flying", 1, input));
  }

  @Test
  public void filter_FloatingTasks() {
    TypeFilter type = new TypeFilter(Constants.TYPE_TASK.FLOATING);
    List<Task> filtered = type.filter(tasks);
    
    assertEquals(2, filtered.size());
  }

  @Test
  public void filter_Event() {
    TypeFilter type = new TypeFilter(Constants.TYPE_TASK.EVENT);
    List<Task> filtered = type.filter(tasks);
    
    assertEquals(1, filtered.size());
  }

  @Test
  public void filter_OverdueDate() {
    TypeFilter type = new TypeFilter(Constants.TYPE_TASK.OVERDUE);
    List<Task> filtered = type.filter(tasks);
    
    assertEquals(2, filtered.size());
  }

  @Test
  public void filter_OverdueTime() {
    DateTime late = new DateTime(LocalDate.now(), null, LocalDate.now(), 
        LocalTime.now().minusMinutes(2L));

    tasks.add(createTask("I am late", late));

    TypeFilter type = new TypeFilter(Constants.TYPE_TASK.OVERDUE);
    List<Task> filtered = type.filter(tasks);
    
    assertEquals(3, filtered.size());
  }

  @Test
  public void filter_OverdueDateTime() {
    DateTime today = new DateTime("26/10/2015", "2359");
    ArrayList<Task> nothing = new ArrayList<Task>();
    nothing.add(createTask("I am late", today));

    TypeFilter type = new TypeFilter(Constants.TYPE_TASK.OVERDUE);
    List<Task> filtered = type.filter(nothing);
    assertEquals(0, filtered.size());
  }
}
