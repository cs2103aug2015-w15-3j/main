package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.handler.StorageHandler;

public class SetCommandRunner extends CommandRunner {

  final String SUCCESS_MESSAGE = "Your data is now safe at %s";
  final String FAIL_MESSAGE = "You have provided an invalid path. Please"
      + " make sure that the folder exists";

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    String storagePath = input.getHelperOption();
    if (StorageHandler.isDirectory(storagePath)) {          // Check whether directory exists
      storagePath = input.getHelperOption() + Constants.NAME_USER_FOLDER;
      session.setStorageDirectory(storagePath, session.baseConfigPath);
      return new Status(String.format(SUCCESS_MESSAGE, storagePath));
    }
    return new Status(FAIL_MESSAGE, false);            
  }

}
