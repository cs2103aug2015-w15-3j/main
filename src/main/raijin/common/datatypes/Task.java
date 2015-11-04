// @@author A0112213E

package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;


/**
 * Represents details of a task created by user
 * 
 * @author papa
 *
 */
public class Task implements Comparable<Task> {

  private int id; // unique id that describes a task
  private String name; // description of a task
  private DateTime dateTime;
  private String priority;
  private TreeSet<String> tags = new TreeSet<String>();
  private TreeSet<Integer> subTasks = new TreeSet<Integer>();
  private ArrayList<String> keywords;
  private Constants.TYPE_TASK type; // floating, event or normal task


  /* Creates a floating task */
  public Task(String name, int id) {
    init(name, id);
    setTypeOfTask();
  }

  /* Creates a normal task with deadline */
  public Task(String name, int id, ParsedInput input) {
    init(name, id);
    initExtra(input);
    setTypeOfTask();
  }


  void setTypeOfTask() {
    if (dateTime == null) {
      type = Constants.TYPE_TASK.FLOATING;
    } else {

      if (dateTime.getStartTime() == null || dateTime.getStartTime().equals(dateTime.getEndTime())
          && dateTime.getStartDate().equals(dateTime.getEndDate())) {
        type = Constants.TYPE_TASK.SPECIFIC;
      } else {
        type = Constants.TYPE_TASK.EVENT;
      }
    }
  }

  void extractTags(ParsedInput input) {
    if (input.getTags() != null) {
      addTags(input.getTags());
    }
  }

  /* Initialise most basic fields */
  void init(String name, int id) {
    assert (id > 0);
    this.name = name;
    this.id = id;
    this.keywords = new ArrayList<String>(Arrays.asList(name.split(" ")));
  }

  /* Initialise non-mandatory fields for task */
  void initExtra(ParsedInput input) {
    dateTime = input.getDateTime();
    priority = input.getPriority() == null ? "m" : input.getPriority();
    extractTags(input);
  }

  void lazyUpdateSubTasks() {
    HashMap<Integer, Task> pendingTasks = TasksManager.getManager().getPendingTasks();
    List<Integer> filtered =
        subTasks.stream().filter(x -> pendingTasks.containsKey(x)).collect(Collectors.toList());
    subTasks = new TreeSet<Integer>(filtered);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public DateTime getDateTime() {
    return dateTime;
  }


  public String getPriority() {
    return priority;
  }

  public TreeSet<String> getTags() {
    return tags;
  }

  public void addTags(TreeSet<String> input) {
    tags.addAll(input);
  }

  /* Returns keywords from task description */
  public ArrayList<String> getKeywords() {
    return keywords;
  }

  public TreeSet<Integer> getSubTasks() {
    lazyUpdateSubTasks(); // Update status of subtasks
    return subTasks;
  }

  public void addSubTask(int taskID) {
    subTasks.add(taskID);
  }

  public void removeSubTask(int taskID) {
    subTasks.remove(taskID);
  }

  public Constants.TYPE_TASK getType() {
    return type;
  }

  boolean compareDateTime(DateTime target) {
    if (getDateTime() == null) {
      return target == null;
    } else {
      return getDateTime().equals(target);
    }
  }

  @Override
  public boolean equals(Object ob2) {
    return ob2 instanceof Task && ((Task) ob2).getName().equals(getName())
        && compareDateTime(((Task) ob2).getDateTime());
  }

  @Override
  public int compareTo(Task other) {

    if (this.getType().equals(Constants.TYPE_TASK.FLOATING)) {

      // Case #1: both tasks being compared are floating tasks.
      if (other.getType().equals(Constants.TYPE_TASK.FLOATING)) {
        return this.getName().compareToIgnoreCase(other.getName());
      }

      // Case #2: this task is floating while the other is not.
      else {
        return 1;
      }

    } else {

      // Case #3: this task is not floating, the other is floating.
      if (other.getType().equals(Constants.TYPE_TASK.FLOATING)) {
        return -1;
      }

      // Case #4: both tasks are not floating.
      else {
        return this.getDateTime().compareTo(other.getDateTime());
      }
    }
  }

}
