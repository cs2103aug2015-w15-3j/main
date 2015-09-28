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
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DisplayController extends BorderPane {
	 
	 final DateFormat dateFormatSplash = new SimpleDateFormat("EEE, d MMM ''yy");
	 final DateFormat dateFormatCompare = new SimpleDateFormat("dd/MM/yyyy");
	 Date date;
	 Calendar cal = Calendar.getInstance();
	 
	 Label headMessage;
	 
	 // Main display for tasks
	 ListView<String> listView;
	 
	 // Retrieve memory
	 Memory memory = Memory.getMemory();
	 TasksMap tasksMap = memory.getTasksMap();
	 
	 // Temporary ArrayLists for storing information
	 ArrayList<Task> pending;
	 ArrayList<Task> completed;
	 
	 public DisplayController() {
		 date = new Date();
		 headMessage = new Label("Tasks pending for " + dateFormatSplash.format(date));
		 headMessage.setStyle("-fx-font-size: 20px; -fx-padding: 5px;");
		 this.setTop(headMessage);
		 
		 listView = new ListView<String>();
		 this.setCenter(listView);
		 
		 pending = new ArrayList<Task>(memory.getTasks().values());
		 completed = new ArrayList<Task>(memory.getCompletedTasks().values());
		 
		 displayTodaysTasks(pending, listView, cal);
	 }
	 
	 
	 static void displayTodaysTasks(ArrayList<Task> pending, ListView<String> listView, Calendar cal) {
		
		 if (pending.isEmpty()) {
			 listView.getItems().add("You have no pending tasks for today!");
		 } else {
			 for (int i=0; i<pending.size(); i++) {
				 //TODO run a method that checks if
				 //task startDate <= current dateTime <= task endDate
				 if (isRelevantDate(cal, pending.get(i).getDateTime())) {
					 listView.getItems().add(pending.get(i).getName());
				 }
			 }
		 }
		 
	 }
	 
	 static void displayTasks(String specifiedDate) {
		 // TODO
	 }
	 
	 static boolean isRelevantDate(Calendar cal, DateTime dateTime) {
		 // TODO
		 
		 return false;
	 }
}
