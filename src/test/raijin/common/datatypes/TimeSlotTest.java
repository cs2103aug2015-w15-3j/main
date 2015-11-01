package raijin.common.datatypes;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import raijin.helper.TestUtils;

public class TimeSlotTest {

  private static TestUtils testUtils;
  private static ArrayList<Task> pendingTasks;
  private static LocalDate testDate;

  @BeforeClass
  public static void setUpClass() {
    testUtils = new TestUtils();
    pendingTasks = new ArrayList<Task>();
    testDate = LocalDate.of(2015, 12, 12);
  }

  @Before
  public void setUp() {
    pendingTasks.clear();
  }

  @Test
  public void getEvents_OneMatched() {
    //Testing for event that spans several days
    pendingTasks.add(testUtils.createTask("Normal task", new DateTime(testDate, 
        testDate.plusDays(2L))));

    //Testing for task with specific date
    pendingTasks.add(testUtils.createTask("Specific deadline", new DateTime(
        "12/12/2015", "1000")));
    
    pendingTasks.add(testUtils.createTask("event", new DateTime(
        "12/12/2015", "1000", "1200")));
    
    TimeSlot timeSlot = new TimeSlot(testDate, pendingTasks);
    List<Task> result = timeSlot.getEvents();
    
    assertEquals(1, result.size());
  }

  @Test
  public void getOccupiedSlots_Test1() {

    pendingTasks.add(testUtils.createTask("event 1", new DateTime(testDate,
        LocalTime.of(7, 0), testDate, LocalTime.of(9, 0))));

    pendingTasks.add(testUtils.createTask("event 2", new DateTime(testDate,
        LocalTime.of(15, 0), testDate, LocalTime.of(17, 0))));

    //Overlap event
    pendingTasks.add(testUtils.createTask("event 3", new DateTime(testDate,
        LocalTime.of(16, 0), testDate, LocalTime.of(16, 45))));

    //Long event
    pendingTasks.add(testUtils.createTask("event 4", new DateTime(testDate,
        LocalTime.of(12, 0), testDate, LocalTime.of(19, 0))));

    TimeSlot timeSlot = new TimeSlot(testDate, pendingTasks);
    LocalTime result = timeSlot.getOccupiedSlots().get(0).getStartTime();
    
    assertTrue(result.equals(LocalTime.of(12, 0)));
    
  }
  
  @Test
  public void getDuration() {
    LocalTime start = LocalTime.of(5, 45);
    LocalTime end = LocalTime.of(6, 50);
    TimeSlot timeSlot = new TimeSlot(testDate, pendingTasks);
    
    assertEquals(65, timeSlot.getDuration(start, end));
  }

  @Test
  public void streamlineEvents_Test1() {

    pendingTasks.add(testUtils.createTask("event 0", new DateTime(testDate,
        LocalTime.of(5, 0), testDate, LocalTime.of(6, 0))));

    pendingTasks.add(testUtils.createTask("event 1", new DateTime(testDate,
        LocalTime.of(20, 0), testDate, LocalTime.of(22, 0))));

    pendingTasks.add(testUtils.createTask("event 2", new DateTime(testDate,
        LocalTime.of(8, 0), testDate, LocalTime.of(12, 0))));

    //Overlap event
    pendingTasks.add(testUtils.createTask("event 3", new DateTime(testDate,
        LocalTime.of(16, 0), testDate, LocalTime.of(16, 45))));

    //Long event
    pendingTasks.add(testUtils.createTask("event 4", new DateTime(testDate,
        LocalTime.of(12, 0), testDate, LocalTime.of(19, 0))));

    pendingTasks.add(testUtils.createTask("event 5", new DateTime(testDate,
        LocalTime.of(20, 20), testDate, LocalTime.of(23, 0))));

    TimeSlot timeSlot = new TimeSlot(testDate, pendingTasks);

    List<DateTime> result = timeSlot.streamlineEvents();
    assertEquals(3, result.size());

  }
}
