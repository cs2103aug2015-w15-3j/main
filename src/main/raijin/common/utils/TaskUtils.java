package raijin.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import edu.emory.mathcs.backport.java.util.Collections;
import raijin.common.datatypes.Task;

public class TaskUtils {

  public static List<Task> initSort(List<Task> tasks) {
	  ArrayList<Task> list = new ArrayList<Task>(tasks);
	  Collections.sort(list);
	  
	  return list;
  }

  public static List<TaskPane> initTasks(List<Task> tasks) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();

    if (tasks.isEmpty()) {
      return displayMessage("You have no pending tasks!");
    }

    for (int i = 0; i < tasks.size(); i++) {
      list.add(new TaskPane(i + 1, tasks.get(i), "none"));
    }

    return list;
  }

  public static List<TaskPane> convertToTaskPane(List<Task> tasks) {
    ArrayList<TaskPane> list = new ArrayList<TaskPane>();
    
    if (tasks.isEmpty()) {
        return displayMessage("You have no pending tasks!");
    }

    for (int i = 0; i < tasks.size(); i++) {
      list.add(new TaskPane(i + 1, tasks.get(i), "none"));
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
}
