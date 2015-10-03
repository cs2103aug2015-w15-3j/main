package raijin.common.exception;

import java.util.List;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class IllegalCommandArgumentException extends RaijinException {
  
  private List<Constants.CommandParam> arguments;

  public IllegalCommandArgumentException(String message, List<Constants.CommandParam> args) {
    super(message, Constants.Error.IllegalCommandArgument);
    this.arguments = args;
  }

  public List<Constants.CommandParam> getArgumentsList() {
    return arguments;
  }
}
