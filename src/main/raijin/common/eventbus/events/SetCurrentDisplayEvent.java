package raijin.common.eventbus.events;

import java.util.List;

import raijin.common.datatypes.Task;

public class SetCurrentTasksEvent {

  public List<Task> tasks;
  
  public SetCurrentTasksEvent(List<Task> tasks) {
    this.tasks = tasks;
  }
}
