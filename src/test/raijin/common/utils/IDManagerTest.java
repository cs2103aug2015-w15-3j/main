package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.utils.IDManager;

public class IDManagerTest {

  private IDManager idManager;

  @Before
  public void setUp() throws Exception {
    this.idManager = IDManager.getIdManager();
    idManager.setMaxId(Constants.MAX_ID);
  }

  @Test
  public void testUniqueIds() {
    assertNotEquals(idManager.getId(), idManager.getId());
  }

  @Test
  public void testReturnId() {
    int id = idManager.getId();
    TreeSet<Integer> idPool = idManager.getIdPool();
    assertFalse(idPool.contains(id));
    idManager.returnId(id);
    assertTrue(idPool.contains(id));
  }

  @Test
  public void testFlushIdPool() {
    //Get a few ids 
    int a = idManager.getId();
    
    idManager.flushIdPool();
    assertEquals(1, idManager.getId());
  }
  
  @Test
  public void initIdPoolFromPendingTasks_ReduceAvaibleIds() {
    HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    tasks.put(1, new Task("submit op2", idManager.getId()));
    tasks.put(2, new Task("submit op3", idManager.getId()));
    tasks.put(3, new Task("submit op4", idManager.getId()));
    idManager.flushIdPool();

    int expected_size = idManager.getMaxId() - tasks.size();
    idManager.updateIdPool(tasks);
    assertEquals(expected_size, idManager.getIdPool().size());
  }
  
  @Test
  public void updateIdPool_PendingTasksMoreThanMaxID_IncreaseMaxID() {
    HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    for (int i=1; i <= 500; i++) {
      tasks.put(i, new Task("I am weird", i));
    }

    idManager.updateIdPool(tasks);
    assertEquals(700, idManager.getMaxId());
  }

  @Test
  public void getId_IdPoolEmpty_ExtendIdPool() {
    idManager.setIdPool(new TreeSet<Integer>());
    int id = idManager.getId();
    assertEquals(Constants.MAX_ID+1, id);
  }

}
