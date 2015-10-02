package raijin.logic.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import raijin.common.datatypes.Constants;
import raijin.ui.Raijin;

public class LogicTest {

  private Logic logic;

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    logic = new Logic(Mockito.mock(Raijin.class));
  }

  
  @Test
  public void  setupBaseConfig_CreatesBaseConfig() throws FileNotFoundException {
    logic.setProgramPath(tmpFolder.getRoot().getAbsolutePath());
    logic.setupBaseConfig();
    String expectedFilePath = tmpFolder.getRoot().getAbsolutePath()
        + Constants.NAME_USER_FOLDER + Constants.NAME_BASE_CONFIG;
    assertTrue(new File(expectedFilePath).exists());
  }

  @Test
  public void setupDataFolder_DefaultFolder_CreateFiles() {
    //Set storage path to default location
    logic.setStoragePath(tmpFolder.getRoot().getAbsolutePath() 
        + Constants.NAME_USER_FOLDER);

    String expectedFilePath = tmpFolder.getRoot().getAbsolutePath() 
        + Constants.NAME_USER_FOLDER + Constants.NAME_USER_CONFIG;
    logic.setupDataFolder();
    assertTrue(new File(expectedFilePath).exists());
  }

}
