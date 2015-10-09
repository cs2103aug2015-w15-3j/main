package raijin.common.datatypes;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
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
    task = new Task("submit op1", idManager.getId());
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
    TreeSet<String> tags = new TreeSet<String>();
    tags.add("cs2101");
    task.addTags(tags);
    System.out.println(task.getTags().toString());
    assertTrue(task.getTags().contains("cs2101"));
  }
  
  @Test
  public void testKeyWords() {
    assertArrayEquals(task.getKeywords(), new String[] {"submit", "op1"});
  }

}
