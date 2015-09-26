package raijin.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raijin.ui.Raijin;

public class RaijinLogger {

  private static final Logger logger = LoggerFactory.getLogger(Raijin.class);
  private RaijinLogger() {}
  
  public static Logger getLogger() {
    return logger;
  }
}
