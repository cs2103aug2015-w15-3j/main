package raijin.common.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventBus {

  private static EventBus eventBus = new EventBus();
  private StringProperty feedBack = new SimpleStringProperty(this, "feedback", "Write here");
  
  public String getFeedback() {
    return feedBack.get();
  }
    
  public void setFeedback(String input) {
    feedBack.set(input);
  }

  public StringProperty feedBackProperty() {
    return feedBack;
  }

  private EventBus() {}
  
  public static EventBus getEventBus() {
    return eventBus;
  }

}
