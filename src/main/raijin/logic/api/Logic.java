//@@author A0112213E

package raijin.logic.api;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.RaijinLogger;
import raijin.common.utils.TaskUtils;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.ParserInterface;
import raijin.logic.parser.SimpleParser;
import raijin.logic.realtime.AutoComplete;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;


/**
 * Main class that handles communication with other components
 * @author papa
 */
public class Logic {

  private ParserInterface parser;
  private Session session;
  private Logger logger;
  private AutoComplete autoComplete;
  private HashMap<Constants.Command, CommandRunner> commandRunners;
  
  public Logic() throws FileNotFoundException {
    initAssets();                              
  }
  
  /*Initialiaze required assets*/
  private void initAssets() {
    parser = new SimpleParser();
    session = Session.getSession();
    logger = RaijinLogger.getLogger();
    autoComplete = new AutoComplete(TasksManager.getManager());
    commandRunners = new HashMap<Constants.Command, CommandRunner>();
    setupCommandRunners();

  }

  /**
   * Invokes command runner based on user input
   * @param userInput       
   * @return Status         status of executing the command
   */
  public Status executeCommand(String userInput) {
    try {
      ParsedInput parsed = parser.parse(userInput);
      CommandRunner cmdRunner = CommandRunnerFactory.getCommandRunner(parsed.getCommand());
      return cmdRunner.execute(parsed);
    } catch (UnableToExecuteCommandException | FailedToParseException e) {
      logger.error(e.getMessage(), e);
      return new Status(e.getMessage(), false);
    } 
  }
  

  //===========================================================================
  // Session methods
  //===========================================================================

  public void setChosenUserStorage(String userPath) {
    /*Append data folder to the path*/
    String storagePath = userPath + Constants.NAME_USER_FOLDER;
    session.setStorageDirectory(storagePath, session.baseConfigPath);
  }

  /*for UI to save on closing when exiting*/
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
      CommandRunner cmdRunner = CommandRunnerFactory.getCommandRunner(
          Constants.Command.EXIT);
      cmdRunner.execute(input);
    } catch (UnableToExecuteCommandException e) {
      logger.error(e.getMessage());
    }
  }

  /*Initialise all command runners*/
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

  /**
   * Used by developers to load custom JSON file 
   * @param fileName
   */
  public void loadCustomData(String fileName) {
    String dataPath = session.programDirectory + "/" + fileName + ".json";
    session.loadCustomJSON(dataPath);
  }
  
}
