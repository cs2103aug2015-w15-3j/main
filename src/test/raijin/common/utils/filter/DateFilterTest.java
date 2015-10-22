package raijin.common.utils.filter;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

public class DateFilterTest {

  private DateFilter dateFilter;

  //===========================================================================
  // Helper
  //===========================================================================
  
  public DateFilter getFilter(List<Task> tasks, DateTime limit) {
    return new DateFilter(tasks, limit);
  }

  @Before
  public void setUp() {}

  //===========================================================================
  // Time 
  //===========================================================================
  
  @Test
  public void isMatchedTime_SameStartTime_ReturnTrue() {
    DateTime limit = new DateTime("19/10/2015", "1000");
    
    //Not end time. 
    DateTime target = new DateTime("19/10/2015", "1000");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatchedTime(target));
  }

  @Test
  public void isMatchedTime_DifferentStartTime_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000");
    
    //Not end time. 
    DateTime target = new DateTime("19/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatchedTime(target));
  }

  @Test
  public void isMatchedTime_WithinDurationEvent_ReturnTrue() {
    DateTime limit = new DateTime("19/10/2015", "1000", "1700");
    
    DateTime target = new DateTime("19/10/2015", "1001", "1659");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatchedTime(target));
  }

  @Test
  public void isMatchedTime_OverdueEvent_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000", "1700");
    
    DateTime target = new DateTime("19/10/2015", "1200", "1900");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatchedTime(target));
  }

  @Test
  public void isMatchedTime_ExactEvent_ReturnTrue() {
    DateTime limit = new DateTime("19/10/2015", "1000", "1700");
    
    DateTime target = new DateTime("19/10/2015", "1000", "1700");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatchedTime(target));
  }

  //===========================================================================
  // Date
  //===========================================================================

  @Test
  public void isMatchedDate_SameStartDate_ReturnTrue() {
    DateTime limit = new DateTime("19/10/2015");
    
    DateTime target = new DateTime("19/10/2015");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatchedDate(target));
  }

  @Test
  public void isMatchedDate_DifferentStartDate_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015");
    
    DateTime target = new DateTime("20/10/2015");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatchedDate(target));
  }

  @Test
  public void isMatchedDate_WithinDateDuration_ReturnTrue() {
    DateTime limit = new DateTime("19/10/2015", "1000", "22/10/2015", "1200");
    
    DateTime target = new DateTime("20/10/2015", "1000", "21/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatchedDate(target));
  }

  @Test
  public void isMatchedDate_LessThanStartDate_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000", "22/10/2015", "1200");
    
    DateTime target = new DateTime("18/10/2015", "1000", "22/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatchedDate(target));
  }

  @Test
  public void isMatchedDate_MoreThanEndDate_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000", "22/10/2015", "1200");
    
    DateTime target = new DateTime("19/10/2015", "1000", "23/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatchedDate(target));
  }

  //===========================================================================
  // Date & Time combine
  //===========================================================================
  
  @Test
  public void isMatched_OnlyDate_ReturnTrue() {
    DateTime limit = new DateTime(LocalDate.of(2015, 9, 19), null);
    
    DateTime target = new DateTime("19/09/2015", "1000", "23/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatched(target));
  }

  @Test
  public void isMatched_OnlyTime_ReturnTrue() {
    DateTime limit = new DateTime(LocalTime.of(12, 0), null);
    
    DateTime target = new DateTime("19/09/2015", "1200", "23/10/2015", "1200");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertTrue(dateFilter.isMatched(target));
  }

  @Test
  public void isMatched_ConflictTime_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000", "1100");
    
    DateTime target = new DateTime("19/10/2015", "0900", "1000");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatched(target));
  }

  @Test
  public void isMatched_ConflictDate_ReturnFalse() {
    DateTime limit = new DateTime("19/10/2015", "1000", "1100");
    
    DateTime target = new DateTime("19/12/2015", "1000", "1059");
    dateFilter = getFilter(new ArrayList<Task>(), limit); 
    
    assertFalse(dateFilter.isMatched(target));
  }

}
