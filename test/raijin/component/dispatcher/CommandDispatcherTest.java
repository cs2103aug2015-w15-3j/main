package raijin.component.dispatcher;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class CommandDispatcherTest {

  private CommandDispatcher dispatcher;
  private UserInput input = new UserInput("put", 
      new String[]{"submit"}, "add submit");

  @Before
  public void setUp() throws Exception {
    dispatcher = CommandDispatcher.getDispatcher();
  }

  @Test
  public void testUnknownCommand() {
    assertEquals(Status.ERROR_UNKNOWN_COMMAND, 
        dispatcher.handleCommand(input));
  }

}
