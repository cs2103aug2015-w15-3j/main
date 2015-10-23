package raijin.common.eventbus.events;

import java.util.List;

import raijin.common.datatypes.Task;

public class SetCurrentDisplayEvent {

  public List<Task> tasks;
  public String headMessage;
  
  public SetCurrentDisplayEvent(List<Task> tasks) {
    this.tasks = tasks;
  }

  public SetCurrentDisplayEvent(List<Task> tasks, String headMessage) {
    this.tasks = tasks;
    this.headMessage = headMessage;
  }
}