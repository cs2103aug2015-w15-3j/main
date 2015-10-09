package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import raijin.common.utils.IDManager;


/**
 * 
 * @author papa
 * This is a basic unit of todo that is used to describe details of a todo
 *
 */
public class Task {
  
  private int id;                                                  //Unique id that describes a task
  private String name;                                             //Description of a task
  private DateTime dateTime;
  private Constants.Priority priority = Constants.Priority.MID;    //Default priority level to medium
  private TreeSet<String> tags = new TreeSet<String>();            //Empty tag set when initialized
  private ArrayList<String> keywords;


  /*Constructor for flexible task*/
  public Task(String name, int id) {
    init(name, id);
  }

  
  /*Constructor for task or event*/
  public Task(String name, int id, DateTime dateTime) {
    init(name, id);
    this.dateTime = dateTime;
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


  public Constants.Priority getPriority() {
    return priority;
  }


  /*Needed after undo or redo*/
  public void setId(int id) {
   this.id = id;
  }

  public void setPriority(Constants.Priority priority) {
    this.priority = priority;
  }


  public TreeSet<String> getTags() {
    return tags;
  }


  public void addTags(String tag) {
    tags.add(tag.toLowerCase().trim()); //Sanitize string before adding 
  }

  public void removeTags(String tag) {
    tags.remove(tag);
  }

  /*Returns keywords from task description*/
  public String[] getKeywords() {
    return name.split(" ");
  }

  @Override
  public boolean equals(Object ob2) {
    return ob2 instanceof Task && ((Task) ob2).getId() == getId() 
        && ((Task) ob2).getName().equals(getName());
  }
  
  void init(String name, int id) {
    this.name = name;
    this.id = id;
    String[] keywords = name.split(" ");
    this.keywords = new ArrayList<String>(Arrays.asList(keywords));
  }
}
