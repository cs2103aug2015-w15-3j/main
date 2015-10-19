package raijin.ui;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import raijin.common.datatypes.DateTime;
import raijin.common.utils.EventBus;
import raijin.common.utils.TaskPane;
import raijin.storage.api.TasksManager;

import java.util.Date;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class DisplayController extends BorderPane {

  private EventBus eventBus = EventBus.getEventBus();

  private static final String DISPLAY_CONTROLLER_FXML = "resource/layout/DisplayController.fxml";
  final DateFormat dateFormatSplash = new SimpleDateFormat("EEE, d MMM ''yy");

  @FXML
  Date date;

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

    date = new Date();
    
    headMessage = new Label("All pending tasks");
    headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 10px;");
    this.setTop(headMessage);

    tasksPane = new ListView<TaskPane>();
    tasksPane.setStyle("-fx-background-insets: 0; -fx-background-color: #fff, #fff;");
    tasksPane.setPadding(new Insets(0));
    
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
    
    //eventBus.initDisplayTasks(TasksManager.getManager());
    eventBus.initDisplayTasks(TasksManager.getManager());
    tasksPane.setItems(eventBus.currentTasksPropertyPane());

  }
  
  private void setHeadMessage(String newVal) {
    headMessage.setText(newVal);
  }
  
}
