package raijin.common.eventbus;

import com.google.common.eventbus.EventBus;

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
