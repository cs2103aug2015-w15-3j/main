package raijin.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.utils.IDManager;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;
import raijin.ui.Raijin;

import static raijin.storage.handler.StorageHandler.*;
import static org.mockito.Mockito.*;

public class LogicStorageIT {

  private static String[] sampleTasks;
  private Logic logic;
  private String programPath;
  private String dataPath;
  private String userConfigPath;

  @Rule public TemporaryFolder programDirectory = new TemporaryFolder();


  @Before
  public void setUp() throws Exception {
    //Initialize assets
    logic = new Logic(mock(Raijin.class));
    programPath = programDirectory.getRoot().getAbsolutePath();
    dataPath = programPath + Constants.NAME_USER_FOLDER + Constants.NAME_USER_DATA;
    userConfigPath = programPath + Constants.NAME_USER_FOLDER + Constants.NAME_USER_CONFIG;
    
    //Set program directory 
    logic.setProgramPath(programPath);
  }

  @BeforeClass
  public static void setUpClass() {
    sampleTasks = new String[] {"submit op1", "kick people", "test cat or dog", 
       "knock knock", "say hello to the monster", "leave Mordor"};
  }

  //===========================================================================
  // Helper methods
  //===========================================================================
  public <T> void writeDataToFile(String dataPath, T data) {
    writeToFile(convertToJson(data), dataPath);
  }

  @SuppressWarnings("serial")
  public TasksManager readDataFromFile(String dataPath) {
    JsonReader jsonReader = getJsonReaderFromFile(dataPath);
    return readFromJson(jsonReader, new TypeToken<TasksManager>() {}.getType());
  }
  
  /*Add random tasks to current tasksMap*/
  public void addRandomTasks() {
    TasksManager tasksManager = TasksManager.getManager();
    for (int i = 0; i < sampleTasks.length; i++) {
      tasksManager.addPendingTask(new Task(sampleTasks[i]));
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
  public void ValidateStateOfIdManager() {
    //Make some changes to program internal memory
    addRandomTasks();
    writeDataToFile(dataPath, TasksManager.getManager());
    System.out.println(IDManager.getIdManager().getIdPool().toString());
    
    //Reset & read from file
    resetState();
    TasksManager deserialized = readDataFromFile(dataPath);
    logic.initializeData(deserialized);
    assertEquals(50 - sampleTasks.length, IDManager.getIdManager().getIdPool().size());

  }
}
