package raijin.common.datatypes;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Task;

public class TaskTest {
  

  private Task flexibleTask;
  private Task task;

  @Before
  public void setUp(){
    task = new Task("submit op1", new DateTime());
  }
  
  @Test
  public void testNameMustNotBeNull(){
    assertNotNull(task.getName());
  }

  @Test
  public void testIdMustBeValid(){
    assertTrue(task.getId() > 0);
  }

  @Test
  public void testUniqueId(){
    flexibleTask = new Task("submit op2");
    assertNotEquals(flexibleTask.getId(), task.getId());
  }
}
