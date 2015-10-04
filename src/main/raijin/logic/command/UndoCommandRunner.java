package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class UndoCommandRunner extends CommandRunner {

  public Status processCommand(ParsedInput cmd) throws UnableToExecuteCommandException {
    history.undo();
    return new Status(Constants.FEEDBACK_UNDO_SUCCESS);
  }

}
