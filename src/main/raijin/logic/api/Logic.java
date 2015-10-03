package raijin.logic.api;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Application;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.exception.NonExistentTaskException;
import raijin.common.utils.IDManager;
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

  private Application raijin;
  private TasksManager tasksManager;
  private CommandDispatcher commandDispatcher;
  private ParserInterface parser;
  private Session session;
  
  public Logic(Application raijin) throws FileNotFoundException {
    initAssets(raijin);                   //Initialize required components
  }
  
  private void initAssets(Application raijin) {
    this.raijin = raijin;
    tasksManager = TasksManager.getManager();
    commandDispatcher = CommandDispatcher.getDispatcher();
    parser = new SimpleParser();
    session = Session.getSession();
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

    } catch (IllegalArgumentException e) {
      return new Status(Constants.FEEDBACK_ERROR_ILLEGALCOMMAND);
    } catch (NonExistentTaskException e) {
      return new Status(Constants.EXCEPTION_NONEXISTENTTASK);
    }
  }

  //===========================================================================
  // Storage methods
  //===========================================================================
  
  public ArrayList<Task> retrievePendingTasks() {
    return new ArrayList<Task>(tasksManager.getPendingTasks().values());
  }

  public ArrayList<Task> retrieveCompletedTasks() {
    return new ArrayList<Task>(tasksManager.getCompletedTasks().values());
  }

}
