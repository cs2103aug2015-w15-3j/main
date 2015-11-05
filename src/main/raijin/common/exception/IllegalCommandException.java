//@@author A0112213E

package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class IllegalCommandException extends RaijinException {

  private String command;

  public IllegalCommandException(String message, String command) {
    super(message, Constants.Error.IllegalCommand);
    this.command = command;                                                     //Type of command that is invalid
  }
  
  public String getCommand() {
    return command;
  }
}
