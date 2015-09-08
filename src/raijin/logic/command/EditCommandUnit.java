package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.UserInput;
import raijin.logic.api.CommandUnitInterface;

public class EditCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return String.format(Status.INFO_EDITED_SUCCESS, input.getRawInput());
  }

}
