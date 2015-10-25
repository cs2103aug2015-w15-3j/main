package raijin.logic.api;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;

import javafx.application.Application;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.AutoComplete;
import raijin.common.utils.EventBus;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;
import raijin.common.utils.TaskUtils;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParserInterface;
import raijin.logic.parser.SimpleParser;
import raijin.storage.api.History;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;
import raijin.storage.handler.StorageHandler;


/**
 * 
 * @author papa
 * Basically a facade class that handles all components needs
 */
public class Logic {

  private ParserInterface parser;
  private Session session;
  private Logger logger;
  private AutoComplete autoComplete;
  private HashMap<Constants.Command, CommandRunner> commandRunners;
  private com.google.common.eventbus.EventBus eventbus;
  
  public Logic() throws FileNotFoundException {
    initAssets();                               //Initialize required components
  }
  
  private void initAssets() {
    parser = new SimpleParser();
    session = Session.getSession();
    logger = RaijinLogger.getLogger();
    autoComplete = new AutoComplete(TasksManager.getManager());
    commandRunners = new HashMap<Constants.Command, CommandRunner>();
    setupCommandRunners();

  }

  /*Initialize list of tasks*/
  public void initializeData(TasksManager tasksManager) {
    HashMap<Integer, Task> pendingTasks = tasksManager.getPendingTasks();
    tasksManager.setPendingTasks(pendingTasks);
    IDManager.getIdManager().updateIdPool(pendingTasks);
  }
  

  /*Used by UI controller to execute command and get a Status message*/
  public Status executeCommand(String userInput) {
    try {
      ParsedInput parsed = parser.parse(userInput);
      CommandRunner cmdRunner = CommandRunnerFactory.getCommandRunner(parsed.getCommand());
      return cmdRunner.execute(parsed);
    } catch (UnableToExecuteCommandException | FailedToParseException e) {
      logger.error(e.getMessage(), e);
      return new Status(e.getMessage());
    } 
  }
  

  //===========================================================================
  // Session methods
  //===========================================================================

  public boolean isFirstTime() {
    return session.isFirstTime;
  }

  public void setChosenUserStorage(String userPath) {
    /*Append data folder to the path*/
    String storagePath = userPath + Constants.NAME_USER_FOLDER;
    session.setStorageDirectory(storagePath, session.baseConfigPath);
  }

  //for UI to save on closing when exiting.
  public Session getSession() {
	  return this.session;
  }
  
  //===========================================================================
  // Auxilliary commands
  //===========================================================================
  
  public void exit() {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.EXIT)
      .createParsedInput();
    try {
      CommandRunner cmdRunner = CommandRunnerFactory.getCommandRunner(Constants.Command.EXIT);
      cmdRunner.execute(input);
    } catch (UnableToExecuteCommandException e) {
      logger.error(e.getMessage());
    }
  }

  void setupCommandRunners() {
    for (Constants.Command cmd : Constants.Command.values()) {
      commandRunners.put(cmd, CommandRunnerFactory.getCommandRunner(cmd));
    }
  }
  
  public List<Task> getPendingTasks() {
    return TaskUtils.getTasksList(TasksManager.getManager().getPendingTasks());
  }

  public List<Task> getCompletedTasks() {
    return TaskUtils.getTasksList(TasksManager.getManager().getCompletedTasks());
  }

  public void loadCustomData(String fileName) {
    String dataPath = session.programDirectory + "/" + fileName + ".json";
    session.loadCustomJSON(dataPath);
  }
  
}
