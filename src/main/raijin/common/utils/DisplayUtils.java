package raijin.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.emory.mathcs.backport.java.util.Collections;
import raijin.common.datatypes.Task;

public class DisplayUtils {
	
  public static List<TaskPane> initTasks(List<Task> tasks) {
	  ArrayList<TaskPane> list = new ArrayList<TaskPane>();
	  Collections.sort(tasks);
	  
	  for (int i=0; i<tasks.size(); i++) {
		  list.add(new TaskPane(tasks.get(i), "none"));
	  }
	  
	  return list;
  }
  
  public static List<TaskPane> displayMessage(String message) {
	  ArrayList<TaskPane> list = new ArrayList<TaskPane>();
	  list.add(new TaskPane(message));
	  
	  
	  return list;
  }

  public static List<String> filterName(List<Task> tasks) {
    return tasks.stream().map(
        (Task t) -> (tasks.indexOf(t)+1) + " : " 
        		 + (t.getName().length() > 60 ? t.getName().substring(0, 59) + "..." : t.getName()) 
        		 + (t.getDateTime() == null ? "" : " [ " 
        		 + String.format("%02d", t.getDateTime().getStartDate().getDayOfMonth()) 
                 + "/" 
                 + String.format("%02d", t.getDateTime().getStartDate().getMonthValue())
                 + "/" + t.getDateTime().getStartDate().getYear()
                 + " to "
        		 + String.format("%02d", t.getDateTime().getEndDate().getDayOfMonth()) 
                 + "/" 
                 + String.format("%02d", t.getDateTime().getEndDate().getMonthValue())
                 + "/" + t.getDateTime().getEndDate().getYear()
                 + " | "
                 + (t.getDateTime().getStartTime() == null ? "" : t.getDateTime().getStartTime().toString())
                 + (t.getDateTime().getEndTime() == null ? "" : " - " + t.getDateTime().getEndTime().toString()) + " ]")
        ).collect(Collectors.toList());
    
  }
}
