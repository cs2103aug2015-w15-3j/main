package raijin.common.utils;

import java.util.TreeSet;

import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;

public class TaskPane extends StackPane {
	
	private String[] highPriorityColours = {"#FF9494", "#FFA366"}; 		// red, orange
	private String[] midPriorityColours = {"#B2FFB2", "#99E6FF"}; 		// green, blue
	private String[] lowPriorityColours = {"#E6E6E6"};					// grey
	
	private String[] subTaskColours = {"#FFC2C2", "#FFC299", "#D1FFD1", "#C2F0FF", "#F0F0F0"};
	
	private Label id;
	private Label taskName;
	private Label start, end;
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
	
	
	public TaskPane (Task task, String colourOfParent) {
		id = new Label(String.valueOf(task.getId()));
		id.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;");
		id.setPadding(new Insets(20));
		id.setTextAlignment(TextAlignment.LEFT); // this seems useless
		
		taskName = new Label(task.getName());
		taskName.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
		tags.setStyle("-fx-font-weight:bold;");
		tagsValue = new Label(retrieveTags(task));
		tagsValue.setStyle("-fx-font-style: italic;");
		priority.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
		
		try {
		if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
			priorityValue = new Label("High");
		} else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
			priorityValue = new Label("Mid");
		} else {
			priorityValue = new Label("Low");
		}
		} catch (NullPointerException e) {
			priorityValue = new Label("placeholder");
		}
		taskType = task.getType();
		
		if (taskType.equals(Constants.TYPE_TASK.EVENT)) {
			String startDate = task.getDateTime().getStartDate().toString();
			String startTime = task.getDateTime().getStartTime() == null ? "" 
								: " @ " + task.getDateTime().getStartTime().toString();
			
			String endDate = task.getDateTime().getEndDate().toString();
			String endTime = task.getDateTime().getEndTime() == null ? "" 
								: " @ " + task.getDateTime().getEndTime().toString();
			
			start = new Label("Start: " + startDate + startTime);
			end = new Label("End: " + endDate + endTime);
			
		} else if (taskType.equals(Constants.TYPE_TASK.SPECIFIC)) {
			// TODO may need better formatting.
			String endDate = task.getDateTime().getEndDate().toString();
			String startTime = task.getDateTime().getStartTime() == null ? "" 
					: " @ " + task.getDateTime().getStartTime().toString();
			String endTime = task.getDateTime().getEndTime() == null ? "" 
					: " to " + task.getDateTime().getEndTime().toString();
			
			start = new Label("By/On: " + endDate + startTime + endTime);
			end = new Label();
			
		} else if (taskType.equals(Constants.TYPE_TASK.FLOATING)) {
			start = new Label("No deadlines for this! :)");
			end = new Label();
		}
		
		HBox indentBox = new HBox();
		indentBox.setPrefWidth(50);
		
		HBox idBox = new HBox();
		idBox.setMaxWidth(30);
		idBox.getChildren().addAll(id);
		
		HBox fillerBox = new HBox();
		fillerBox.setPrefWidth(30);
		
		HBox taskBox = new HBox();
		taskBox.setPrefHeight(30);
		taskBox.getChildren().addAll(taskName);
		
		HBox datesBox = new HBox();
		datesBox.getChildren().addAll(start, end);
		datesBox.setPrefHeight(20);
		
		HBox tagsBox = new HBox();
		tagsBox.getChildren().addAll(tags, tagsValue);
		
		VBox centre = new VBox();
		centre.setPrefWidth(500);
		centre.getChildren().addAll(taskBox, datesBox, tagsBox);
		
		VBox right = new VBox();
		right.getChildren().addAll(priority, priorityValue);

		HBox pane = new HBox();
		
		// TODO remove the ! after parser supports priority
		if (!colourOfParent.equals("none")) {
			if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
				this.setStyle("-fx-background-color: " + highPriorityColours[task.getId()%2]);
				this.colour = highPriorityColours[task.getId()%2];
			} else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
				this.setStyle("-fx-background-color: " + midPriorityColours[task.getId()%2]);
				this.colour = midPriorityColours[task.getId()%2];
			} else if(task.getPriority().equals(Constants.PRIORITY_LOW)) {
				this.setStyle("-fx-background-color: " + lowPriorityColours[task.getId()%1]);
				this.colour = lowPriorityColours[task.getId()%1];
			}
			
			pane.getChildren().addAll(idBox, fillerBox, centre, right);
			
		} else {
			String colourForThis = "";
			
			for (int i=0; i<highPriorityColours.length; i++) {
				if (colourOfParent.equals(highPriorityColours[i])) {
					colourForThis = subTaskColours[i];
				}
			}
			
			for (int i=0; i<midPriorityColours.length; i++) {
				if (colourOfParent.equals(midPriorityColours[i])) {
					colourForThis = subTaskColours[i+2];
				}
			}
			
			for (int i=0; i<lowPriorityColours.length; i++) {
				if (colourOfParent.equals(lowPriorityColours[i])) {
					colourForThis = subTaskColours[i+3];
				}
			}
			
			this.colour = colourForThis;
			this.setStyle("-fx-background-color: " + colourForThis);
			pane.getChildren().addAll(idBox, centre, right); // TODO add back the indentbox
		}
		
		this.getChildren().addAll(pane);
		this.setMaxHeight(20);
		this.setStyle("-fx-background-radius: 5px");
		this.setPadding(new Insets(2));
		
	}
	
	public TaskPane (String message) {
		Label msg = new Label(message);
		msg.setStyle("-fx-font-size: 25px");
		
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
		} else {
			tagString = "none";
		}
		
		return tagString;
	}
	
}
