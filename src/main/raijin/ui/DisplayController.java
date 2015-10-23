package raijin.ui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.utils.EventBus;
import raijin.common.utils.TaskPane;
import raijin.common.utils.TaskUtils;
import raijin.storage.api.TasksManager;

import java.util.List;
import java.io.IOException;

import com.google.common.eventbus.Subscribe;

public class DisplayController extends BorderPane {

  private EventBus eventBus = EventBus.getEventBus();
  private com.google.common.eventbus.EventBus eventbus = RaijinEventBus.getEventBus();     //Google event bus

  private static final String DISPLAY_CONTROLLER_FXML = "resource/layout/DisplayController.fxml";

  @FXML
  private Label headMessage;

  @FXML
  ListView<TaskPane> tasksPane;

  public DisplayController() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(DISPLAY_CONTROLLER_FXML));
    loader.setController(this);
    loader.setRoot(this);

    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    headMessage = new Label("All pending tasks");
    headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
    this.setTop(headMessage);

    tasksPane = new ListView<TaskPane>();
    tasksPane.setStyle("-fx-background-insets: 0; -fx-background-color: #fff, #fff;");
    tasksPane.setPadding(new Insets(0));
    
    this.setStyle("-fx-background-color: white;");
    this.setCenter(tasksPane);

    eventBus.displayHeadMessageProperty().addListener((v, oldVal, newVal) -> {
      setHeadMessage(newVal);
    });
    
    eventBus.currentTasksProperty().addListener(new ListChangeListener<String>() {

      @Override
      public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
        tasksPane.setItems(eventBus.currentTasksPropertyPane());
      }
      
    });
    
    eventBus.initDisplayTasks(TasksManager.getManager());
    tasksPane.setItems(eventBus.currentTasksPropertyPane());

    handleSetCurrentDisplayEvent();
    handleChangeViewEvent();
  }
  
  private void setHeadMessage(String newVal) {
    if (newVal != null) {
      headMessage.setText(newVal);
    }
  }
  
  void handleSetCurrentDisplayEvent() {
    MainSubscriber<SetCurrentDisplayEvent> setCurrentHandler = new MainSubscriber<
        SetCurrentDisplayEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(SetCurrentDisplayEvent event) {
            List<TaskPane> currentTask = TaskUtils.convertToTaskPane(event.tasks);
            tasksPane.setItems(FXCollections.observableArrayList(currentTask));
            setHeadMessage(event.headMessage);
          }};
  }
  
  void handleChangeViewEvent() {
    MainSubscriber<ChangeViewEvent> changeViewHandler = new MainSubscriber<
        ChangeViewEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(ChangeViewEvent event) {
            List<TaskPane> currentTask = TaskUtils.convertToTaskPane(event.focusView);
            tasksPane.setItems(FXCollections.observableArrayList(currentTask));
            setHeadMessage(event.typeOfView);
          }};
  }
}
