package raijin.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;
import raijin.logic.api.Logic;
import raijin.storage.api.Memory;
import raijin.storage.api.TasksMap;
import static raijin.storage.handler.StorageHandler.*;

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
    logic = new Logic();
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
  public void writeDataToFile(String dataPath, TasksMap tasks) {
    writeToFile(convertToJson(tasks), dataPath);
  }

  public TasksMap readDataFromFile(String dataPath) {
    JsonReader jsonReader = getJsonReaderFromFile(dataPath);
    return readFromJson(jsonReader, Constants.tasksType);
  }
  
  /*Add random tasks to current tasksMap*/
  public void addRandomTasks() {
    Memory memory = Memory.getMemory();
    for (int i = 0; i < sampleTasks.length; i++) {
      memory.addTask(new Task(sampleTasks[i]));
    }
  }

  /*Reset states of memory*/
  public void resetState() {
    IDManager.getIdManager().flushIdPool();
    Memory.getMemory().setTasksMap(new TasksMap());
  }

  //===========================================================================
  // Test cases
  //===========================================================================

  @Test
  public void ValidateStateOfIdManager() {
    //Setup 
    logic.setupEnvironment();
    
    
    //Make some changes to program internal memory
    addRandomTasks();
    writeDataToFile(dataPath, Memory.getMemory().getTasksMap());
    System.out.println(IDManager.getIdManager().getIdPool().toString());
    
    //Reset & read from file
    resetState();
    TasksMap deserialized = readDataFromFile(dataPath);
    logic.initializeData(deserialized);
    assertEquals(50 - sampleTasks.length, IDManager.getIdManager().getIdPool().size());

  }
}