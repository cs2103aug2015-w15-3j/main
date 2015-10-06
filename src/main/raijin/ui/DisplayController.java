package raijin.ui;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import raijin.common.datatypes.DateTime;
import raijin.common.utils.EventBus;
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
  ListView<String> listView;

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
    //headMessage = new Label("Tasks pending for " + dateFormatSplash.format(date));
    headMessage = new Label("All pending tasks");
    headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
    this.setTop(headMessage);

    listView = new ListView<String>();
    
    this.setCenter(listView);

    eventBus.displayHeadMessageProperty().addListener((v, oldVal, newVal) -> {
      setHeadMessage(newVal);
    });
    
    eventBus.currentTasksProperty().addListener(new ListChangeListener<String>() {

      @Override
      public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
        listView.setItems(eventBus.currentTasksProperty());
      }
      
    });
    
    eventBus.initDisplayTasks(TasksManager.getManager());

  }
  
  private void setHeadMessage(String newVal) {
    headMessage.setText(newVal);
  }

  public void setHeadMessage(DateTime dateTime) {
    date =
        Date.from(dateTime.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    headMessage.setText("Tasks pending for " + dateFormatSplash.format(date));
    this.setTop(headMessage);
  }
  
  /* TODO remove if confirm not needed.
  public void setListView(ListView<String> lv) {
    listView.getItems().clear();
    listView.setItems(lv.getItems());
    this.setCenter(listView);
  }*/

}
