//@@author A0112213E

package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class IllegalCommandArgumentException extends RaijinException {
  
  private Constants.CommandParam argument;

  public IllegalCommandArgumentException(String message, Constants.CommandParam arg) {
    super(message, Constants.Error.IllegalCommandArgument);
    this.argument = arg;                                                        //Type of argument that is invalid
  }

  public Constants.CommandParam getArgument() {
    return argument;
  }
}
