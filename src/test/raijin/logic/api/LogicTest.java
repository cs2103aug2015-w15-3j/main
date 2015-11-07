//@@author A0112213E

package raijin.logic.api;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.datatypes.Status;


public class LogicTest {

  private static Logic logic;

  @Rule 
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @BeforeClass
  public static void setUpClass() throws Exception {
    logic = new Logic();
  }
  
  @Before
  public void setUp() {
    logic.getSession().setupBase(tmpFolder.getRoot().getAbsolutePath());
  }

  @After
  public void tearDown() {
    tmpFolder.delete();
  }

  @Test
  public void executeCommand_AddFloating() {
    Status result = logic.executeCommand("add something new");
    assertTrue(result.isSuccess());
  }

  @Test
  public void executeCommand_DeleteInvalidId() {
    Status result = logic.executeCommand("delete 10");
    assertFalse(result.isSuccess());
  }

}
