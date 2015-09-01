package raijin.util;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.util.Task.BaseTask;
import raijin.util.Task.SimpleTask;
import raijin.util.Time.SimpleDateTime;

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
