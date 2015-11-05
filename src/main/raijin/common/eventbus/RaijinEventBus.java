//@@author A0112213E

package raijin.common.eventbus;

import java.util.ArrayList;
import java.util.List;

import raijin.common.datatypes.Task;

import com.google.common.eventbus.EventBus;

/**
 * Wrapper for Google event bus
 * @author papa
 *
 */
public class RaijinEventBus {
  
  public static RaijinEventBus instance = null;
  private EventBus eventbus;
  private List<Task> displayedTasks = new ArrayList<Task>();
  
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

  public List<Task> getDisplayedTasks() {
    return displayedTasks;
  }
  
  public void setDisplayedTasks(List<Task> displayedTasks) {
    this.displayedTasks = displayedTasks;
  }

}
