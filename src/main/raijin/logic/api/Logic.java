package raijin.logic.api;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;

import javafx.application.Application;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.EventBus;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;
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

  private TasksManager tasksManager;
  private CommandDispatcher commandDispatcher;
  private ParserInterface parser;
  private Session session;
  private Logger logger;
  private EventBus eventBus;
  
  public Logic() throws FileNotFoundException {
    initAssets();                   //Initialize required components
  }
  
  private void initAssets() {
    tasksManager = TasksManager.getManager();
    commandDispatcher = CommandDispatcher.getDispatcher();
    parser = new SimpleParser();
    session = Session.getSession();
    logger = RaijinLogger.getLogger();
    eventBus = EventBus.getEventBus();
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
      return commandDispatcher.delegateCommand(parsed);
    } catch (FailedToParseException e) {
      logger.error(e.getMessage(), e);
      return new Status(String.format(Constants.FEEDBACK_ERROR_FAILEDPARSING,
          e.getUserInput()));
    } catch (UnableToExecuteCommandException e) {
      logger.error(e.getMessage(), e);
      return new Status(String.format(Constants.FEEDBACK_ERROR_FAILEDCOMMAND,
          e.getCommand()));
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

}
