package raijin.ui;

//import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;

import raijin.common.datatypes.Task;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayController extends BorderPane {
	 
	 final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM ''yy");
	 Date date;
	 
	 @FXML
	 Label headMessage;
	 
	 public DisplayController() {
		 date = new Date();
		 headMessage = new Label("Tasks for " + dateFormat.format(date));
		 this.setTop(headMessage);
	 }
}
