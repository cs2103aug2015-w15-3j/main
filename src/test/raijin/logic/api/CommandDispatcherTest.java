package raijin.logic.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;

public class CommandDispatcherTest {

  private CommandDispatcher commandDispatcher;

  @Before
  public void setUp() throws Exception {
    commandDispatcher = CommandDispatcher.getDispatcher();
  }

  @Test
  public void getSizeOfCommandRunners_SameAsAvailableCommands() {
    //returns number of available commands to user
    int expected = Constants.Command.values().length;
    int result = commandDispatcher.getSizeOfCommandRunners();
    assertEquals(expected, result);
  }

}
