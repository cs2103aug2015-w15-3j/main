package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.UserInput;
import raijin.logic.api.CommandUnitInterface;

public class AddCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return String.format(Status.INFO_ADDED_SUCCESS, input.getRawInput());
  }

}
