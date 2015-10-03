package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class RaijinException extends Exception {
  
    /*Setup logger for error recording*/
    private Constants.Error errorCode;

    public RaijinException() {
        super();
    }

    public RaijinException(String message, Constants.Error errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public RaijinException(String message, Constants.Error errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public Constants.Error getErrorCode() {
      return errorCode;
    }
    
}
