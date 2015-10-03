package raijin.ui;

//import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import raijin.common.datatypes.DateTime;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;

public class DisplayController extends BorderPane {
	
	 private static DisplayController dc = new DisplayController();
	 
	 final DateFormat dateFormatSplash = new SimpleDateFormat("EEE, d MMM ''yy");
	 Date date;
	 
	 private Label headMessage;
	 
	 // Main display for tasks
	 ListView<String> listView;
	 
	 public DisplayController() {
		 date = new Date();
		 headMessage = new Label("Tasks pending for " + dateFormatSplash.format(date));
		 headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
		 this.setTop(headMessage);
		 
		 listView = new ListView<String>();
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
