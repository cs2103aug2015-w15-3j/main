//@@author A0112213E

package raijin.common.eventbus.subscribers;

import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;

public abstract class MainSubscriber<E> {

  private List<E> events = new ArrayList<E>();

  public MainSubscriber(EventBus eventBus) {
    eventBus.register(this);
  }

  public abstract void handleEvent(E event);
  
  public List<E> getListenedEvents() {
    return events;
  }
    
}
