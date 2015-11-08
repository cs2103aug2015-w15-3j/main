//@@author A0130720Y

package raijin.ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import raijin.common.datatypes.Constants;
import raijin.common.eventbus.MainSubscriber;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.ScrollEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.utils.TaskUtils;
import raijin.storage.api.TasksManager;

import java.util.List;
import java.io.IOException;

import com.google.common.eventbus.Subscribe;

public class DisplayController extends BorderPane {

  private RaijinEventBus eventbus = RaijinEventBus.getInstance();

  private static final String DISPLAY_CONTROLLER_FXML = "resource/layout/DisplayController.fxml";
  static int scrollIndex = 0; // Determines position of scrollbar

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

    // Setting up headMessage
    headMessage = new Label(Constants.DISPLAY_ALL);
    headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
    this.setTop(headMessage);

    // Setting up tasksPane
    tasksPane = new ListView<TaskPane>();
    tasksPane.setStyle("-fx-background-insets: 0; -fx-background-color: #fff, #fff;");
    tasksPane.setPadding(new Insets(0));
    this.setCenter(tasksPane);

    // Configuring the display colour
    this.setStyle("-fx-background-color: white;");
    
    // Subscribing and listening to event changes
    handleScrollEvent();
    handleSetCurrentDisplayEvent();
    handleChangeViewEvent();
    
    // Initialising display in tasksPane
    if (TasksManager.getManager().getPendingTasks().isEmpty()) {
    	eventbus.post(new SetCurrentDisplayEvent("You have no pending tasks!", Constants.DISPLAY_ALL));
    } else {
    	eventbus.post(new SetCurrentDisplayEvent(TaskUtils.initTasks(TasksManager.getManager().getPendingTasks())));
    }

  }

  private void setHeadMessage(String newVal) {
    if (newVal != null) {
      headMessage.setText(newVal);
    }
  }

  void handleSetCurrentDisplayEvent() {
    MainSubscriber<SetCurrentDisplayEvent> setCurrentHandler =
        new MainSubscriber<SetCurrentDisplayEvent>(eventbus.getEventBus()) {

          @Subscribe
          @Override
          public void handleEvent(SetCurrentDisplayEvent event) {
            List<TaskPane> currentTask;
            if (event.bodyMessage != null) {
              currentTask = TaskUtils.displayMessage(event.bodyMessage);
            } else {
              if (event.headMessage != null && event.headMessage.equals("Tasks pending for...")) {
                currentTask = TaskUtils.convertToTaskPaneDefaultView(event.tasks);
              } else {
                currentTask = TaskUtils.convertToTaskPane(event.tasks);
              }
            }
            tasksPane.setItems(FXCollections.observableArrayList(currentTask));
            setHeadMessage(event.headMessage);
          }
        };
  }

  void handleChangeViewEvent() {
    MainSubscriber<ChangeViewEvent> changeViewHandler =
        new MainSubscriber<ChangeViewEvent>(eventbus.getEventBus()) {

          @Subscribe
          @Override
          public void handleEvent(ChangeViewEvent event) {
            List<TaskPane> currentTask = TaskUtils.convertToTaskPane(event.focusView);
            tasksPane.setItems(FXCollections.observableArrayList(currentTask));
            setHeadMessage(event.viewMessage);
          }
        };
  }

  void handleScrollEvent() {
    MainSubscriber<ScrollEvent> scrollViewHandler = new MainSubscriber<
        ScrollEvent>(eventbus.getEventBus()) {

      @Subscribe
      @Override
      public void handleEvent(ScrollEvent event) {
        scrollIndex = scrollIndex + event.scrollDelta;
        if (scrollIndex >= tasksPane.getItems().size()) {
          scrollIndex = tasksPane.getItems().size() -1;
          System.out.println(scrollIndex);
        }
        scrollIndex = scrollIndex < 0 ? 0 : scrollIndex;
        tasksPane.scrollTo(scrollIndex);

      }
    };
  }
}
