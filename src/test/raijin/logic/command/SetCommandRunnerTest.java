//@@author A0112213E

package raijin.logic.command;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

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

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Rule
  public TemporaryFolder baseFolder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    session = Session.getSession();
    session.setupBase(baseFolder.getRoot().getAbsolutePath());
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
        helperOption(tmpFolder.getRoot().getAbsolutePath()).createParsedInput();
   
    setCommandRunner.execute(input);
    String currentStoragePath = session.storageDirectory;
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
