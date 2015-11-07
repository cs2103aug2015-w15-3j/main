// @@author A0130720Y

package raijin.common.utils;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.TreeSet;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.logic.command.DisplayCommandRunner;
import raijin.storage.api.TasksManager;

public class TaskPane extends StackPane {
  private String highPriorityColour = "#FF9F94";    // red
  private String highPriorityTimeSlot = "#CC0000";  // dark red
  private String midPriorityColour = "#AAE6FF";     // blue
  private String midPriorityTimeSlot = "#0066CC";   // dark blue
  private String lowPriorityColour = "#E6E6E6";     // grey
  private String lowPriorityTimeSlot = "#202020";   // dark grey

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
  private final int TASK_DISPLAY_NAME_LIMIT = 69;
  private final float MAX_EVENT_DURATION = 1440; // Set 24 hour as the upper bound for event duration
  private final float MAX_TIMESLOT_WIDTH = 150; // Maximum width of a time slot label

  private Label id;
  private Label taskName;
  private Label start = new Label("Start: "), end = new Label("End: ");
  private Label startByOn = new Label("By/On: ");
  private Label startValue = new Label();
  private Label endValue = new Label();
  private Label tagsValue;
  private Label isOverdue = new Label("Overdue!");

  private Constants.TYPE_TASK taskType;

  private DisplayCommandRunner displayInstance = new DisplayCommandRunner();
  private ArrayList<Task> completedList = new ArrayList<Task>(TasksManager.getManager()
      .getCompletedTasks().values());

  /* Constructor for Unit Tests */
  public TaskPane() {}

