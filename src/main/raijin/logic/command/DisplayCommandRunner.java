package raijin.logic.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import raijin.common.datatypes.Constants;
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

  private static final String TYPE_ALL = "a";			// display ALL PENDING
  private static final String TYPE_PENDING = "p";		// display PENDING (for today)
  private static final String TYPE_COMPLETED = "c";     // display COMPLETED
  private static final String TYPE_FLOATING = "f";		// display FLOATING
  private static final String TYPE_OVERDUE = "o";		// display OVERDUE
  
  private static final String FEEDBACK_DISPLAY = "Displaying: ";
  private static final String FEEDBACK_ALL_PENDING = "all pending tasks";
  private static final String FEEDBACK_PENDING = "pending tasks";
  private static final String FEEDBACK_COMPLETED = "completed tasks";
  private static final String MESSAGE_SUCCESS = "Success";
  private static final String MESSAGE_NO_PENDING = "You have no pending tasks!";
  private static final String MESSAGE_NO_COMPLETED = "You have no completed tasks!";
  
  private DateTime cmdDateTime;
  private DateTime taskDateTime;
  private LocalDate now;

  private ArrayList<Task> pending;
  private ArrayList<Task> completed;
  private ArrayList<Task> relevant;
  
  private EventBus eventBus = EventBus.getEventBus();
  
  final DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM ''yy");

  public Status processCommand(ParsedInput cmd) {
	  
	  now = LocalDate.now();
	  
	  retrieveLists();
	  cmdDateTime = getQueriedDate(cmd);
	  
	  Date date = Date.from(cmdDateTime.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  String message = "";
	  String feedbackMessage = "";
	  
	  boolean isEmpty = true;
	  
	  switch (cmd.getDisplayOptions()) {
	      case TYPE_PENDING:
	    	  feedbackMessage = FEEDBACK_PENDING;
	    	  for (int i=0; i<pending.size(); i++) {
				  taskDateTime = pending.get(i).getDateTime();
				  
				  if (isRelevantDate(cmdDateTime, taskDateTime)) {
					  isEmpty = false;
					  relevant.add(pending.get(i));
				  }
			  }
	    	  
	    	  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
			  } else {
		          eventBus.setCurrentTasks(relevant);
			  }
	    	  
	    	  message = "Tasks pending for " + dateFormat.format(date);
	    	  
	    	  break;
	    	  
	      case TYPE_ALL:
	    	  feedbackMessage = FEEDBACK_ALL_PENDING;
			  
			  if (pending.isEmpty()) {
				  eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
			  } else {
				  eventBus.setCurrentTasks(pending);
			  }
			  
			  message = "All pending tasks";
	    	 
	    	  break;
	    	  
	      case TYPE_FLOATING:
	    	  
	    	  break;
	    	  
	      case TYPE_COMPLETED:
	    	  feedbackMessage = FEEDBACK_COMPLETED;
			  for (int i=0; i<completed.size(); i++) {
				  isEmpty = false;
				  relevant.add(completed.get(i));
			  }
		      
		      message = "Tasks completed as of " + dateFormat.format(date);
			  
			  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_COMPLETED);
			  } else {
				  eventBus.setCurrentTasks(relevant);
			  }
			  
	    	  break;
	    	  
	      case TYPE_OVERDUE:
	    	  break;
	  }
	  
	  eventBus.setHeadMessage(message);
	  
    return new Status(FEEDBACK_DISPLAY + feedbackMessage, MESSAGE_SUCCESS);
  }
  
  /**
   * This method is used to retrieve the updated list of tasks from
   * TasksManager, and initialise a new list 'relevant' for filtering
   * out the relevant tasks to be displayed.
   */
  public void retrieveLists() {
	  pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
	  completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
	  relevant = new ArrayList<Task>();
  }
  
  public DateTime getQueriedDate(ParsedInput cmd) {
	  if (cmd.getDateTime() != null) {
		  cmdDateTime = cmd.getDateTime();
	  } else {
		  cmdDateTime = new DateTime(String.format("%02d", now.getDayOfMonth()) 
				                     + "/" 
				                     + String.format("%02d", now.getMonthValue())
				                     + "/" + now.getYear());
	  }
	  
	  return cmdDateTime;
  }
  
  /**
   * This method is used to determine whether a task's date is relevant
   * to the date that the user has specified. e.g. does start/end fall on
   * specified date, or the specified date falls in between the queried date.
   * 
   * @param cmdDateTime     Specified/queried DateTime by user.
   * @param taskDateTime	A task's DateTime.
   * @return true if relevant, false if otherwise
   */
  public boolean isRelevantDate(DateTime cmdDateTime, DateTime taskDateTime) {
	  LocalDate taskStart;
	  LocalDate taskEnd;
	 
	  // Don't display floating tasks
	  try {
		  taskStart = taskDateTime.getStartDate();
		  taskEnd = taskDateTime.getEndDate();
	  } catch (NullPointerException e) {
		  return false;
	  }
	  
	  LocalDate date = cmdDateTime.getStartDate();
	  
	  if (taskStart.isBefore(date) && taskEnd.isAfter(date)) {
		  return true;
	  } else if (taskStart.isBefore(date) && taskEnd.isBefore(date)) {
		  return false;
	  } else if (taskStart.isEqual(date) || taskEnd.isEqual(date)) {
		  return true;
	  } else {
		  return false;
	  }
  }
  
}
