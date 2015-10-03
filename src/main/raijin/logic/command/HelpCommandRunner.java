package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Constants;

public class HelpCommandRunner extends CommandRunner {

  public Status processCommand(ParsedInput cmd) {
    return new Status(Constants.FEEDBACK_INFO_SUCCESS, Constants.HELP_MESSAGE);
  }

}