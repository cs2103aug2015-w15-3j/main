package raijin.common.utils;

import java.util.List;

import org.loadui.testfx.FXScreenController;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

public class EventBus {

  private static EventBus eventBus = new EventBus();
  private StringProperty feedBack = new SimpleStringProperty(this, "feedback", "Write here");
  private StringProperty displayHeadMessage = new SimpleStringProperty(this, "displayHeadMessage", "");
  private ObservableList<String> currentTasks = FXCollections.observableArrayList();
  
  public String getFeedback() {
    return feedBack.get();
  }
    
  public void setFeedback(String input) {
    feedBack.set(input);
  }

  public void setHeadMessage(String input) {
    displayHeadMessage.set(input);
  }

  public void setCurrentTasks(List<String> tasks) {
    currentTasks.setAll(tasks);
  }

  public StringProperty feedBackProperty() {
    return feedBack;
  }

  public StringProperty displayHeadMessageProperty() {
    return displayHeadMessage;
  }

  public ObservableList<String> currentTasksProperty() {
    return currentTasks;
  }

  private EventBus() {}
  
  public static EventBus getEventBus() {
    return eventBus;
  }

}
