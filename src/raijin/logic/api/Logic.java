package raijin.logic.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.IDManager;
import raijin.common.datatypes.Status;
import raijin.storage.api.History;
import raijin.storage.api.Memory;
import raijin.storage.api.TasksMap;
import raijin.storage.handler.StorageHandler;


/**
 * 
 * @author papa
 * Basically a facade class that handles all components needs
 */
public class Logic {

  private Memory memory;
  private CommandDispatcher commandDispatcher;
  private String programDirectory;      //Directory where program is running from
  private String storageDirectory;      //Directory where user wish to store data on
  private String baseConfigPath;        //Directory where base config is stored
  private String userConfigPath;        //User config file path
  private String dataPath;              //User's data file path
  
  public Logic() throws FileNotFoundException {
    initAssets();                   //Initialize required components
    setupBaseConfig();
  }
  
  private void initAssets() {
    memory = Memory.getMemory();
    commandDispatcher = CommandDispatcher.getDispatcher();

    try {
      programDirectory = StorageHandler.getJarPath();
      storageDirectory = programDirectory + Constants.NAME_USER_FOLDER; //Default location
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

  }

  /*Set up paths and retrieves information from external file*/
  public void setupEnvironment() {
    setupDataFolder();
  }

  public Status handleInput(String userInput) {
    return null;
  }
  
  
  /*Checks if this is the first time a user use the program*/
  public boolean isFirstTime() {
    //@TODO ensures that the directory path works with Windows
    String dataDirectory = programDirectory + Constants.NAME_USER_FOLDER;
    return !StorageHandler.isDirectory(dataDirectory);
  }

  /*Used for testing purpose or in case when user wants to change location of storage*/
  public void setStoragePath(String path) {
    storageDirectory = path;
    StorageHandler.writeToFile(storageDirectory, baseConfigPath);               //Write changes to base config
  }

  /*Used for testing purposes*/
  public void setProgramPath(String path) {
    programDirectory = path;
    storageDirectory = programDirectory + Constants.NAME_USER_FOLDER;
  }
  
  /*Initialize list of tasks*/
  public void initializeData(TasksMap tasksMap) {
    memory.setTasksMap(tasksMap);
    IDManager.getIdManager().setIdPool(tasksMap.getIdManager().getIdPool());
  }
  

  //===========================================================================
  // Package methods 
  //===========================================================================
  

  /* Base config independent of user's choice will be created */
  void setupBaseConfig() throws FileNotFoundException {
    String dataDirectory = programDirectory + Constants.NAME_USER_FOLDER;
    baseConfigPath = dataDirectory + Constants.NAME_BASE_CONFIG;
    StorageHandler.createDirectory(dataDirectory);                              //Create working folder 
    boolean isCreated = StorageHandler.createFile(baseConfigPath);
    if (!isCreated) {
      StorageHandler.writeToFile(storageDirectory, baseConfigPath);             //Writes default storage location to base config
    } else {
      storageDirectory = StorageHandler.getStorageDirectory(baseConfigPath);    //Get storage directory specified previously
    }
  }
  
  void setupDataFolder() {
    /*Initialize paths*/
    userConfigPath = storageDirectory + Constants.NAME_USER_CONFIG;
    dataPath = storageDirectory + Constants.NAME_USER_DATA;

    /*Setup data folder if does not exist*/
    if (!StorageHandler.isDirectory(storageDirectory)) {
      StorageHandler.createDirectory(storageDirectory);                      
      StorageHandler.createFile(userConfigPath);                                //Creates user config
      StorageHandler.createFile(dataPath);                                      //Creates data file
    }
    
  }

}
