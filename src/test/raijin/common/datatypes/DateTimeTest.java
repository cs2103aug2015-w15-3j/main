//@@author A0112213E

package raijin.common.datatypes;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {

  private DateTime date;

  @Before
  public void setUp() throws Exception {
    date = new DateTime("19/09/2015");
  }

  @Test
  public void testSpecificDate() {
    assertEquals(date.getStartDate().toString(), "2015-09-19");
    assertEquals("23:59", date.getEndTime().toString());
  }

  @Test
  public void testSpecificDateAndTime() {
    DateTime date = new DateTime("19/09/2013", "1900");
    assertEquals(date.getEndDate().toString(), "2013-09-19");
    assertEquals("19:00", date.getEndTime().toString());
  }

  @Test
  public void testSpecificEvent() {
    DateTime date = new DateTime("19/09/2013", "1900", "2100");
    assertEquals(date.getStartDate().toString(), "2013-09-19");
    assertEquals("19:00", date.getStartTime().toString());
    assertEquals("21:00", date.getEndTime().toString());
  }

  @Test
  public void testSpanEvent() {
    DateTime date = new DateTime("19/09/2013", "1900", "21/09/2013", "2100");
    assertEquals( "2013-09-19", date.getStartDate().toString());
    assertEquals( "2013-09-21", date.getEndDate().toString());
    assertEquals("19:00", date.getStartTime().toString());
    assertEquals("21:00", date.getEndTime().toString());
  }
  
  //===========================================================================
  // Compare Start Time
  //===========================================================================
  
  @Test
  public void compareStartTime_EqualTimes() {
    //Same start time, same end time
    LocalTime sourceEnd = LocalTime.of(12, 0);
    LocalTime targetEnd = LocalTime.of(12, 0);
    DateTime source = new DateTime(sourceEnd, null);
    
    assertEquals(0, source.compareStartTime(sourceEnd, targetEnd));
  }
  
  @Test
  public void compareStartTime_EarlierThan() {
    //Same start time, earlier end time
    LocalTime sourceEnd = LocalTime.of(11, 59);
    LocalTime targetEnd = LocalTime.of(12, 0);
    DateTime source = new DateTime(sourceEnd, null);
    
    assertEquals(-1, source.compareStartTime(sourceEnd, targetEnd));
  }

  @Test
  public void compareStartTime_LaterThan() {
    //Same start time, earlier end time
    LocalTime sourceEnd = LocalTime.of(11, 1);
    LocalTime targetEnd = LocalTime.of(11, 0);
    DateTime source = new DateTime(sourceEnd, null);
    
    assertEquals(1, source.compareStartTime(sourceEnd, targetEnd));
  }

  @Test
  public void compareStartTime_NullSource() {
    //Same start time, no end time
    LocalTime targetEnd = LocalTime.of(12, 0);
    DateTime source = new DateTime(null, targetEnd);
    
    assertEquals(1, source.compareStartTime(null, targetEnd));
  }

  @Test
  public void compareStartTime_NullTarget() {
    //Same start time, no end time
    LocalTime sourceEnd = LocalTime.of(12, 0);
    DateTime source = new DateTime(null, sourceEnd);
    
    assertEquals(-1, source.compareStartTime(sourceEnd, null));
  }
  
  //===========================================================================
  // Compare Date & Time
  //===========================================================================
  
  @Test
  public void compareTo_EarlierEndDate() {
    DateTime source = new DateTime("18/10/2015", "1000", "20/10/2015", "1200");
    DateTime target = new DateTime("18/10/2015", "1000", "21/10/2015", "1200");
    assertEquals(-1, source.compareTo(target));
  }
  

  @Test
  public void compareTo_SameEndDate_EarlierTime() {
    DateTime source = new DateTime("18/10/2015", "1000", "20/10/2015", "1200");
    DateTime target = new DateTime("18/10/2015", "1001", "20/10/2015", "1200");
    assertEquals(-1, source.compareTo(target));
  }

  @Test
  public void compareTo_SameEndDate_EarlierEndTime() {
    DateTime source = new DateTime("18/10/2015", "1000", "20/10/2015", "1159");
    DateTime target = new DateTime("18/10/2015", "1000", "20/10/2015", "1200");
    assertEquals(-1, source.compareTo(target));
  }

  @Test
  public void compareTo_SameEndDate_LaterEndTime() {
    DateTime source = new DateTime("18/10/2015", "1000", "20/10/2015", "1200");
    DateTime target = new DateTime("18/10/2015", "1000", "20/10/2015", "1159");
    assertEquals(1, source.compareTo(target));
  }

  @Test
  public void compareTo_SameDate_EarlierEndTime() {
    DateTime source = new DateTime("25/10/2015", "1200");
    DateTime target = new DateTime(LocalDate.now(), null, LocalDate.now(), 
          LocalTime.now());
    assertTrue(source.compareTo(target) < 0);
  }
}
