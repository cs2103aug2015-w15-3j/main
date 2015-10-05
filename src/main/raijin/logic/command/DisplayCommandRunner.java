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
	
  private static final String TYPE_PENDING = "p";
  private static final String TYPE_COMPLETED = "c";
  private static final String MESSAGE_DISPLAY = "Displaying: ";
  private static final String MESSAGE_PENDING = "pending tasks";
  private static final String MESSAGE_COMPLETED = "completed tasks";
  private static final String MESSAGE_SUCCESS = "Success";
  
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
	  
	  if (cmd.getDisplayOptions().equals(TYPE_PENDING)) {
		  feedbackMessage = MESSAGE_PENDING;
		  for (int i=0; i<pending.size(); i++) {
			  taskDateTime = pending.get(i).getDateTime();
			  
			  if (isRelevantDate(cmdDateTime, taskDateTime)) {
				  isEmpty = false;
				  relevant.add(pending.get(i));
			  }
		  }
		  
		  if (isEmpty) {
			  eventBus.setCurrentTasks("You have no pending tasks!");
		  } else {
	          eventBus.setCurrentTasks(relevant);
	        //TODO note: need to give this list somewhere
		  }
		  
	      message = "Tasks pending for " + dateFormat.format(date);
		  
	  } else if (cmd.getDisplayOptions().equals(TYPE_COMPLETED)) {
		  feedbackMessage = MESSAGE_COMPLETED;
		  for (int i=0; i<completed.size(); i++) {
			  isEmpty = false;
			  relevant.add(completed.get(i));
		  }
	      
	      message = "Tasks completed on " + dateFormat.format(date);
		  
		  if (isEmpty) {
			  eventBus.setCurrentTasks("You have no completed tasks!");
		  } else {
			  eventBus.setCurrentTasks(relevant);
		  }
	  }
	  
	  eventBus.setHeadMessage(message);
	  
    return new Status(MESSAGE_DISPLAY + feedbackMessage, MESSAGE_SUCCESS);
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
