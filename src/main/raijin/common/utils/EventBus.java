package raijin.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.loadui.testfx.FXScreenController;

import raijin.common.datatypes.Task;
import raijin.storage.api.TasksManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;

import static raijin.common.utils.DisplayUtils.*;

public class EventBus {

  private static EventBus eventBus = new EventBus();
  private StringProperty feedBack = new SimpleStringProperty(this, "feedback", "Write here");
  private StringProperty displayHeadMessage = new SimpleStringProperty(this, "displayHeadMessage", "");
  private ObservableList<String> currentTasks = FXCollections.observableArrayList();
  private ObservableList<String> completedTasks = FXCollections.observableArrayList();
  private List<Task> displayedTasks = new ArrayList<Task>();
  //
  private List<TaskPane> displayedTasksPane = new ArrayList<TaskPane>();
  private ObservableList<TaskPane> oDisplayedTasksPane;
  private ObservableList<TaskPane> oCompletedTasksPane;
  
  public String getFeedback() {
    return feedBack.get();
  }
    
  public List<Task> getDisplayedTasks() {
    return displayedTasks;
  }

  public void setFeedback(String input) {
    feedBack.set(input);
  }

  public void setHeadMessage(String input) {
    displayHeadMessage.set(input);
  }

  public void setCurrentTasks(List<Task> tasks) {
    displayedTasks = tasks;
    displayedTasksPane = convertToTaskPane(tasks);
    updateDisplay();
    currentTasks.setAll(filterName(tasks));
  }
 
  public void setCurrentTasks(String message) {
	displayedTasksPane = displayMessage(message);
	updateDisplay();
    currentTasks.setAll(message);
  }
  
  public void updateDisplay() {
	oDisplayedTasksPane = FXCollections.observableArrayList(displayedTasksPane);
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
  
  public ObservableList<TaskPane> currentTasksPropertyPane() {
	return oDisplayedTasksPane;
  }

  public ObservableList<String> completedTasksProperty() {
    return completedTasks;
  }
  
  //public ObservableList<String> completedTasksPropertyPane() {
	//return oCompletedTasksPane;
  //}

  public void initDisplayTasks(TasksManager tasksManager) {
    initCurrentTasks(tasksManager.getPendingTasks());
  }

  private void initCurrentTasks(HashMap<Integer, Task> pendingTasks) {
    displayedTasks = new ArrayList<Task>(pendingTasks.values());
    displayedTasksPane = initTasks(displayedTasks);
    updateDisplay();
    currentTasks.setAll(filterName(displayedTasks));
  }

  private EventBus() {}
  
  public static EventBus getEventBus() {
    return eventBus;
  }

}
