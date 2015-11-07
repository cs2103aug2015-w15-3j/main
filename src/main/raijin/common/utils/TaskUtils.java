//@@author A0130720Y

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
  final static String MESSAGE_NO_PENDING = "You have no pending tasks!";
  final static String MESSAGE_DEFAULT_NO_TASKS = "No pending tasks!";

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
        return displayMessage(MESSAGE_NO_PENDING);
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
			list.add(new TaskPane (MESSAGE_DEFAULT_NO_TASKS));
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
		  list.add(new TaskPane (MESSAGE_DEFAULT_NO_TASKS));
	  }
	  
	  list.add(new TaskPane ("Future"));
	  
	  boolean futureIsEmpty = true;
	  for (int k=j; k<tasks.size(); k++) {
		  Task task = tasks.get(k);
		  list.add(new TaskPane (k + 1, task));
		  futureIsEmpty = false;
	  }
	  
	  if (futureIsEmpty) {
		  list.add(new TaskPane (MESSAGE_DEFAULT_NO_TASKS));
	  }
	  return list;
	  
  }

  public static List<TaskPane> displayMessage(String message) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();
    list.add(new TaskPane(message));


    return list;
  }

  //@@author A0112213E
  // ===========================================================================
  // Helper functions to retrieve different properties of task
  // ===========================================================================

  /**
   * Get set of tags created by user
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
   * Keep track of tasks associated with a tag for display on sidebar
   * @param pendingTasks
   * @return
   */
  //@@author A0112213E
  public static Multiset<String> getTagMultiSet(List<Task> pendingTasks) {
    Multiset<String> tags = HashMultiset.create();
    for (Task task : pendingTasks) {
      tags.addAll(task.getTags());
    }
    return tags;
  }

  /**
   * Retrieves set of task names
   * @return set of task names
   */
  //@@author A0112213E
  public static TreeSet<String> getTaskNames(HashMap<Integer, Task> pendingTasks) {
    List<String> names =
        pendingTasks.entrySet().stream().map(e -> e.getValue().getName())
            .collect(Collectors.toList());
    return new TreeSet<String>(names);
  }
  
 
  /**
   * Get tasks that matches name provided
   * @param pendingTasks
   * @param name
   * @return
   */
  //@@author A0112213E
  public static List<Task> filterTaskWithName(HashMap<Integer, Task> pendingTasks, 
      String name) {
    List<Task> filtered = pendingTasks.values().stream().filter(
        t -> t.getName().equals(name)).collect(Collectors.toList());
    return filtered;
  }

  /**
   * Get a list of tasks that matches these tags 
   * @param pendingTasks    
   * @param tags            tags that will be matched with
   * @return filtered       tasks associated with all the tags 
   */
  //@@author A0112213E
  public static List<Task> filterTaskWithTags(HashMap<Integer, Task> pendingTasks,
      TreeSet<String> tags) {
      
      List<String> sanitizedTags = getSanitizedTags(tags);
      List<Task> filtered = pendingTasks.values().stream().filter(
            t -> !CollectionUtils.intersection(t.getTags(), sanitizedTags).isEmpty())
            .collect(Collectors.toList());
      return filtered;
  }
  
  /*Sanitize tag input*/
  //@@author A0112213E
  static String removeHashTag(String tag) {
    if (tag.startsWith("#")) {
      return tag.substring(1);
    }
    return tag;
  }
  
  //@@author A0112213E
  public static List<String> getSanitizedTags(TreeSet<String> tags) {
    return tags.stream().map(tag -> removeHashTag(tag)).collect(Collectors.toList());
  }
  

  //@@author A0112213E
  TreeSet<Integer> getIdsFromTasks(List<Task> filtered) {
    return new TreeSet<Integer>(filtered.stream().map(
        t -> t.getId()).collect(Collectors.toList()));
  }
  
  /**
   * Returns sorted pending tasks without ids attached
   * @param tasks
   * @return
   */
  //@@author A0112213E
  public static List<Task> getTasksList(HashMap<Integer, Task> tasks) {
    List<Task> result = new ArrayList<Task>(tasks.values());
    Collections.sort(result);
    return result;
  }

  /**
   * Get only tasks that has a deadline
   * @param tasks
   * @return
   */
  //@@author A0112213E
  public static List<Task> getOnlyNormalTasks(List<Task> tasks) {
    return tasks.stream().filter(t -> t.getType() != Constants.TYPE_TASK.FLOATING).
        collect(Collectors.toList());
  }
}