  /* Constructor for Tasks */
  public TaskPane(int displayedNum, Task task) {
    id = new Label(Integer.toString(displayedNum));
    taskName =
        new Label((task.getName().length() > TASK_DISPLAY_NAME_LIMIT ? task.getName().substring(0,
            TASK_DISPLAY_NAME_LIMIT)
            + "..." : task.getName()));
    tagsValue = new Label(retrieveTags(task));

    InnerShadow innerShadow = new InnerShadow();
    innerShadow.setOffsetX(3);
    innerShadow.setOffsetY(0);
    innerShadow.setColor(Color.web("#FF8566"));
    isOverdue.setEffect(innerShadow);

    HBox datesBox = new HBox();
    taskType = task.getType();

    if (taskType.equals(Constants.TYPE_TASK.EVENT)) {

      if (task.getDateTime().getEndDate().equals(task.getDateTime().getStartDate())) {
        setSingleDayEvent(task, datesBox);
      } else {
        String startDate = task.getDateTime().getStartDate().format(dateFormat);
        String startTime =
            task.getDateTime().getStartTime() == null ? "" : " @ "
                + task.getDateTime().getStartTime().toString();

        String endDate = task.getDateTime().getEndDate().format(dateFormat);
        String endTime =
            task.getDateTime().getEndTime() == null ? "" : " @ "
                + task.getDateTime().getEndTime().toString();

        startValue = new Label(startDate + startTime);
        startValue.setPrefWidth(225);
        startValue.setPadding(new Insets(0, 50, 0, 0));
        endValue = new Label(endDate + endTime);

        datesBox.getChildren().addAll(start, startValue, end, endValue);
      }

    } else if (taskType.equals(Constants.TYPE_TASK.SPECIFIC)) {
      String endDate = task.getDateTime().getEndDate().format(dateFormat);

      startValue = new Label(endDate);
      startValue.setPrefWidth(220);
      startValue.setPadding(new Insets(0, 50, 0, 0));
      Label timeValue = new Label(task.getDateTime().getEndTime().toString());
      endValue = new Label("");

      datesBox.getChildren().addAll(startByOn, startValue, timeValue);

    } else if (taskType.equals(Constants.TYPE_TASK.FLOATING)) {
      startValue = new Label("");
      endValue = new Label("");
      datesBox.getChildren().addAll(startValue);
    }

    datesBox.setPrefHeight(10);

    HBox idBox = new HBox();
    idBox.setPrefWidth(80);
    idBox.getChildren().addAll(id);

    HBox taskNameBox = new HBox();
    taskNameBox.setPadding(new Insets(3, 0, 3, 0));
    taskNameBox.getChildren().addAll(taskName);

    HBox tagsBox = new HBox();
    tagsBox.setPadding(new Insets(3, 0, 5, 0));
    tagsBox.setPrefWidth(500);
    tagsBox.getChildren().addAll(tagsValue);

    VBox centre = new VBox();
    centre.setPrefWidth(550);
    centre.setPadding(new Insets(0, 10, 0, 0));
    centre.getChildren().addAll(taskNameBox, datesBox, tagsBox);

    HBox overdueBox = new HBox();
    overdueBox.setPrefWidth(60);
    overdueBox.getChildren().addAll(isOverdue);
    overdueBox.setPadding(new Insets(25, 0, 0, 0));

    Label overdueReminder = GlyphsDude.createIconLabel(FontAwesomeIcon.EXCLAMATION_CIRCLE, 
        "", "25px", "10px", ContentDisplay.RIGHT);
    overdueReminder.setPadding(new Insets(20, 0, 0, 0));

    HBox pane = new HBox();
    pane.getChildren().addAll(idBox, centre);

    // Styling the tags
    if (task.getPriority().equals(Constants.PRIORITY_HIGH)) {
      this.setStyle("-fx-background-color: " + highPriorityColour + ";");
      tagsValue.setTextFill(Color.rgb(178, 36, 0));
    } else if (task.getPriority().equals(Constants.PRIORITY_MID)) {
      this.setStyle("-fx-background-color: " + midPriorityColour + ";");
      tagsValue.setTextFill(Color.rgb(32, 129, 160));
    } else if (task.getPriority().equals(Constants.PRIORITY_LOW)) {
      this.setStyle("-fx-background-color: " + lowPriorityColour + ";");
      tagsValue.setTextFill(Color.rgb(110, 110, 110));
    }

    // Add an indicator for overdue if task is overdue
    if (displayInstance.isOverdue(task) && !completedList.contains(task)) {
      pane.getChildren().add(overdueReminder);
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

  /* Constructor for non-tasks */
  public TaskPane(String message) {
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

  float getWidthOfTimeSlot(DateTime dateTime) {
    long duration = dateTime.getStartTime().until(dateTime.getEndTime(), ChronoUnit.MINUTES);
    float width = duration / MAX_EVENT_DURATION * MAX_TIMESLOT_WIDTH;
    width = width > MAX_TIMESLOT_WIDTH ? MAX_TIMESLOT_WIDTH : width;
    return width;
  }

  String getTimeSlotColor(String priority) {
    if (priority.equals(Constants.PRIORITY_HIGH)) {
      return highPriorityTimeSlot;
    } else if (priority.equals(Constants.PRIORITY_LOW)) {
      return lowPriorityTimeSlot;
    } else {
      return midPriorityTimeSlot;
    }
  }

  void setSingleDayEvent(Task task, HBox datesBox) {
    DateTime dateTime = task.getDateTime();
    String endDate = dateTime.getEndDate().format(dateFormat).toString();
    String startTime = dateTime.getStartTime().toString();
    String endTime = dateTime.getEndTime().toString();

    Label eventDate = new Label(endDate);
    eventDate.setPrefWidth(220);
    eventDate.setPadding(new Insets(0, 50, 0, 0));
    Label startTimeLabel = new Label(startTime);
    startTimeLabel.setPadding(new Insets(0, 10, 0, 0));
    Label timeSlot = new Label();
    timeSlot.setPrefHeight(10);
    timeSlot.setPrefWidth(getWidthOfTimeSlot(dateTime));
    String color = getTimeSlotColor(task.getPriority());
    timeSlot.setStyle("-fx-border-radius: 5 5 5 5; "
                    + "-fx-background-radius: 5 5 5 5; " 
                    + "-fx-background-color: " + color);
    Label endTimeLabel = new Label(endTime);
    endTimeLabel.setPadding(new Insets(0, 0, 0, 10));
    datesBox.getChildren().addAll(startByOn, eventDate, startTimeLabel,
                                  timeSlot, endTimeLabel);
  }

  /**
   * This method is to get all the tags of the task into a single String object, separated by
   * commas.
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
      tagString = tagString.substring(0, tagString.length() - 2);
    }

    return tagString;
  }

}
