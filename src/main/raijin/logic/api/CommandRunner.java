package raijin.logic.api;


import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.RaijinException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.RaijinLogger;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public abstract class CommandRunner {

  private Logger logger = RaijinLogger.getLogger();
  protected TasksManager tasksManager = TasksManager.getManager();
  protected History history = History.getHistory();
  protected Session session = Session.getSession();

  protected abstract Status processCommand(ParsedInput input) throws UnableToExecuteCommandException;

  public final Status execute(ParsedInput input) throws UnableToExecuteCommandException {
    return handleCommandException(input);
  }
  
  public Status handleCommandException(ParsedInput input) {
    try {
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
}
