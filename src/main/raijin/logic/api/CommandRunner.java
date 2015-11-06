//@@author A0112213E

package raijin.logic.api;


import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.exception.RaijinException;
import raijin.common.exception.UnableToExecuteCommandException;
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
  protected RaijinEventBus eventbus = RaijinEventBus.getInstance();
  //protected raijin.common.utils.EventBus eventBus = raijin.common.utils.EventBus.getEventBus();
  protected IDManager idManager = IDManager.getIdManager();

  protected abstract Status processCommand(ParsedInput input) throws UnableToExecuteCommandException;

  public final Status execute(ParsedInput input) throws UnableToExecuteCommandException {
    return handleCommandException(input);
  }
  
  /**
   * Template method to handle exception thrown by execution of a command
   * @param input       parsed input
   * @return
   */
  public Status handleCommandException(ParsedInput input) {
    try {
      translateIds(input);
      return processCommand(input);
    } catch (UnableToExecuteCommandException e) {
      logger.debug(e.getMessage(), e);
      return new Status(e.getMessage());
    }
  }

  int getRealId(int displayedId) throws UnableToExecuteCommandException {
    try {
      return eventbus.getDisplayedTasks().get(displayedId-1).getId();
    } catch (IndexOutOfBoundsException e) {
      throw new UnableToExecuteCommandException("Does not match displayed task", e);
    }
  }

  /**
   * Translate virtual id on view to real task id for multiple ids
   * @param input
   * @throws UnableToExecuteCommandException
   */
  public void translateIds(ParsedInput input) throws UnableToExecuteCommandException {
    TreeSet<Integer> translated = new TreeSet<Integer>();
    if (input.getId() != 0) {
      for (int id : input.getIds()) {
        translated.add(getRealId(id));
      }
      input.setId(translated);
    }
  }
  
  protected void wrapLowerLevelException(RaijinException e, 
    Constants.Command typeOfCommand) throws UnableToExecuteCommandException {
    throw new UnableToExecuteCommandException(e.getMessage(), typeOfCommand, e);
  }
  
  /*trim task name to fit max length*/
  protected String normalizeTaskName(String taskName) {
    return StringUtils.left(taskName, Constants.MAX_NAME_LENGTH) + "...";
  }

}
