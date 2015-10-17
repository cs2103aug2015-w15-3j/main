package raijin.common.utils;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import raijin.common.datatypes.Task;

public class TaskPane extends StackPane {
	
	private String[] highPriorityColours = {};
	private String[] medPriorityColours = {};
	private String[] lowPriorityColours = {};
	
	private Label id;
	private Label taskName;
	private Label startDate, endDate;
	private Label startTime, endTime;
	private Label tags;
	private Label priority;
	
	public TaskPane (Task task, boolean isSubTask) {
		id = new Label(String.valueOf(task.getId()));
		taskName = new Label(task.getName());
	}
	
	public TaskPane (Task task, boolean isSubTask, String colourOfParent) {
		
	}
	
}
