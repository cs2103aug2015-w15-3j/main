package raijin.storage.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.exception.StorageFailureException;
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
  public boolean isFirstTime;           //Verify if user has used this application before
  
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
      setupBase(basePath);
      setupStorage();
    } catch (UnsupportedEncodingException e) {
      throw new StorageFailureException("Unsupported Encoding while getting program path", e);
    } 
  }
  
  public void setupBase(String basePath) {
    programDirectory = basePath;
    isFirstTime = StorageHandler.createDirectory(programDirectory);   //Create working folder 
    baseConfigPath = programDirectory + Constants.NAME_BASE_CONFIG;
    setupBaseConfig(baseConfigPath);
  }

  /*Checks if this is the first time a user use the program*/
  public boolean isFirstTime() {
    return !StorageHandler.isDirectory(programDirectory);
  }

  public void setStorageDirectory(String desiredPath, String baseConfigPath) {
    writeToFile(desiredPath, baseConfigPath);
    setupStorage();                             //trigger setup of storage after deciding storage path
  }
  
  /*Commit changes to tasks to a temp file*/
  public void commit() {
    writeToFile(StorageHandler.convertToJson(TasksManager.getManager()), tempPath);
  }
  

  /*Needed to be separated from init to ensure user has chosen a storage location*/
  public void setupStorage() {
    /*Always read from baseConfigPath to get path to ensure consistency*/
    storageDirectory = getStorageDirectory(baseConfigPath);
    dataPath = storageDirectory + Constants.NAME_USER_DATA;
    userConfigPath = storageDirectory + Constants.NAME_USER_CONFIG;
    StorageHandler.createDirectory(storageDirectory + Constants.NAME_USER_FOLDER);       
    setupDataFolder();
    setupTempPath(StorageHandler.createTempFile(Constants.NAME_TEMP_DATA));
  }
  
  public String getPathInfo() {
    return programDirectory + "\n" + storageDirectory + "\n" 
        + baseConfigPath + "\n" + dataPath + "\n" + userConfigPath;
  }

  void setupBaseConfig(String baseConfigPath) {
    boolean isSuccessful = createNewFile(baseConfigPath);
    if (isSuccessful) {
      writeToFile(programDirectory, baseConfigPath);
    }
  }

  void setupDataFolder() {
    createNewFile(userConfigPath);                              
    createNewFile(dataPath);                                    
  }

  void setupTempPath(String tempPath) {
    this.tempPath = tempPath;
  }

  //===========================================================================
  // IOException Handling 
  //===========================================================================
  
  public void writeOnExit() {
    Path source = Paths.get(tempPath);
    Path target = Paths.get(dataPath);
    try {
      StorageHandler.copyFiles(source, target);
    } catch (IOException e) {
      throw new StorageFailureException(String.format("Unable to copy temp file from %s to "
          + "overwrite %s", tempPath, dataPath), e);
    }
  }

  void writeToFile(String output, String filePath) {
    try {
      StorageHandler.writeToFile(output, filePath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format(
          "Failed to write \"%s\" to %s", output, filePath), e);
    }
  }

  /*Create new file with exception handling*/
  boolean createNewFile(String filePath) {
    try {
      return StorageHandler.createFile(filePath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format("Cannot create file %s",
          filePath), e);
    }
  }
  
  String getStorageDirectory(String baseConfigPath) {
    try {
      return StorageHandler.getStorageDirectory(baseConfigPath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format("Cannot get storage directory from file %s",
          baseConfigPath), e);
    }
  }

}
