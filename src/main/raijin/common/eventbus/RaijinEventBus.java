//@@author A0112213E

package raijin.common.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * Wrapper for Google event bus
 * @author papa
 *
 */
public class RaijinEventBus {
  
  public static EventBus eventBus = null;
  
  private RaijinEventBus() {}
  
  public static EventBus getEventBus() {
    if (eventBus == null) {
      eventBus = new EventBus();
    }
    return eventBus;
  }
}
