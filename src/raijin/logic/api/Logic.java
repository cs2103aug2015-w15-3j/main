package raijin.logic.api;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.storage.api.History;
import raijin.storage.api.Memory;
import raijin.storage.handler.StorageHandler;


/**
 * 
 * @author papa
 * Basically a facade class that handles all components needs
 */
public class Logic {

  private Memory memory;
  private History history;
  private CommandDispatcher commandDispatcher;
  private String programDirectory;      //Directory where program is running from
  private String storageDirectory;      //Directory where user wish to store data on
  private String baseConfigPath;        //Directory where base config is stored
  
  public Logic() {
    initAssets();                   //Initialize required components
  }
  
  private void initAssets() {
    memory = Memory.getMemory();
    history = History.getHistory();
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
    setupBaseStorage();
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
  }

  //===========================================================================
  // Package methods 
  //===========================================================================
  
  /*Used for testing purposes*/
  void setProgramPath(String path) {
    programDirectory = path;
  }

  /* Base storage independent of user's choice will be created */
  void setupBaseStorage() {
    String dataDirectory = programDirectory + Constants.NAME_USER_FOLDER;
    baseConfigPath = dataDirectory + Constants.NAME_BASE_CONFIG;
    StorageHandler.createDirectory(dataDirectory);                 //Create working folder 
    StorageHandler.createFile(baseConfigPath);
    StorageHandler.writeToFile(storageDirectory, baseConfigPath);  //Writes default storage location to base config
  }


}
