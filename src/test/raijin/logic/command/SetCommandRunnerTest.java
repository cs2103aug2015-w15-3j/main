package raijin.logic.command;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParsedInputTest;
import raijin.storage.api.Session;
import raijin.storage.handler.StorageHandler;

public class SetCommandRunnerTest {

  private Session session;
  private SetCommandRunner setCommandRunner;
  private String storageDirectory;

  @Before
  public void setUp() throws Exception {
    session = Session.getSession();
    storageDirectory = session.storageDirectory;
    setCommandRunner = new SetCommandRunner();
  }
  
  @After
  public void tearDown() throws IOException {
    StorageHandler.writeToFile(storageDirectory, session.baseConfigPath);
  }

  @Test
  public void processCommand_ValidPath_ChangeStoragePath() throws UnableToExecuteCommandException {
    String initialStoragePath = session.storageDirectory;
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.SET).
        helperOption("/tmp").createParsedInput();
   
    setCommandRunner.execute(input);
    String currentStoragePath = session.storageDirectory;
    System.out.println(initialStoragePath);
    assertNotEquals(initialStoragePath, currentStoragePath);
  }

  @Test
  public void processCommand_InvalidPath_ReturnFail() throws UnableToExecuteCommandException {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.SET).
        helperOption("/tmp/holland").createParsedInput();
   
    Status status = setCommandRunner.execute(input);
    assertEquals(setCommandRunner.FAIL_MESSAGE, status.getFeedback());
  }
}
