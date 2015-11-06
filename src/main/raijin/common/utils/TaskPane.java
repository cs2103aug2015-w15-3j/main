//@@author A0130720Y

package raijin.common.utils;

import java.time.format.DateTimeFormatter;
import java.util.TreeSet;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.logic.command.DisplayCommandRunner;

public class TaskPane extends StackPane {
	private String highPriorityColour = "#FF9F94"; 		    // red
	private String midPriorityColour = "#AAE6FF"; 		    // blue
	private String lowPriorityColour = "#E6E6E6";		    // grey
	
	final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
	
	private Label id;
	private Label taskName;
	private Label start = new Label("Start: "), end = new Label("End: ");
	private Label startByOn = new Label("By/On: ");
	private Label startValue, endValue;
	private Label tagsValue;
	private Label isOverdue = new Label("Overdue!");
	
	private Constants.TYPE_TASK taskType;
	private DisplayCommandRunner displayInstance = new DisplayCommandRunner(); 
	
	public TaskPane() {
		
	}
	
	public TaskPane (int displayedNum, Task task) {
		id = new Label(Integer.toString(displayedNum));
		
		taskName = new Label((task.getName().length() > 69 
							  ? task.getName().substring(0,69) + "..."
							  : task.getName()));
		
		tagsValue = new Label(retrieveTags(task));
		
		InnerShadow innerShadow = new InnerShadow();
		innerShadow.setOffsetX(3);
		innerShadow.setOffsetY(0);
		innerShadow.setColor(Color.web("#FF8566"));
		isOverdue.setEffect(innerShadow);
		System.out.println(isOverdue.getStyle());
		
		HBox datesBox = new HBox();
		taskType = task.getType();
		
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
			endValue = new Label("");
			
			datesBox.getChildren().addAll(startByOn, startValue);
			
		} else if (taskType.equals(Constants.TYPE_TASK.FLOATING)) {
			startValue = new Label("");
			endValue = new Label("");
			datesBox.getChildren().addAll(startValue);
		}
		
		HBox idBox = new HBox();
		idBox.setPrefWidth(80);
		idBox.getChildren().addAll(id);
		
		HBox fillerBox = new HBox();
		fillerBox.setPrefWidth(10);
		
		HBox taskBox = new HBox();
		taskBox.setPadding(new Insets(3, 0, 3, 0));
		taskBox.getChildren().addAll(taskName);
		
		datesBox.setPrefHeight(10);
		
		HBox tagsBox = new HBox();
		tagsBox.setPadding(new Insets(3, 0, 5, 0));
		tagsBox.getChildren().addAll(tagsValue);
		
		VBox centre = new VBox();
		centre.setPrefWidth(550);
		centre.getChildren().addAll(taskBox, datesBox, tagsBox);
		
		HBox overdueBox = new HBox();
		overdueBox.setPrefWidth(60);
		overdueBox.getChildren().addAll(isOverdue);
		overdueBox.setPadding(new Insets(25, 0, 0, 0));

		HBox pane = new HBox();
		
		if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
			this.setStyle("-fx-background-color: " + highPriorityColour + ";");
			tagsValue.setTextFill(Color.rgb(178, 36, 0));
		} else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
			this.setStyle("-fx-background-color: " + midPriorityColour + ";");
			tagsValue.setTextFill(Color.rgb(32, 129, 160));
		} else if(task.getPriority().equals(Constants.PRIORITY_LOW)) {
			this.setStyle("-fx-background-color: " + lowPriorityColour + ";");
			tagsValue.setTextFill(Color.rgb(110, 110, 110));
		}
			
		pane.getChildren().addAll(idBox, centre);
		
		if (displayInstance.isOverdue(task)) {
			pane.getChildren().add(overdueBox);
		}
		
		this.getChildren().addAll(pane);
		this.setPrefHeight(69);
		this.setStyle(this.getStyle() + "-fx-background-radius: 20px;");
		
		// Assigning ID's to attributes for styling via css stylesheet
		id.setId("id");
		taskName.setId("taskName");
		start.setId("start");
		end.setId("end");
		startByOn.setId("startByOn");
		startValue.setId("startValue");
		endValue.setId("endValue");
		tagsValue.setId("tagsValue");
		isOverdue.setId("isOverdue");
		
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
