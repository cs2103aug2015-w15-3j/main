package raijin.component.dispatcher;

import raijin.util.Task.UserInput;


public interface CommandUnitInterface {
  public String executeCommand(UserInput input);
}
