package raijin.logic.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LogicTest {

  private Logic logic;

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    logic = new Logic();
  }

  @Test
  public void isFirstTime_FirstTime_CreateDirectory() {
    //Sets path to temporary path for testing
    logic.setCurrentPath(tmpFolder.getRoot().getAbsolutePath());
    assertTrue(logic.isFirstTime());
  }

}
