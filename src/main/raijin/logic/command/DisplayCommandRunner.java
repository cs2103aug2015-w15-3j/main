package raijin.logic.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import raijin.common.datatypes.DateTime;
//import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.utils.EventBus;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;
//import raijin.ui.DisplayController;

public class DisplayCommandRunner extends CommandRunner {
	
  private static final String PENDING = "p";
  private static final String COMPLETED = "c";
  
  private DateTime cmdDateTime;
  private DateTime taskDateTime;
  private LocalDate now;

  private ArrayList<Task> pending;
  private ArrayList<Task> completed;
  private ArrayList<Task> relevant;
  
  private EventBus eventBus = EventBus.getEventBus();
  
  final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM ''yy");

  public Status processCommand(ParsedInput cmd) {
	  // Getting the current DateTime
	  now = LocalDate.now();
	  
	  pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
	  completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
	  relevant = new ArrayList<Task>();
	  
	  boolean isEmpty = true;
	  
	  if (cmd.getDateTime() != null) {
		  cmdDateTime = cmd.getDateTime();
	  } else {
		  cmdDateTime = new DateTime(String.format("%02d", now.getDayOfMonth()) 
				                     + "/" 
				                     + String.format("%02d", now.getMonthValue())
				                     + "/" + now.getYear());
	  }
	  
	  Date date = Date.from(cmdDateTime.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  String message = "";
	  String feedbackMessage = "";
	  
	  if (cmd.getDisplayOptions().equals(PENDING)) {
		  feedbackMessage = "pending tasks";
		  for (int i=0; i<pending.size(); i++) {
			  taskDateTime = pending.get(i).getDateTime();
			  
			  if (isRelevantDate(cmdDateTime, taskDateTime)) {
				  isEmpty = false;
				  relevant.add(pending.get(i));
			  }
		  }
		  
		  if (isEmpty) {
			  // TODO add in "you have no pending tasks message", by creating a new task?
		  } else {
	          eventBus.setCurrentTasks(relevant);
		  }
		  
	      message = "Tasks pending for " + dateFormat.format(date);
		  
	  } else if (cmd.getDisplayOptions().equals(COMPLETED)) {
		  feedbackMessage = "completed tasks";
		  for (int i=0; i<completed.size(); i++) {
			  isEmpty = false;
			  relevant.add(completed.get(i));
		  }
	      
	      message = "Tasks completed on " + dateFormat.format(date);
		  
		  if (isEmpty) {
			  // TODO add message "You have no completed tasks!"
		  } else {
			  eventBus.setCurrentTasks(relevant);
			  //note: need to give this list somewhere
		  }
	  }
	  
	  eventBus.setHeadMessage(message);
	  
    return new Status("Displaying " + feedbackMessage, "success");
  }
  
  public boolean isRelevantDate(DateTime cmdDateTime, DateTime taskDateTime) {
	  LocalDate taskStart = taskDateTime.getStartDate();
	  LocalDate taskEnd = taskDateTime.getEndDate();
	  
	  LocalDate date = cmdDateTime.getStartDate();
	  
	  if (taskStart.isBefore(date) && taskEnd.isAfter(date)) {
		  return true;
	  } else if (taskStart.isBefore(date)) {
		  return true;
	  } else if (taskStart.isEqual(date) || taskEnd.isEqual(date)) {
		  return true;
	  } else {
		  return false;
	  }
  }
  
}
