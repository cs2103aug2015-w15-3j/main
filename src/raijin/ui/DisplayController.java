package raijin.ui;

//import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import java.util.Date;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayController extends BorderPane {
	 
	 final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM ''yy");
	 Date date;
	 
	 Label headMessage;
	 ArrayList<Task> list;
	 ArrayList<Task> filterList;
	 ListView<String> listView;
	 
	 public DisplayController() {
		 date = new Date();
		 headMessage = new Label("Tasks pending for " + dateFormat.format(date));
		 headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
		 this.setTop(headMessage);
		 
		 listView = new ListView<String>();
		 this.setCenter(listView);
		 
		 list = new ArrayList<Task>();
		 list = retrieveTasks(list);
		 filterList = displayTodaysTasks(list, listView);
	 }
	 
	 static ArrayList<Task> retrieveTasks(ArrayList<Task> list) {
		//TODO, places list of tasks from memory into an arraylist
		 
		 //list.add(new Task("watch tv", new DateTime("26/09/2015")));
		 //list.add(new Task("play mahjong", new DateTime("26/09/2015")));
		 
		 return list;
	 }
	 
	 static ArrayList<Task> displayTodaysTasks(ArrayList<Task> list, ListView<String> listView) {
		 ArrayList<Task> list2 = new ArrayList();
		
		 if(list.isEmpty()) {
			 listView.getItems().add("You have no pending tasks for today!");
		 } else {
			 for (int i=0; i<list.size(); i++) {
				 //TODO run a method that checks if
				 //task startDate <= current dateTime <= task endDate
				 listView.getItems().add(list.get(i).getName());
				 list2.add(list.get(i));
			 }
		 }
		 
		 return list2;
	 }
	 
	 static void displayTasks(String specifiedDate) {
		 // TODO
	 }
}
