package raijin.logic.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

//import java.util.Collections;
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
  private static final String FEEDBACK_FLOATING = "floating tasks";
  private static final String FEEDBACK_ALL_PENDING = "all pending tasks";
  private static final String FEEDBACK_PENDING = "pending tasks";
  private static final String FEEDBACK_COMPLETED = "completed tasks";
  private static final String FEEDBACK_OVERDUE = "overdue tasks";
  
  private static final String MESSAGE_SUCCESS = "Success";
  private static final String MESSAGE_NO_PENDING = "You have no pending tasks!";
  private static final String MESSAGE_NO_COMPLETED = "You have no completed tasks!";
  private static final String MESSAGE_NO_FLOATING = "You have no pending floating tasks!";
  private static final String MESSAGE_NO_OVERDUE = "You have no overdue tasks! Well done!";
  
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
	  
	  Date dateForDisplay = Date.from(cmdDateTime.getStartDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	  String message = "";
	  String feedbackMessage = "";
	  
	  boolean isEmpty = true;
	  Task currentTask;
	  
	  switch (cmd.getDisplayOptions()) {
	      case TYPE_PENDING:
	    	  feedbackMessage = FEEDBACK_PENDING;
	    	  for (int i=0; i<pending.size(); i++) {
	    		  currentTask = pending.get(i);
	    		  
				  taskDateTime = currentTask.getDateTime();
				  
				  if (currentTask.getType() != Constants.TYPE_TASK.FLOATING && 
					  isRelevantDate(cmdDateTime, taskDateTime)) {
					  
					  isEmpty = false;
					  relevant.add(currentTask);
				  }
			  }
	    	  
	    	  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
			  } else {
				  Collections.sort(relevant);
		          eventBus.setCurrentTasks(relevant);
			  }
	    	  
	    	  message = "Tasks pending for " + dateFormat.format(dateForDisplay);
	    	  
	    	  break;
	    	  
	      case TYPE_ALL:
	    	  feedbackMessage = FEEDBACK_ALL_PENDING;
			  
			  if (pending.isEmpty()) {
				  eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
			  } else {
				  Collections.sort(pending);
				  eventBus.setCurrentTasks(pending);
			  }
			  
			  message = "All pending tasks";
	    	 
	    	  break;
	    	  
	      case TYPE_FLOATING:
	    	  feedbackMessage = FEEDBACK_FLOATING;
	    	  
	    	  for(int i=0; i<pending.size(); i++) {
	    		  currentTask = pending.get(i);
	    		  
	    		  if (currentTask.getType().equals(Constants.TYPE_TASK.FLOATING)) {
	    			  relevant.add(currentTask);
	    			  isEmpty = false;
	    		  }
	    	  }
	    	  
	    	  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_FLOATING);
			  } else {
		          eventBus.setCurrentTasks(relevant);
			  }
	    	  
	    	  message = "All floating tasks";
	    	  
	    	  break;
	    	  
	      case TYPE_COMPLETED:
	    	  feedbackMessage = FEEDBACK_COMPLETED;
	    	  
			  for (int i=0; i<completed.size(); i++) {
				  currentTask = completed.get(i);
				  relevant.add(currentTask);
				  isEmpty = false;
			  }
		     
			  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_COMPLETED);
			  } else {
				  Collections.sort(relevant);
				  eventBus.setCurrentTasks(relevant);
			  }
			  
			  message = "Tasks completed as of " + dateFormat.format(dateForDisplay);
			  
	    	  break;
	    	  
	      case TYPE_OVERDUE:
	    	  feedbackMessage = FEEDBACK_OVERDUE;
	    	  
	    	  for(int i=0; i<pending.size(); i++) {
	    		  currentTask = pending.get(i);
	    		  DateTime currentTaskDateTime;
	    		  
	    		  try {
	    			  currentTaskDateTime = currentTask.getDateTime();
	    		  } catch (NullPointerException e) {
	    			  continue;
	    		  }
	    		  
    		      if (isOverdue(currentTaskDateTime)) {
    			      relevant.add(currentTask);
    			      isEmpty = false;
    		      }
	    	  }
	    	  
	    	  if (isEmpty) {
				  eventBus.setCurrentTasks(MESSAGE_NO_OVERDUE);
			  } else {
				  Collections.sort(relevant);
		          eventBus.setCurrentTasks(relevant);
			  }
	    	  
	    	  message = "All overdue tasks";
	    	  
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
  
  /**
   * This method returns a DateTime object for the queried date
   * as specified by user. If no date has been specified, it
   * defaults to the today's date.
   * 
   * @param cmd			ParsedInput that the user keyed in.
   * @return the queried DateTime
   */
  
  public DateTime getQueriedDate(ParsedInput cmd) {
	  if (cmd.getDateTime() != null) {
		  cmdDateTime = cmd.getDateTime();
	  } else {
		  cmdDateTime = new DateTime(String.format("%02d", now.getDayOfMonth()) 
				                     + "/" 
				                     + String.format("%02d", now.getMonthValue())
				                     + "/"
				                     + now.getYear());
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
	 
	  //TODO this needs to change. start=end default?
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
  
  /**
   * This method determines whether a task is overdue as of today.
   * 
   * @param taskDateTime    A task's DateTime.
   * @return
   */
  public boolean isOverdue(DateTime taskDateTime) {
	  LocalDate taskEnd;
	  
	  try {
		  taskEnd = taskDateTime.getEndDate();
	  } catch (NullPointerException e) {
		  return false;
	  }
	  
	  if (taskEnd.isBefore(now)) {
		  return true;
	  } else {
		  return false;
	  }
  }
  
}
