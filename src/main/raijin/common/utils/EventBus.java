package raijin.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.loadui.testfx.FXScreenController;

import raijin.common.datatypes.DisplayContainer;
import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Task;
import raijin.storage.api.TasksManager;
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
  private DisplayContainer displayedTasks = new ListDisplayContainer();
  
  public String getFeedback() {
    return feedBack.get();
  }
    
  public DisplayContainer getDisplayedTasks() {
    return displayedTasks;
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

  public void initDisplayTasks(TasksManager tasksManager) {
    initCurrentTasks(tasksManager.getPendingTasks());
  }

  private void initCurrentTasks(HashMap<Integer, Task> pendingTasks) {
    List<Task> tempList = new ArrayList<Task>(pendingTasks.values());
    currentTasks.setAll(tempList.stream().map(Task::getName).collect(Collectors.toList()));
  }

  private EventBus() {}
  
  public static EventBus getEventBus() {
    return eventBus;
  }

}
