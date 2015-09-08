package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.UserInput;
import raijin.logic.api.CommandUnitInterface;

public class UndoCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return Status.INFO_SUCCESS;
  }

}
