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

  private static Session session = new Session();
  private Logger logger;
  private TasksManager tasksManager;

  String userConfigPath;
  String dataPath;
  String tempPath;

  public String programDirectory;
  public String storageDirectory;
  public String baseConfigPath;
  public boolean isFirstTime; // Verify if user has used this application before

  private Session() {
    init();
  }

  public static Session getSession() {
    return session;
  }

  /* Needed to run regardless of user's choice of storage location */
  void init() {
    logger = RaijinLogger.getLogger();
    tasksManager = TasksManager.getManager();
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
    isFirstTime = StorageHandler.createDirectory(programDirectory); // Create working folder
    baseConfigPath = programDirectory + Constants.NAME_BASE_CONFIG;
    setupBaseConfig(baseConfigPath);
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

  /* Commit changes to tasks to a temp file */
  public void commit() {
    writeToFile(StorageHandler.convertToJson(TasksManager.getManager()), tempPath);
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

  public void setDataPath(String dataPath) {
    this.dataPath = dataPath;
  }

  public String getPathInfo() {
    return programDirectory + "\n" + storageDirectory + "\n" + baseConfigPath + "\n" + dataPath
        + "\n" + userConfigPath;
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

  // ===========================================================================
  // IOException Handling
  // ===========================================================================

  public void writeOnExit() {
    commit();
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
      throw new StorageFailureException(String.format("Failed to write \"%s\" to %s", output,
          filePath), e);
    }
  }

  /* Create new file with exception handling */
  boolean createNewFile(String filePath) {
    try {
      return StorageHandler.createFile(filePath);
    } catch (IOException e) {
      throw new StorageFailureException(String.format("Cannot create file %s", filePath), e);
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

  @SuppressWarnings("serial")
  TasksManager getDataFromJson(String dataPath) throws StorageFailureException {
    JsonReader reader = StorageHandler.getJsonReaderFromFile(dataPath);
    try {
      TasksManager retrievedData =
          StorageHandler.readFromJson(reader, new TypeToken<TasksManager>() {}.getType());
      return retrievedData;

    } catch (JsonParseException e) { // JSON file is corrupted
      generateNewFile(dataPath); // Recreate empty file
      throw new StorageFailureException("Corrupted JSON file.New JSON file created", e);

    } catch (NullPointerException e) { // JSON file is missing
      createNewFile(dataPath);
      throw new StorageFailureException("Missing JSON file.New JSON file created", e);

    } catch (IOException e) {
      throw new StorageFailureException("Failed to read from JSON file", e);
    }
  }

  void initTasksManager() {
    TasksManager retrievedData = getDataFromJson(dataPath);
    if (retrievedData != null) {
      tasksManager.sync(retrievedData);
      IDManager.getIdManager().updateIdPool(tasksManager.getPendingTasks());
    }
    /* @TODO replace with backup */
    generateNewFile(dataPath); // Recreate empty file
  }

  void generateNewFile(String filePath) {
    StorageHandler.deleteFile(filePath);
    createNewFile(filePath);
  }

  public void loadCustomJSON(String dataPath) {
    TasksManager retrievedData = getDataFromJson(dataPath);
    if (retrievedData != null) {
      tasksManager.sync(retrievedData);
      IDManager.getIdManager().updateIdPool(tasksManager.getPendingTasks());
      List<Task> result = TaskUtils.getTasksList(tasksManager.getPendingTasks());
      RaijinEventBus.getEventBus().post(new SetCurrentDisplayEvent(result));
    }
  }

}
