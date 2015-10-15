package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class SetCommandRunner extends CommandRunner {

  private final String SUCCESS_MESSAGE = "Your data is now safe at %s";

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    String storagePath = input.getHelperOption() + Constants.NAME_USER_FOLDER;
    session.setStorageDirectory(storagePath, session.baseConfigPath);
    return new Status(String.format(SUCCESS_MESSAGE, storagePath));
  }

}
