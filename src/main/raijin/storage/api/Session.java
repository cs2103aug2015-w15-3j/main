//@@author A0112213E

package raijin.storage.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.exception.StorageFailureException;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;
import raijin.common.utils.TaskUtils;
import raijin.storage.handler.StorageHandler;

public class Session {

  private static final String ERROR_FAIL_GET_STORAGE = "Cannot get storage "
      + "directory from file %s";
  private static final String ERROR_ENCODING = "Unsupported Encoding while "
      + "getting program path"; 
  private static final String ERROR_COPY_FILES = "Unable to copy temp file from "
      + "%s to overwrite %s";
  private static final String ERROR_WRITING_FILES = "Failed to write \"%s\" to %s";
  private static final String ERROR_CREATING_FILE = "Cannot create file %s";
  private static final String ERROR_CORRUPTED_JSON = "Corrupted JSON file. New "
      + "JSON file is created";
  private static final String ERROR_MISSING_JSON = "Missing JSON file. New "
      + "JSON file is created";
  private static final String ERROR_READING_JSON = "Failed to read JSON file. New "
      + "JSON file is created";

  private static Session session = new Session();
  private TasksManager tasksManager;

  String userConfigPath;
  String dataPath;
  String tempPath;
  public String programDirectory;
  public String storageDirectory;
  public String baseConfigPath;
  public boolean isFirstTime; 

  private Session() {
    init();
  }

  public static Session getSession() {
    return session;
  }

  /**
   * Creates base config file that stores desired storage directory
   * @param baseConfigPath
   */
  void setupBaseConfig(String baseConfigPath) {
    boolean isSuccessful = createNewFile(baseConfigPath);
    if (isSuccessful) {
      writeToFile(programDirectory, baseConfigPath);
    }
  }

  /**
   * Creates folder to store application's data.
   * This may not be the same with storage directory.
   * @param basePath
   */
  public void setupBase(String basePath) {
    programDirectory = basePath;
    isFirstTime = StorageHandler.createDirectory(programDirectory); // Create working folder
    baseConfigPath = programDirectory + Constants.NAME_BASE_CONFIG;
    setupBaseConfig(baseConfigPath);
  }

  /**
   * Read storage directory from base.cfg file
   * @param baseConfigPath
   * @return
   */
  String getStorageDirectory(String baseConfigPath) {
    try {
      return StorageHandler.getStorageDirectory(baseConfigPath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format(ERROR_FAIL_GET_STORAGE,
          baseConfigPath), e);
    }
  }

  /*Creates data folder at storage directory*/
  void setupDataFolder() {
    createNewFile(userConfigPath);
    createNewFile(dataPath);
  }

  void generateNewFile(String filePath) {
    StorageHandler.deleteFile(filePath);
    createNewFile(filePath);
  }

  /*Initializes tasks manager with external user data stored in JSON*/
  void initTasksManager() {
    TasksManager retrievedData = getDataFromJson(dataPath);
    if (retrievedData != null) {
      tasksManager.sync(retrievedData);
      IDManager.getIdManager().updateIdPool(tasksManager.getPendingTasks());
    }
    generateNewFile(dataPath);                                                  
  }

  /*Setup temporary file that records all changes*/
  void setupTempPath(String tempPath) {
    this.tempPath = tempPath;
  }

  /* Needed to be separated from init to ensure user has chosen a storage location */
  public void setupStorage() {
    /* Always read from baseConfigPath to get path to ensure consistency */
    storageDirectory = getStorageDirectory(baseConfigPath);
    dataPath = storageDirectory + Constants.NAME_USER_DATA;
    userConfigPath = storageDirectory + Constants.NAME_USER_CONFIG;
    StorageHandler.createDirectory(storageDirectory);
    setupDataFolder();
    initTasksManager();
    setupTempPath(StorageHandler.createTempFile(Constants.NAME_TEMP_DATA));
  }

  /*Initialise basic paths where application is ran*/
  void init() {
    tasksManager = TasksManager.getManager();
    try {
      String basePath = StorageHandler.getJarPath() + Constants.NAME_USER_FOLDER;
      setupBase(basePath);
      setupStorage();
    } catch (UnsupportedEncodingException e) {
      throw new StorageFailureException(ERROR_ENCODING, e);
    }
  }

  /* Checks if this is the first time a user use the program */
  public boolean isFirstTime() {
    return !StorageHandler.isDirectory(programDirectory);
  }

  public void setStorageDirectory(String desiredPath, String baseConfigPath) {
    String sanitizedPath = StorageHandler.sanitizePath(desiredPath);
    writeToFile(sanitizedPath, baseConfigPath);
    setupStorage(); // trigger setup of storage after deciding storage path
  }

  /* Commit changes to a temp file */
  public void commit() {
    writeToFile(StorageHandler.convertToJson(TasksManager.getManager()), tempPath);
  }

  // ===========================================================================
  // IOException Handling
  // ===========================================================================

  public void writeOnExit() {
    commit();
    Path source = Paths.get(tempPath);
    Path target = Paths.get(dataPath);
    System.out.println(dataPath);
    try {
      StorageHandler.copyFiles(source, target);
    } catch (IOException e) {
      throw new StorageFailureException(String.format(ERROR_COPY_FILES, 
          tempPath, dataPath), e);
    }
  }

  void writeToFile(String output, String filePath) {
    try {
      StorageHandler.writeToFile(output, filePath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format(ERROR_WRITING_FILES, output,
          filePath), e);
    }
  }

  /* Create new file with exception handling */
  boolean createNewFile(String filePath) {
    try {
      return StorageHandler.createFile(filePath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format(ERROR_CREATING_FILE, filePath), e);
    }
  }

  @SuppressWarnings("serial")
  TasksManager getDataFromJson(String dataPath) throws StorageFailureException {
    JsonReader reader = StorageHandler.getJsonReaderFromFile(dataPath);
    try {
      TasksManager retrievedData =
          StorageHandler.readFromJson(reader, new TypeToken<TasksManager>() {}.getType());
      return retrievedData;

    } catch (JsonParseException e) {                                            // JSON file is corrupted
      generateNewFile(dataPath);                                                // Create empty JSON file
      throw new StorageFailureException(ERROR_CORRUPTED_JSON, e);

    } catch (NullPointerException e) { // JSON file is missing
      createNewFile(dataPath);
      throw new StorageFailureException(ERROR_MISSING_JSON, e);

    } catch (IOException e) {
      throw new StorageFailureException(ERROR_READING_JSON, e);
    }
  }

  //===========================================================================
  // Testing
  //===========================================================================

  public void loadCustomJSON(String dataPath) {
    TasksManager retrievedData = getDataFromJson(dataPath);
    if (retrievedData != null) {
      tasksManager.sync(retrievedData);
      IDManager.getIdManager().updateIdPool(tasksManager.getPendingTasks());
      List<Task> result = TaskUtils.getTasksList(tasksManager.getPendingTasks());
      RaijinEventBus.getInstance().post(new SetCurrentDisplayEvent(result));
    }
  }

  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }

}
