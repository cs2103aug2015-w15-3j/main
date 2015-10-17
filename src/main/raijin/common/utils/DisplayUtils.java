package raijin.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Task;

public class DisplayUtils {

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
    
    // TODO Below for reference, to be removed later.
    
    /*
     * return tasks.stream().map(
        (Task t) -> t.getId() + " : " + t.getName() + " by " +
         (t.getDateTime() == null ? "?" : t.getDateTime().getEndDate().toString())
        ).collect(Collectors.toList());
     */
    
    /*
     * return tasks.stream().map(
        (Task t) -> t.getId() + " : " + t.getName()).collect(Collectors.toList());
        
        String.format("%02d", t.getDateTime().getEndDate().getDayOfMonth()) 
                          + "/" 
                          + String.format("%02d", t.getDateTime().getEndDate().getMonthValue())
                          + "/" + t.getDateTime().getEndDate().getYear())
                          
     */
  }
}
