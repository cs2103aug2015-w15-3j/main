package raijin.storage.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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

}
