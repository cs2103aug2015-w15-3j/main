package raijin.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import raijin.common.datatypes.DateTime;

import java.util.Date;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class DisplayController extends BorderPane {
	
	 private static DisplayController dc = new DisplayController();
	 
	 private static final String DISPLAY_CONTROLLER_FXML = "resource/layout/DisplayController.fxml";
	 final DateFormat dateFormatSplash = new SimpleDateFormat("EEE, d MMM ''yy");
	 
	 @FXML
	 Date date;
	 
	 @FXML
	 private Label headMessage;
	 
	 @FXML
	 ListView<String> listView;
	 
	 public DisplayController () {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(DISPLAY_CONTROLLER_FXML));
			loader.setController(this);
			loader.setRoot(this);
			
			try {
				loader.load();
		    } catch(IOException e) {
				e.printStackTrace();
		    }
			
		 date = new Date();
		 headMessage = new Label("Tasks pending for " + dateFormatSplash.format(date));
		 headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
		 this.setTop(headMessage);
		 
		 listView = new ListView<String>();
		 //listView.getItems().add("test");
		 this.setCenter(listView);
		 
	 }
	 
	 public void setHeadMessage(DateTime dateTime) {
		 date = Date.from(dateTime.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		 headMessage.setText("Tasks pending for " + dateFormatSplash.format(date));
		 this.setTop(headMessage);
	 }
	 
	 public void setListView(ListView<String> lv) {
		 listView.getItems().clear();
		 listView.setItems(lv.getItems());
		 this.setCenter(listView);
	 }
	 
	 public static DisplayController getDisplayController() {
		 return dc;
	 }
	 
}
