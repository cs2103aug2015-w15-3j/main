package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class RedoCommandRunner extends CommandRunner {

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    history.redo();
    return new Status(Constants.FEEDBACK_REDO_SUCCESS);
  }

}
