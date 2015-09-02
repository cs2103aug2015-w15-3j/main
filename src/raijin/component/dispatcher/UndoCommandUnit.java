package raijin.component.dispatcher;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class UndoCommandUnit implements CommandUnitInterface {

  @Override
  public String executeCommand(UserInput input) {
    return Status.INFO_SUCCESS;
  }

}
