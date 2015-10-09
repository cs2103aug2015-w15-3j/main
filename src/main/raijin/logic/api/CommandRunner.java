package raijin.logic.api;


import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.RaijinException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.EventBus;
import raijin.common.utils.IDManager;
import raijin.common.utils.RaijinLogger;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public abstract class CommandRunner {

  protected Logger logger = RaijinLogger.getLogger();
  protected TasksManager tasksManager = TasksManager.getManager();
  protected History history = History.getHistory();
  protected Session session = Session.getSession();
  protected EventBus eventBus = EventBus.getEventBus();
  protected IDManager idManager = IDManager.getIdManager();

  protected abstract Status processCommand(ParsedInput input) throws UnableToExecuteCommandException;

  public final Status execute(ParsedInput input) throws UnableToExecuteCommandException {
    return handleCommandException(input);
  }
  
  public Status handleCommandException(ParsedInput input) {
    try {
      if (input.getId() != 0) {
        int taskId = getRealId(input.getId());
        input.setId(taskId);
      }
      return processCommand(input);
    } catch (UnableToExecuteCommandException e) {
      logger.debug(e.getMessage(), e);
      return new Status(e.getMessage());
    }
  }
  
  protected void wrapLowerLevelException(RaijinException e, 
    Constants.Command typeOfCommand) throws UnableToExecuteCommandException {
    throw new UnableToExecuteCommandException(e.getMessage(), typeOfCommand, e);
  }
  
  int getRealId(int displayedId) throws UnableToExecuteCommandException {
    try {
      return eventBus.getDisplayedTasks().get(displayedId-1).getId();
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new UnableToExecuteCommandException("Does not match displayed task", e);
    }
  }
}
