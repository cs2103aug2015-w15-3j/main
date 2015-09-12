package raijin.common.datatypes;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.storage.handler.StorageHandler;

public class SessionTest {

  private Session session;

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();
  @Before
  public void setUp() throws Exception {
    session = Session.createSession(Paths.get(tmpFolder.getRoot().getAbsolutePath()));
  }

  @Test
  public void testSetupData() {
    String dirPath = tmpFolder.getRoot().getAbsolutePath()+Constants.NAME_USER_FOLDER;
    File dataFile = new File(dirPath+Constants.NAME_USER_DATA);
    File configFile = new File(dirPath+Constants.NAME_USER_CONFIG);
    assertTrue(dataFile.exists() && configFile.exists());
  }
  
  @Test
  public void testSetupConfig() {
  }
  

}
