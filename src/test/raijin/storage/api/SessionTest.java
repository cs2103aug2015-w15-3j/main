package raijin.storage.api;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.datatypes.Constants;
import raijin.common.exception.StorageFailureException;
import raijin.storage.handler.StorageHandler;

public class SessionTest {

  private Session session;
  private final String invalidJsonFile = "/test-classes/invalid.json";
  private final String wrongFormatFile = "/test-classes/wrongFormat.json";

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    session = Session.getSession();
  }

  @Test
  public void testProgramDirectory_MatchWithTargetDirectory() throws UnsupportedEncodingException {
    String expected = StorageHandler.getJarPath() + Constants.NAME_USER_FOLDER;
    assertEquals(expected, session.programDirectory);
  }
  
  @Test(expected = StorageFailureException.class)
  public void initTasksManager_InvalidJson() throws UnsupportedEncodingException {
    session.getDataFromJson(StorageHandler.getJarPath() + invalidJsonFile);
  }

  @Test(expected = StorageFailureException.class)
  public void initTasksManager_WrongFormat() throws UnsupportedEncodingException {
    TasksManager tasksManager = session.getDataFromJson(StorageHandler.getJarPath() 
        + wrongFormatFile);
  }

  @Test(expected = StorageFailureException.class)
  public void initTasksManager_MissingFile() throws UnsupportedEncodingException {
    TasksManager tasksManager = session.getDataFromJson(StorageHandler.getJarPath() 
        + "nothing");
  }
}
