package raijin.logic.api;

import com.google.common.eventbus.EventBus;

import raijin.common.eventbus.RaijinEventBus;

public interface CommandShortcut {
  
  public void handleKeyEvent();

}
