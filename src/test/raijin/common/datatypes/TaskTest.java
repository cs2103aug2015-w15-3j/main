package raijin.common.datatypes;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import raijin.common.datatypes.Task;
import raijin.common.utils.IDManager;

public class TaskTest {
  

  private Task flexibleTask;
  private Task task;
  private static IDManager idManager;

  @BeforeClass
  public static void setUpClass() {
    idManager = IDManager.getIdManager();
  }

  @Before
  public void setUp(){
    task = new Task("submit op1", idManager.getId(), new DateTime("19/09/2015"));
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
    flexibleTask = new Task("submit op2", idManager.getId());
    assertNotEquals(flexibleTask.getId(), task.getId());
  }

  @Test
  public void testAddTag(){
    task.addTags("CS2101");
    assertTrue(task.getTags().contains("cs2101"));
  }
  
  @Test
  public void testKeyWords() {
    assertArrayEquals(task.getKeywords(), new String[] {"submit", "op1"});
  }

}
