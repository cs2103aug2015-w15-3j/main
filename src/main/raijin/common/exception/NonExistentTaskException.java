package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class NonExistentTaskException extends RaijinException {

 public NonExistentTaskException(String message) {
    super(message, Constants.Error.NoSuchTask);
 }

}
