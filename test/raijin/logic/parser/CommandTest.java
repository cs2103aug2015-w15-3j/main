package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.DateTime;

public class CommandTest {

  private Command command;

  @Before
  public void setUp() throws Exception {
    command = Command.buildSimple(1);
  }

  @Test
  public void testInitialName() {
    assertNull(command.getName());
  }

  @Test
  public void testSetValues() {
    Command command = Command.buildUndoable(0, "submit op1", new DateTime("19/09/2015"));
    assertEquals(0, command.getId());
    assertEquals("submit op1", command.getName());
  }

}
