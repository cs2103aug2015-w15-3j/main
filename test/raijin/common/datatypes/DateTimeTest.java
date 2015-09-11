package raijin.common.datatypes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DateTimeTest {

  private DateTime date;

  @Before
  public void setUp() throws Exception {
    date = new DateTime("19/9/2015");
  }

  @Test
  public void testSpecificDate() {
    assertEquals(date.getStartDate().toString(), "2015-09-19");
    assertEquals("23:59", date.getStartTime().toString());
  }

  @Test
  public void testSpecificDateAndTime() {
    DateTime date = new DateTime("19/9/2013", "1900");
    assertEquals(date.getStartDate().toString(), "2013-09-19");
    assertEquals("19:00", date.getStartTime().toString());
  }

  @Test
  public void testSpecificEvent() {
    DateTime date = new DateTime("19/9/2013", "1900", "2100");
    assertEquals(date.getStartDate().toString(), "2013-09-19");
    assertEquals("19:00", date.getStartTime().toString());
    assertEquals("21:00", date.getEndTime().toString());
  }

  @Test
  public void testSpanEvent() {
    DateTime date = new DateTime("19/9/2013", "1900", "21/9/2013", "2100");
    assertEquals( "2013-09-19", date.getStartDate().toString());
    assertEquals( "2013-09-21", date.getEndDate().toString());
    assertEquals("19:00", date.getStartTime().toString());
    assertEquals("21:00", date.getEndTime().toString());
  }
}
