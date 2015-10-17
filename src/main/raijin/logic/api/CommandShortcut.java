package raijin.logic.api;

import com.google.common.eventbus.EventBus;

import raijin.common.eventbus.RaijinEventBus;

public interface CommandShortcut {
  
  public EventBus eventbus = RaijinEventBus.getEventBus();
  public void handleKeyEvent();

}
