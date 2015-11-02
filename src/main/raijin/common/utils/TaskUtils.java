package raijin.common.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import edu.emory.mathcs.backport.java.util.Collections;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;

public class TaskUtils {
	final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy");

  public static List<Task> initSort(List<Task> tasks) {
	  ArrayList<Task> list = new ArrayList<Task>(tasks);
	  Collections.sort(list);
	  
	  return list;
  }
  
  public static List<Task> initTasks(HashMap<Integer, Task> pendingTasks) {
	  List<Task> list = initSort(new ArrayList<Task>(pendingTasks.values()));
	  
	  return list;
  }

  public static List<TaskPane> initTasks(List<Task> tasks) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();

    if (tasks.isEmpty()) {
      return displayMessage("You have no pending tasks!");
    }

    for (int i = 0; i < tasks.size(); i++) {
      list.add(new TaskPane(i + 1, tasks.get(i)));
    }

    return list;
  }

  public static List<TaskPane> convertToTaskPane(List<Task> tasks) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();
    
    if (tasks.isEmpty()) {
        return displayMessage("You have no pending tasks!");
    }
    
    for (int i = 0; i < tasks.size(); i++) {
    	Task task = tasks.get(i);
    	list.add(new TaskPane(i + 1, task));
    }
    
    return list;
  }
  
  public static List<TaskPane> convertToTaskPaneDefaultView (List<Task> tasks) {
	  ArrayList<TaskPane> list = new ArrayList<TaskPane>();
	  LocalDate today = LocalDate.now();
	  String todayString = today.format(dateFormat);
	  LocalDate tomorrow = today.plusDays(1);
	  String tomorrowString = tomorrow.format(dateFormat);
	  
	  list.add(new TaskPane ("Today - " + todayString + "")); //TodayPane
	  
	  boolean todayIsEmpty = true;
	  boolean tomorrowIsEmpty = true;
	  LocalDate taskStartDate;
	  
	  int i = 0;
	  for (i = 0; i < tasks.size(); i++) {
	    	Task task = tasks.get(i);
	    	
	    	try {
	    		taskStartDate = task.getDateTime().getStartDate();
	    	} catch (NullPointerException e) {
	    		taskStartDate = today.plusDays(2);
	    	}
	    	
	    	if (taskStartDate.isAfter(today)) {
	    		break;
	    	}
	    	
	    	list.add(new TaskPane(i + 1, task));
	    	todayIsEmpty = false;
	    
	  }
	  
	  if (todayIsEmpty) {
			list.add(new TaskPane ("No pending tasks!"));
	  }
	  
	  list.add(new TaskPane ("Tomorrow - " + tomorrowString + ""));
	  
	  int j;
	  for (j=i; j<tasks.size(); j++) {
		  Task task = tasks.get(j);
		  
		  try {
	    		taskStartDate = task.getDateTime().getStartDate();
	      } catch (NullPointerException e) {
	    		taskStartDate = today.plusDays(2);
	      }
	    	
	      if (taskStartDate.isAfter(tomorrow)) {
	      	  break;
	      }
	      
	      list.add(new TaskPane (j+1, task));
	      tomorrowIsEmpty = false;
		  
	  }
	  
	  if (tomorrowIsEmpty) {
		  list.add(new TaskPane ("No pending tasks!"));
	  }
	  
	  list.add(new TaskPane ("Future"));
	  
	  boolean futureIsEmpty = true;
	  for (int k=j; k<tasks.size(); k++) {
		  Task task = tasks.get(k);
		  list.add(new TaskPane (k + 1, task));
		  futureIsEmpty = false;
	  }
	  
	  if (futureIsEmpty) {
		  list.add(new TaskPane ("No pending tasks!"));
	  }
	  
	  
	  return list;
	  
  }
  

  public static List<TaskPane> displayMessage(String message) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();
    list.add(new TaskPane(message));


    return list;
  }

  public static List<String> filterName(List<Task> tasks) {
    return tasks
        .stream()
        .map(
            (Task t) -> (tasks.indexOf(t) + 1)
                + " : "
                + (t.getName().length() > 60 ? t.getName().substring(0, 59) + "..." : t.getName())
                + (t.getDateTime() == null ? "" : " [ "
                    + String.format("%02d", t.getDateTime().getStartDate().getDayOfMonth())
                    + "/"
                    + String.format("%02d", t.getDateTime().getStartDate().getMonthValue())
                    + "/"
                    + t.getDateTime().getStartDate().getYear()
                    + " to "
                    + String.format("%02d", t.getDateTime().getEndDate().getDayOfMonth())
                    + "/"
                    + String.format("%02d", t.getDateTime().getEndDate().getMonthValue())
                    + "/"
                    + t.getDateTime().getEndDate().getYear()
                    + " | "
                    + (t.getDateTime().getStartTime() == null ? "" : t.getDateTime().getStartTime()
                        .toString())
                    + (t.getDateTime().getEndTime() == null ? "" : " - "
                        + t.getDateTime().getEndTime().toString()) + " ]"))
        .collect(Collectors.toList());

  }

  // ===========================================================================
  // Helper functions to retrieve different properties of task
  // ===========================================================================

  /**
   * 
   * @param pendingTasks
   * @return set of tags
   */
  public static TreeSet<String> getTags(HashMap<Integer, Task> pendingTasks) {
    TreeSet<String> tags = new TreeSet<String>();

    for (Task task : pendingTasks.values()) {
      tags.addAll(task.getTags());
    }
    return tags;
  }

  /**
   * Keep track of tasks associated with a tag
   * @param pendingTasks
   * @return
   */
  public static Multiset<String> getTagMultiSet(List<Task> pendingTasks) {
    Multiset<String> tags = HashMultiset.create();
    for (Task task : pendingTasks) {
      tags.addAll(task.getTags());
    }
    return tags;
  }

  /**
   * Retrieves set of task names
   * 
   * @return set of task names
   */
  public static TreeSet<String> getTaskNames(HashMap<Integer, Task> pendingTasks) {
    List<String> names =
        pendingTasks.entrySet().stream().map(e -> e.getValue().getName())
            .collect(Collectors.toList());
    return new TreeSet<String>(names);
  }
  
 
  public static List<Task> filterTaskWithTags(HashMap<Integer, Task> pendingTasks,
      TreeSet<String> tags) {
      
      List<String> sanitizedTags = getSanitizedTags(tags);
      List<Task> filtered = pendingTasks.values().stream().filter(
            t -> !CollectionUtils.intersection(t.getTags(), sanitizedTags).isEmpty())
            .collect(Collectors.toList());
      return filtered;
  }
  
  static String removeHashTag(String tag) {
    if (tag.startsWith("#")) {
      return tag.substring(1);
    }
    return tag;
  }
  
  public static List<String> getSanitizedTags(TreeSet<String> tags) {
    return tags.stream().map(tag -> removeHashTag(tag)).collect(Collectors.toList());
  }
  
  public static List<Task> filterTaskWithName(HashMap<Integer, Task> pendingTasks, 
      String name) {
    List<Task> filtered = pendingTasks.values().stream().filter(
        t -> t.getName().equals(name)).collect(Collectors.toList());
    return filtered;
  }

  TreeSet<Integer> getIdsFromTasks(List<Task> filtered) {
    return new TreeSet<Integer>(filtered.stream().map(
        t -> t.getId()).collect(Collectors.toList()));
  }
  
  public static List<Task> getTasksList(HashMap<Integer, Task> tasks) {
    List<Task> result = new ArrayList<Task>(tasks.values());
    Collections.sort(result);
    return result;
  }

  /*Exclude floating tasks*/
  public static List<Task> getOnlyNormalTasks(List<Task> tasks) {
    return tasks.stream().filter(t -> t.getType() != Constants.TYPE_TASK.FLOATING).
        collect(Collectors.toList());
  }
}
