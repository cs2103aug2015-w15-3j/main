package raijin.common.exception;

import java.util.List;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class IllegalCommandArgumentException extends RaijinException {
  
  private Constants.CommandParam argument;

  public IllegalCommandArgumentException(String message, Constants.CommandParam arg) {
    super(message, Constants.Error.IllegalCommandArgument);
    this.argument = arg;
  }

  public Constants.CommandParam getArgument() {
    return argument;
  }
}
