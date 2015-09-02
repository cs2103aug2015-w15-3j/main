package raijin.component.dispatcher;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class DeleteCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return String.format(Status.INFO_DELETED_SUCCESS, input.getRawInput());
  }

}
