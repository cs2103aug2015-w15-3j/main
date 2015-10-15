package raijin.logic.command;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInputTest;
import raijin.storage.api.Session;

public class SetCommandRunnerTest {

  private Session session;
  private SetCommandRunner setCommandRunner;

  @Before
  public void setUp() throws Exception {
    session = Session.getSession();
    setCommandRunner = new SetCommandRunner();
  }

  @Test
  public void processCommand_ChangeStoragePath() throws UnableToExecuteCommandException {
    String initialStoragePath = session.storageDirectory;
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.SET).
        helperOption("/tmp").createParsedInput();
   
    setCommandRunner.execute(input);
    String currentStoragePath = session.storageDirectory;
    assertNotEquals(initialStoragePath, currentStoragePath);

  }

}
