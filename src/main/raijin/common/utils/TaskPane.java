package raijin.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;

public class TaskPane extends StackPane {
	
	//private String[] highPriorityColours = {"#FF9494", "#FFA366"}; 		// red, orange
	//private String[] midPriorityColours = {"#B2FFB2", "#99E6FF"}; 		// green, blue
	//private String[] lowPriorityColours = {"#E6E6E6"};					// grey
	
	private String[] highPriorityColours = {"#FF9494"}; 		// red, orange
	private String[] midPriorityColours = {"#99E6FF"}; 		// green, blue
	private String[] lowPriorityColours = {"#E6E6E6"};					// grey
	
	//private String[] subTaskColours = {"#FFC2C2", "#FFC299", "#D1FFD1", "#C2F0FF", "#F0F0F0"};
	private String[] subTaskColours = {"#FFC2C2", "#D1FFD1", "#F0F0F0"};
	
	final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
	
	private Label id;
	private Label taskName;
	private Label start = new Label("Start: "), end = new Label("End: ");
	private Label startByOn = new Label("By/On: ");
	private Label startValue, endValue;
	private Label tags = new Label("Tags: ");
	private Label tagsValue;
	private Label priority = new Label("Priority:");
	private Label priorityValue;
	
	private Constants.TYPE_TASK taskType;
	public String colour;
	
	// A Task pane
	// VBox					VBox												  VBox
	//====================================================================================//
	//|id       |__ taskName _________________________________________________| Priority: |
	//|_________|[start:date,time|end:date,time]/[endDate, time-time]/[none]__| high/med/ |
	//|Overdue? |	tags go here											  | low       |
	//====================================================================================//
	
	public TaskPane() {
		
	}
	
