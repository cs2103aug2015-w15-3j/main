package raijin.storage.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.utils.RaijinLogger;
import raijin.storage.handler.StorageHandler;

public class Session {

  private static Session session = new Session();
  private Logger logger;

  public String programDirectory;      
  public String storageDirectory;
  public String baseConfigPath;
  public String userConfigPath;
  public String dataPath;
  public String tempPath;
  
  private Session() {
    init();
  } 
  
  public static Session getSession() {
    return session;
  }
  
  /*Needed to run regardless of user's choice of storage location*/
  void init() {
    logger = RaijinLogger.getLogger();
    try {
      String basePath = StorageHandler.getJarPath() + Constants.NAME_USER_FOLDER;
      setupBasePaths(basePath);
    } catch (UnsupportedEncodingException e) {
      logger.warn("Unsupported Encoding");
    }
    setupBaseConfig(baseConfigPath);
  }
  
  void setupBasePaths(String basePath) {
    programDirectory = basePath;
    StorageHandler.createDirectory(programDirectory);   //Create working folder 
    baseConfigPath = programDirectory + Constants.NAME_USER_FOLDER 
        + Constants.NAME_BASE_CONFIG;
  }

  void setupBaseConfig(String baseConfigPath) {
    boolean isCreated = StorageHandler.createFile(baseConfigPath);
    if (!isCreated) {
      StorageHandler.writeToFile(programDirectory, baseConfigPath);
    }
  }

  /*Needed to be separated from init to ensure user has chosen a storage location*/
  void setupStorage() throws FileNotFoundException {
    /*Always read from baseConfigPath to get path to ensure consistency*/
    storageDirectory = StorageHandler.getStorageDirectory(baseConfigPath);
    dataPath = storageDirectory + Constants.NAME_USER_DATA;
    userConfigPath = storageDirectory + Constants.NAME_USER_CONFIG;
    StorageHandler.createDirectory(storageDirectory);       
    setupDataFolder();
    setupTempPath(StorageHandler.createTempFile(Constants.NAME_TEMP_DATA));
  }

  void setupDataFolder() {
    StorageHandler.createFile(userConfigPath);                              
    StorageHandler.createFile(dataPath);                                    
  }

  void setupTempPath(String tempPath) {
    this.tempPath = tempPath;
  }

  /*Checks if this is the first time a user use the program*/
  public boolean isFirstTime() {
    return !StorageHandler.isDirectory(programDirectory);
  }

  public void setStorageDirectory(String desiredPath, String baseConfigPath) {
    StorageHandler.writeToFile(desiredPath, baseConfigPath);
  }
  
  /*Commit changes to tasks to a temp file*/
  public void commit() {
    StorageHandler.writeToFile(StorageHandler.convertToJson(
        TasksManager.getManager()), tempPath);
  }
  
  public void writeOnExit() throws IOException {
    Path source = Paths.get(tempPath);
    Path target = Paths.get(dataPath);
    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
  }

}
