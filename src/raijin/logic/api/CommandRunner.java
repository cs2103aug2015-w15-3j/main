package raijin.logic.api;

import raijin.common.datatypes.Status;
import raijin.logic.parser.Command;

public interface CommandRunner {

  public Status execute(Command cmd);
}
