//@@author A0112213E

package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.handler.StorageHandler;

/**
 * Sets storage directory specified by user
 * @author papa
 *
 */
public class SetCommandRunner extends CommandRunner {

  static final String SUCCESS_MESSAGE = "Your data is now safe at %s";
  static final String FAIL_MESSAGE = "You have provided an invalid path. Please"
      + " make sure that the folder exists";

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    /*Obtain path of directory*/
    String storagePath = input.getHelperOption();

    if (StorageHandler.isDirectory(storagePath)) {          
      storagePath = input.getHelperOption() + Constants.NAME_USER_FOLDER;
      session.setStorageDirectory(storagePath, session.baseConfigPath);
      return new Status(String.format(SUCCESS_MESSAGE, storagePath));
    }
    return new Status(FAIL_MESSAGE, false);            
  }

}
