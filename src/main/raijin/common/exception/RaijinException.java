package raijin.common.exception;

import org.slf4j.Logger;

import raijin.common.utils.RaijinLogger;

@SuppressWarnings("serial")
public class RaijinException extends Exception {
  
    /*Setup logger for error recording*/
    private Logger logger = RaijinLogger.getLogger();

    public RaijinException() {
        super();
    }

    public RaijinException(String message) {
        super(message);
        logger.error(message);
    }
}
