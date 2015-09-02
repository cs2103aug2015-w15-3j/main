package raijin.component.dispatcher;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class AddCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return String.format(Status.INFO_ADDED_SUCCESS, input.getRawInput());
  }

}
