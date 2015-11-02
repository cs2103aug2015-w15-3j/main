package raijin.common.eventbus.events;

public class ScrollEvent {
  
  public int scrollDelta = 10;        //Scroll through 10 items
  
  public ScrollEvent(int direction) {
    scrollDelta*=direction;
  }

}
