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
import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Task;
import raijin.logic.api.Logic;
import raijin.storage.api.Memory;
import raijin.storage.api.TasksMap;
import static raijin.storage.handler.StorageHandler.*;

public class LogicStorageIT {

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

  //===========================================================================
  // Test cases
  //===========================================================================

  @Test
  public void ValidateStoredDataTest() {
    //Using default folder
    logic.setupEnvironment();
  }
}
