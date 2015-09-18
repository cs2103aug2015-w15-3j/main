package raijin.logic.api;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

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
  private String currentDirectory;              //Records down current directory
  
  public Logic() {
    setupEnvironment();
  }
  public Status handleInput(String userInput) {
    return null;
  }
  
  private void setupEnvironment() {
    memory = Memory.getMemory();
    history = History.getHistory();
    //commandDispatcher = CommandDispatcher.getDispatcher();
    try {
      currentDirectory = StorageHandler.getJarPath();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
  
  /*Checks if this is the first time a user use the program*/
  public boolean isFirstTime() {
    //@TODO ensures that the directory path works with Windows
    String dataPath = currentDirectory+Constants.NAME_USER_FOLDER;
    return !StorageHandler.isDirectory(dataPath);
  }

  /*Used for testing purpose or in case when user wants to change location of storage*/
  public void setCurrentPath(String path) {
    currentDirectory = path;
  }

}
