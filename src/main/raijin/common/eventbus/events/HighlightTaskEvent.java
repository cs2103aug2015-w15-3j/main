package raijin.common.eventbus.events;

public class HighlightTaskEvent {

  public int taskID;            //Index of task to be higlighted by display
  
  public HighlightTaskEvent(int taskID) {
    this.taskID = taskID;
  }
}
