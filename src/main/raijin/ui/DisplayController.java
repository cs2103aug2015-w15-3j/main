package raijin.ui;

//import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayController extends BorderPane {
	
	private static DisplayController dc = new DisplayController();
	 
	 final DateFormat dateFormatSplash = new SimpleDateFormat("EEE, d MMM ''yy");
	 final DateFormat dateFormatCompare = new SimpleDateFormat("dd/MM/yyyy");
	 Date date;
	 Calendar cal = Calendar.getInstance();
	 
	 Label headMessage;
	 
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
	 
	 public void setListView(ListView<String> lv) {
		 this.listView.setItems(lv.getItems());
	 }
	 
	 public static DisplayController getDisplayController() {
		 return dc;
	 }
	 
}
