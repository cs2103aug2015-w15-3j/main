package raijin.logic.api;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.datatypes.Constants;

public class LogicTest {

  private Logic logic;

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    logic = new Logic();
  }

  @Test
  public void isFirstTime_FirstTime_ReturnTrue() {
    assertTrue(logic.isFirstTime());
  }
  
  @Test
  public void  setupBaseStorage_CreatesBaseConfig() {
    logic.setProgramPath(tmpFolder.getRoot().getAbsolutePath());
    logic.setupBaseStorage();
    String expectedFilePath = tmpFolder.getRoot().getAbsolutePath()
        + Constants.NAME_USER_FOLDER + Constants.NAME_BASE_CONFIG;
    assertTrue(new File(expectedFilePath).exists());
  }

}
