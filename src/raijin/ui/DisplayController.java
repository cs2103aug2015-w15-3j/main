package raijin.ui;

//import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.storage.api.Memory;
import raijin.storage.api.TasksMap;

import java.util.Date;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayController extends BorderPane {
	 
	 final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM ''yy");
	 Date date;
	 
	 Label headMessage;
	 
	 // Main display for tasks
	 ListView<String> listView = new ListView<String>();
	 
	 // Retrieve memory
	 Memory memory = Memory.getMemory();
	 TasksMap tasksMap = memory.getTasksMap();
	 
	 // Retrieve list of pending & completed tasks from memory
	 ArrayList<Task> pending = tasksMap.getPendingList();
	 ArrayList<Task> completed = tasksMap.getCompletedList();
	 
	 public DisplayController() {
		 date = new Date();
		 headMessage = new Label("Tasks pending for " + dateFormat.format(date));
		 headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
		 this.setTop(headMessage);
		 
		 this.setCenter(listView);
		 
		 displayTodaysTasks(pending, listView);
	 }
	 
	 static void displayTodaysTasks(ArrayList<Task> pending, ListView<String> listView) {
		
		 if (pending.isEmpty()) {
			 listView.getItems().add("You have no pending tasks for today!");
		 } else {
			 for (int i=0; i<pending.size(); i++) {
				 //TODO run a method that checks if
				 //task startDate <= current dateTime <= task endDate
				 listView.getItems().add(pending.get(i).getName());
			 }
		 }
		 
	 }
	 
	 static void displayTasks(String specifiedDate) {
		 // TODO
	 }
}
