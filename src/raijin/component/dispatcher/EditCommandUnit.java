package raijin.component.dispatcher;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class EditCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return String.format(Status.INFO_EDITED_SUCCESS, input.getRawInput());
  }

}
