package raijin.storage.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

public class MemoryTest {

  private Memory memory;

  @Before
  public void setUp() throws Exception {
    memory = Memory.getMemory();
  }

  @Test
  public void testOnlySingleMemoryInstance() {
    Memory mem2 = Memory.getMemory();
    assertEquals(mem2, memory);
  }

  @Test
  public void testAddGetTask() {
    Task task1 = new Task("add flexible task");
    memory.addTask(task1);
    assertEquals("add flexible task", memory.getTask(1).getName());
  }


}
