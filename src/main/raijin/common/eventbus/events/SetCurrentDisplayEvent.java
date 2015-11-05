//@@author A0112213E

package raijin.common.eventbus.events;

import java.util.List;

import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;

/**
 * Populate current view with given list of tasks
 * @author papa
 *
 */
public class SetCurrentDisplayEvent {

  private RaijinEventBus eventbus = RaijinEventBus.getInstance();
  public List<Task> tasks;
  public String headMessage;                        //title of view
  public String bodyMessage;                        //content of view will be replaced with this message
  
  public SetCurrentDisplayEvent(List<Task> tasks) {
    this.tasks = tasks;
  }

  public SetCurrentDisplayEvent(String bodyMessage, String headMessage) {
    this.bodyMessage = bodyMessage;
    this.headMessage = headMessage;
  }

  public SetCurrentDisplayEvent(List<Task> tasks, String headMessage) {
    this.tasks = tasks;
    this.headMessage = headMessage;
    eventbus.setDisplayedTasks(tasks);              //Update dispayed tasks
  }
}