	public TaskPane (int displayedNum, Task task, String colourOfParent) {
		id = new Label(Integer.toString(displayedNum));
		id.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		id.setPadding(new Insets(15,20,0,25));
		//id.setTextAlignment(TextAlignment.CENTER); // this seems useless
		
		start.setStyle("-fx-font-weight:bold");
		end.setStyle("-fx-font-weight:bold");
		startByOn.setStyle("-fx-font-weight:bold");
		
		taskName = new Label((task.getName().length() > 59 ? task.getName().substring(0,59) + "..." : task.getName()));
		taskName.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
		
		tags.setStyle("-fx-font-weight:bold;");
		tagsValue = new Label(retrieveTags(task));
		tagsValue.setStyle("-fx-font-style: italic;");
		
		priority.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		
		
		if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
			priorityValue = new Label("High");
		} else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
			priorityValue = new Label("Mid");
		} else {
			priorityValue = new Label("Low");
		}
		
		taskType = task.getType();
		
		HBox datesBox = new HBox();
		
		if (taskType.equals(Constants.TYPE_TASK.EVENT)) {
			String startDate = task.getDateTime().getStartDate().format(dateFormat);
			String startTime = task.getDateTime().getStartTime() == null ? "" 
								: " @ " + task.getDateTime().getStartTime().toString();
			
			String endDate = task.getDateTime().getEndDate().format(dateFormat);
			String endTime = task.getDateTime().getEndTime() == null ? "" 
								: " @ " + task.getDateTime().getEndTime().toString();
			
			startValue = new Label(startDate + startTime);
			startValue.setPadding(new Insets(0, 50, 0, 0));
			endValue = new Label(endDate + endTime);
			
			datesBox.getChildren().addAll(start, startValue, end, endValue);
			
		} else if (taskType.equals(Constants.TYPE_TASK.SPECIFIC)) {
			String endDate = task.getDateTime().getEndDate().format(dateFormat);
			String startTime = task.getDateTime().getStartTime() == null ? "" 
					: " @ " + task.getDateTime().getStartTime().toString() + " to";
			String endTime = task.getDateTime().getEndTime() == null ? "" 
					:  ", " + task.getDateTime().getEndTime().toString();
			
			startValue = new Label(endDate + startTime + endTime);
			
			datesBox.getChildren().addAll(startByOn, startValue);
			
		} else if (taskType.equals(Constants.TYPE_TASK.FLOATING)) {
			startValue = new Label("");
			datesBox.getChildren().addAll(startValue);
		}
		
		HBox indentBox = new HBox();
		indentBox.setPrefWidth(50);
		
		HBox idBox = new HBox();
		idBox.setPrefWidth(80);
		idBox.getChildren().addAll(id);
		
		HBox fillerBox = new HBox();
		fillerBox.setPrefWidth(10);
		
		HBox taskBox = new HBox();
		//taskBox.setPrefHeight(30);
		taskBox.setPadding(new Insets(3, 0, 3, 0));
		taskBox.getChildren().addAll(taskName);
		
		datesBox.setPrefHeight(10);
		
		HBox tagsBox = new HBox();
		tagsBox.setPadding(new Insets(3, 0, 5, 0));
		tagsBox.getChildren().addAll(tagsValue);
		
		VBox centre = new VBox();
		centre.setPrefWidth(500);
		centre.getChildren().addAll(taskBox, datesBox, tagsBox);
		
		VBox right = new VBox();
		right.getChildren().addAll(priority, priorityValue);

		HBox pane = new HBox();
		//pane.setPrefHeight(0);
		
		if (colourOfParent.equals("none")) {
			if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
				this.setStyle("-fx-background-color: " + highPriorityColours[task.getId()%1] + ";");
				this.colour = highPriorityColours[task.getId()%1];
				tagsValue.setTextFill(Color.rgb(178, 36, 0));
			} else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
				this.setStyle("-fx-background-color: " + midPriorityColours[task.getId()%1] + ";");
				this.colour = midPriorityColours[task.getId()%1];
				tagsValue.setTextFill(Color.rgb(32, 129, 160));
			} else if(task.getPriority().equals(Constants.PRIORITY_LOW)) {
				this.setStyle("-fx-background-color: " + lowPriorityColours[task.getId()%1] + ";");
				this.colour = lowPriorityColours[task.getId()%1];
				tagsValue.setTextFill(Color.rgb(110, 110, 110));
			}
			
			//pane.getChildren().addAll(idBox, centre, right);
			pane.getChildren().addAll(idBox, centre); //excluding priority label
			
		} else {
			String colourForThis = "";
			
			for (int i=0; i<highPriorityColours.length; i++) {
				if (colourOfParent.equals(highPriorityColours[i])) {
					colourForThis = subTaskColours[i];
				}
			}
			
			for (int i=0; i<midPriorityColours.length; i++) {
				if (colourOfParent.equals(midPriorityColours[i])) {
					colourForThis = subTaskColours[i+1];
				}
			}
			
			for (int i=0; i<lowPriorityColours.length; i++) {
				if (colourOfParent.equals(lowPriorityColours[i])) {
					colourForThis = subTaskColours[i+2];
				}
			}
			
			this.colour = colourForThis;
			this.setStyle("-fx-background-color: " + colourForThis);
			pane.getChildren().addAll(indentBox, idBox, centre, right);
		}
		
		this.getChildren().addAll(pane);
		this.setPrefHeight(65);
		this.setStyle(this.getStyle() + "-fx-background-radius: 20px;");
		//this.setPadding(new Insets(10));
		
	}
	
	public TaskPane (String message) {
		Label msg = new Label(message);
		DropShadow dropShadow = new DropShadow();
		 dropShadow.setRadius(3.0);
		 dropShadow.setOffsetX(2.0);
		 dropShadow.setOffsetY(2.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5, 0.3));
		 
		if (!message.equals("No pending tasks!")) {
			msg.setStyle("-fx-font-size: 17px; -fx-padding: 7px;");
			msg.setEffect(dropShadow);
		} else {
			msg.setStyle("-fx-font-size: 15px; -fx-padding: 5px;");
		}
		
		this.getChildren().addAll(msg);
		this.setStyle("-fx-background-color: white;");
	}

	/**
	 * This method is to get all the tags of the task into a single String
	 * object, separated by commas.
	 * 
	 * @param task
	 * @return tagString
	 */
	public String retrieveTags(Task task) {
		TreeSet<String> tagsTree = new TreeSet<String>(task.getTags());
		String tagString = "";
		boolean hasTags = false;
		
		while (!tagsTree.isEmpty()) {
			hasTags = true;
			tagString += tagsTree.pollFirst();
			tagString += ", ";
		}
		
		if (hasTags) {
			tagString = tagString.substring(0, tagString.length() -  2);
		}
		
		return tagString;
	}
	
}
