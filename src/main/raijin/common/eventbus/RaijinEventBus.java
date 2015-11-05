//@@author A0112213E

package raijin.common.eventbus;

import com.google.common.eventbus.EventBus;

/**
 * Wrapper for Google event bus
 * @author papa
 *
 */
public class RaijinEventBus {
  
  public static RaijinEventBus instance = null;
  private EventBus eventbus;
  
  private RaijinEventBus() {
    eventbus = new EventBus();
  }
  
  public static RaijinEventBus getInstance() {
    if (instance == null) {
      instance = new RaijinEventBus();
    }
    return instance;
  }
  
  public void post(Object event) {
    eventbus.post(event);
  }
  
  public EventBus getEventBus() {
    return eventbus;
  }

}
