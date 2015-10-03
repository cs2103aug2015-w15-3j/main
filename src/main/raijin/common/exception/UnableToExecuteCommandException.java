package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class UnableToExecuteCommandException extends RaijinException {
  
  private Constants.Command command;
  
  public UnableToExecuteCommandException(String message, Constants.Command command, Throwable cause) {
    super(message, Constants.Error.UnableToExecuteCommand, cause);
    this.command = command;
  }
  
  public Constants.Command getCommand() {
    return command;
  }

}
