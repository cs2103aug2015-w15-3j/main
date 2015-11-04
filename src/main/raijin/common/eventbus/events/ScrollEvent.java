//@@author A0112213E

package raijin.common.eventbus.events;

public class ScrollEvent {
  
  public int scrollDelta = 10;        //Scroll through 10 items at a time
  
  public ScrollEvent(int direction) {
    scrollDelta*=direction;
  }

}
