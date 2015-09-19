package raijin.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.logic.api.Logic;
import raijin.storage.api.Memory;
import raijin.storage.api.TasksMap;
import static raijin.storage.handler.StorageHandler.*;

public class LogicStorageIntegrationTest {

  private static Logic logic;
  private String programPath;
  private String dataPath;
  private String userConfigPath;

  @Rule public TemporaryFolder programDirectory = new TemporaryFolder();

  @BeforeClass
  public static void setUpClass() throws FileNotFoundException {
    logic = new Logic();
  }

  @Before
  public void setUp() throws Exception {
    //Initialize assets
    programPath = programDirectory.getRoot().getAbsolutePath();
    dataPath = programPath + Constants.NAME_USER_FOLDER + Constants.NAME_USER_DATA;
    userConfigPath = programPath + Constants.NAME_USER_FOLDER + Constants.NAME_USER_CONFIG;
    
    //Set program directory 
    logic.setProgramPath(programPath);
  }

  //===========================================================================
  // Helper methods
  //===========================================================================
  public TasksMap createSampleData() {
    TasksMap tasksMap = new TasksMap();
    tasksMap.addTask(new Task("submit op1", new DateTime("19/09/2013")));
    tasksMap.addTask(new Task("fencing day", new DateTime("13/04/2013")));
    tasksMap.addTask(new Task("watch movie with sam", new DateTime("10/09/2013")));
    tasksMap.addTask(new Task("pray to god", new DateTime("06/06/2013")));
    tasksMap.addTask(new Task("midterm for CS2103T", new DateTime("19/04/2013")));
    return tasksMap;
  }

  public void writeDataToFile(String dataPath, TasksMap tasks) {
    writeToFile(convertToJson(tasks), dataPath);
  }

  public TasksMap readDataFromFile(String dataPath) {
    JsonReader jsonReader = getJsonReaderFromFile(dataPath);
    return readFromJson(jsonReader, Constants.tasksType);
  }

  //===========================================================================
  // Test cases
  //===========================================================================

  @Test
  public void ValidateStoredDataTest() {
    //Using default folder
    logic.setupEnvironment();
    
    TasksMap sampleData = createSampleData();
    writeDataToFile(dataPath, sampleData);
    
    //Initialize internal data from external json file
    logic.initializeData(sampleData);
    Memory memory = Memory.getMemory();
    assertEquals("submit op1", memory.getTask(1).getName());
  }

}
