package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class ExitCommandRunner extends CommandRunner {

  public Status processCommand(ParsedInput cmd) {
    System.exit(0);
    return null;
  }

}
