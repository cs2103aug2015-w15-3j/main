//@@author A0112213E

package raijin.common.datatypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;


/**
 * 
 * @author papa
 * This is a basic unit of todo that is used to describe details of a todo
 *
 */
public class Task implements Comparable<Task> {
  
  private int id;                                                  //Unique id that describes a task
  private String name;                                             //Description of a task
  private DateTime dateTime;
  private String priority;                                         //Default priority level to medium
  private TreeSet<String> tags = new TreeSet<String>();            //Empty tag set when initialized
  private TreeSet<Integer> subTasks = new TreeSet<Integer>();
  private String project;
  private ArrayList<String> keywords;
  private Constants.TYPE_TASK type;


  /*Constructor for flexible task*/
  public Task(String name, int id) {
    init(name, id);
    setTypeOfTask();
  }

  /*Constructor for task or event*/
  public Task(String name, int id, ParsedInput input) {
    init(name, id);
    initExtra(input);
    setTypeOfTask();
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public DateTime getDateTime() {
    return dateTime;
  }


  public String getPriority() {
    return priority;
  }


  public String getProject() {    
    return project;
  }

  /*Needed after undo or redo*/
  public void setId(int id) {
   this.id = id;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public void setDateTime(DateTime dateTime) {
    this.dateTime = dateTime;
  }

  public TreeSet<String> getTags() {
    return tags;
  }


  public void addTags(TreeSet<String> input) {
    tags.addAll(input); 
  }

  public void removeTags(String tag) {
    tags.remove(tag);
  }

  /*Returns keywords from task description*/
  public ArrayList<String> getKeywords() {
    return keywords;
  }

  public void addSubTask(int taskID) {
    subTasks.add(taskID);
  }
  
  public void removeSubTask(int taskID) {
    subTasks.remove(taskID);
  }

  public TreeSet<Integer> getSubTasks() {
    lazyUpdateSubTasks();                       //Update status of subtasks
    return subTasks;
  }

  /*Returns type of a task*/
  public Constants.TYPE_TASK getType() {
    return type;
  }

  @Override
  public boolean equals(Object ob2) {
    return ob2 instanceof Task 
        && ((Task) ob2).getName().equals(getName())
        && compareDateTime(((Task) ob2).getDateTime());
  }
  
  /*Initialise most basic fields*/
  void init(String name, int id) {
    assert(id > 0);
    this.name = name;
    this.id = id;
    this.keywords = new ArrayList<String>(Arrays.asList(name.split(" ")));
  }
  
  /*Init non-mandatory fields for task*/
  void initExtra(ParsedInput input) {
    dateTime = input.getDateTime();
    priority = input.getPriority() == null ? "m" : input.getPriority();
    project = input.getProject();
    extractTags(input);
  }
  
  void extractTags(ParsedInput input) {
    if (input.getTags() != null) {                 
      addTags(input.getTags());
    }
  }

  void extractSubTasks(ParsedInput input) {
    if (input.getSubTaskOf() != 0) {                 
      addSubTask(input.getSubTaskOf());
    }
  }
  
  void lazyUpdateSubTasks() {
    HashMap<Integer, Task> pendingTasks = TasksManager.getManager().getPendingTasks();
    List<Integer> filtered = subTasks.stream().filter(x -> pendingTasks.
        containsKey(x)).collect(Collectors.toList());
    subTasks = new TreeSet<Integer>(filtered);
  }
  
  boolean compareDateTime(DateTime target) {
    if (getDateTime() == null) {
      return target == null;
    } else {
      return getDateTime().equals(target);
    }
  }
  
  void setTypeOfTask() {    
    if (dateTime == null) {
      type = Constants.TYPE_TASK.FLOATING;
    } else {
      
      if (dateTime.getStartTime() == null || dateTime.getStartTime().equals(
          dateTime.getEndTime())
        && dateTime.getStartDate().equals(dateTime.getEndDate())) {
        type = Constants.TYPE_TASK.SPECIFIC;
      } else {
        type = Constants.TYPE_TASK.EVENT;
      }

    }
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
