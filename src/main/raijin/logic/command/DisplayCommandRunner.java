package raijin.logic.command;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.ListView;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.ListDisplayContainer;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class DisplayCommandRunner extends CommandRunner {
	
  private static final String PENDING = "p";
  private static final String COMPLETED = "c";
  
  private DateTime cmdDateTime;
  private DateTime taskDateTime;
  private LocalDate now;

  private ArrayList<Task> pending;
  private ArrayList<Task> completed;
  
  private ListView<String> listView;
  
  //need to somehow get the instance of displaycontroller
  //private DisplayController dc = 

  public Status execute(ParsedInput cmd) {
	  // Getting the current DateTime
	  now = LocalDate.now();
	  listView = new ListView<String>();
	  
	  pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
	  completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
	  
	  if (cmd.getDisplayOptions().equals(PENDING)) {
		  try {
		  cmdDateTime = cmd.getDateTime();
		  } catch (NullPointerException e) {
			  cmdDateTime = new DateTime(now.toString());
		  }
		  
		  for (int i=0; i<pending.size(); i++) {
			  taskDateTime = pending.get(i).getDateTime();
			  
			  if (isRelevantDate(cmdDateTime, taskDateTime)) {
				  listView.getItems().add(pending.get(i).getName() + " by " + pending.get(i).getDateTime().getEndDate().toString());
			  }
		  }
		  
		  // pass this listView to displaycontroller
		  
	  } else if (cmd.getDisplayOptions().equals(COMPLETED)) {
		  
	  }

	  
	  
    return null;
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
