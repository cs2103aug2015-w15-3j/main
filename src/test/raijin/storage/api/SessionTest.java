//@@author A0112213E

package raijin.storage.api;

import static org.junit.Assert.*;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.datatypes.Constants;
import raijin.common.exception.StorageFailureException;
import raijin.storage.handler.StorageHandler;

public class SessionTest {

  private static Session session;
  private static String invalidJsonFile = "/test-classes/invalid.json";
  private static String wrongFormatFile = "/test-classes/wrongFormat.json";
  private static String nothing = "/nothing";

  @Rule public TemporaryFolder tmpFolder = new TemporaryFolder();

  @BeforeClass
  public static void setUpClass() throws Exception {
    session = Session.getSession();
    invalidJsonFile = StorageHandler.getJarPath() + invalidJsonFile;
    wrongFormatFile = StorageHandler.getJarPath() + wrongFormatFile;
    nothing = StorageHandler.getJarPath() + nothing;
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
    new File(invalidJsonFile).delete();
    new File(wrongFormatFile).delete();
    new File(nothing).delete();
  }

  @Test
  public void testProgramDirectory_MatchWithTargetDirectory() throws UnsupportedEncodingException {
    String expected = StorageHandler.getJarPath() + Constants.NAME_USER_FOLDER;
    session.setupBase(expected);
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
        + "/nothing");
  }
}
