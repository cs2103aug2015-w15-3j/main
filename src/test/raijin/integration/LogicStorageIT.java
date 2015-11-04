//@@author A0112213E

package raijin.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.utils.IDManager;
import raijin.logic.api.Logic;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;
import raijin.storage.handler.StorageHandler;
import raijin.ui.Raijin;
import static raijin.storage.handler.StorageHandler.*;
import static org.mockito.Mockito.*;

public class LogicStorageIT {

  private static String[] sampleTasks;
  private static Logic logic;
  private static Session session;
  private static IDManager idManager;
  private String programPath;
  private String dataPath;
  private String userConfigPath;

  @Rule public TemporaryFolder programDirectory = new TemporaryFolder();
  @Rule public TemporaryFolder storageDirectory = new TemporaryFolder();


  @BeforeClass
  public static void setUpClass() throws Exception {
    logic = new Logic();
    session = Session.getSession();
    idManager = IDManager.getIdManager();
  }

  @Before
  public void setUp() throws IOException {
    programDirectory.newFolder("data");
    session.setupBase(programDirectory.getRoot().getAbsolutePath());
  }

  //===========================================================================
  // Helper methods
  //===========================================================================
  public <T> void writeDataToFile(String dataPath, T data) throws IOException {
    writeToFile(convertToJson(data), dataPath);
  }

  @SuppressWarnings("serial")
  public TasksManager readDataFromFile(String dataPath) throws JsonSyntaxException, IOException {
    JsonReader jsonReader = getJsonReaderFromFile(dataPath);
    return readFromJson(jsonReader, new TypeToken<TasksManager>() {}.getType());
  }
  
  /*Add random tasks to current tasksMap*/
  public void addRandomTasks() {
    TasksManager tasksManager = TasksManager.getManager();
    for (int i = 0; i < sampleTasks.length; i++) {
      tasksManager.addPendingTask(new Task(sampleTasks[i], idManager.getId()));
    }
  }

  /*Reset states of memory*/
  public void resetState() {
    IDManager.getIdManager().flushIdPool();
    TasksManager.getManager().setPendingTasks(new HashMap<Integer, Task>());
  }

  //===========================================================================
  // Test cases
  //===========================================================================

  @Test 
  public void setStorageDirectory_MatchUserLocationWithBaseConfig() throws IOException {
    String storagePath = storageDirectory.getRoot().getAbsolutePath();
    session.setStorageDirectory(storagePath, session.baseConfigPath);
    session.setupStorage();
    assertEquals(storagePath, StorageHandler.getStorageDirectory(session.baseConfigPath));
  }

}
