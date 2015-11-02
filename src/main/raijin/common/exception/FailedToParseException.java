//@@author A0112213E

package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class FailedToParseException extends RaijinException {

  private String userInput;

  public FailedToParseException(String message, String userInput, Throwable cause) {
    super(message, Constants.Error.FailedToParse, cause);
    this.userInput = userInput;
  }
  
  public String getUserInput() {
    return userInput;
  }
}
