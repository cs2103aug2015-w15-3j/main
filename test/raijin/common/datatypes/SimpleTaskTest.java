package raijin.common.datatypes;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.BaseTask;
import raijin.common.datatypes.SimpleDateTime;
import raijin.common.datatypes.SimpleTask;

public class SimpleTaskTest {
  

  private SimpleTask task;

  @Before
  public void setUp(){
    this.task = new SimpleTask(1, "submit OP1 slides", new SimpleDateTime());
  }

  @Test
  public void ensureCorrectNameTest() {
    assertEquals(this.task.getName(), "submit OP1 slides");
  }

}
