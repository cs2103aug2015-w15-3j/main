package raijin.logic.command;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.commons.lang.RandomStringUtils;

import javafx.scene.control.ListView;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.utils.EventBus;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;
import raijin.ui.DisplayController;

public class DisplayCommandRunner extends CommandRunner {
	
  private static final String PENDING = "p";
  private static final String COMPLETED = "c";
  
  private DateTime cmdDateTime;
  private DateTime taskDateTime;
  private LocalDate now;

  private ArrayList<Task> pending;
  private ArrayList<Task> completed;
  
  private ListView<String> listView;
  
  private EventBus eventBus = EventBus.getEventBus();

  public Status processCommand(ParsedInput cmd) {
	  // Getting the current DateTime
	  now = LocalDate.now();
	  listView = new ListView<String>();
	  
	  pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
	  completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
	  
	  boolean isEmpty = true;
	  if (cmd.getDisplayOptions().equals(PENDING)) {
		  
	    eventBus.setCurrentTasks(pending);
	    /*
		  if (cmd.getDateTime() != null) {
			  cmdDateTime = cmd.getDateTime();
		  } else {
			  cmdDateTime = new DateTime(String.format("%02d", now.getDayOfMonth()) 
					                     + "/" 
					                     + String.format("%02d", now.getMonthValue())
					                     + "/" + now.getYear());
		  }
		  
		  
		  for (int i=0; i<pending.size(); i++) {
			  taskDateTime = pending.get(i).getDateTime();
			  isEmpty = false;
			  
			  test.add(pending.get(i).getName());
			  if (isRelevantDate(cmdDateTime, taskDateTime)) {
				  listView.getItems().add(pending.get(i).getName() + " by " + 
			                              pending.get(i).getDateTime().getEndDate().toString());
			  }
		  }
		  
		  if (isEmpty) {
			  listView.getItems().add("You have no pending tasks!");
		  }
		  
		  */
		  
	  } else if (cmd.getDisplayOptions().equals(COMPLETED)) {

	      eventBus.setCurrentTasks(completed);
	    /*
		  for (int i=0; i<completed.size(); i++) {
			  isEmpty = false;
			  listView.getItems().add(completed.get(i).getName());
		  }
		  
		  if (isEmpty) {
			  listView.getItems().add("You have no completed tasks!");
		  }
		  */
	  }
	  
	  eventBus.setHeadMessage(RandomStringUtils.random(6));

	  /*
	  // pass the cmdDateTime to displaycontroller
	  dc.setHeadMessage(cmdDateTime);
	  
	  // pass this listView to displaycontroller
	  dc.setListView(listView);
	  */
	  
    return new Status("Displaying", "success");
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
