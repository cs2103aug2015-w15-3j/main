//@@author A0112213E

package raijin.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import raijin.ui.Raijin;

/**
 * Wrapper for logback logger
 * @author papa
 *
 */
public class RaijinLogger {

  private static final Logger logger = LoggerFactory.getLogger(Raijin.class);
  private RaijinLogger() {}
  
  public static Logger getLogger() {
    return logger;
  }
}
