package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;


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
  private String priority;                                         //Default priority level to medium
  private TreeSet<String> tags = new TreeSet<String>();            //Empty tag set when initialized
  private ArrayList<String> keywords;


  /*Constructor for flexible task*/
  public Task(String name, int id) {
    init(name, id);
  }

  /*Constructor for task or event*/
  public Task(String name, int id, ParsedInput input) {
    init(name, id);
    initExtra(input);
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
  public String[] getKeywords() {
    return name.split(" ");
  }

  @Override
  public boolean equals(Object ob2) {
    return ob2 instanceof Task && ((Task) ob2).getId() == getId() 
        && ((Task) ob2).getName().equals(getName());
  }
  
  /*Initialise most basic fields*/
  void init(String name, int id) {
    this.name = name;
    this.id = id;
    this.keywords = new ArrayList<String>(Arrays.asList(name.split(" ")));
  }
  
  /*Init non-mandatory fields for task*/
  void initExtra(ParsedInput input) {
    dateTime = input.getDateTime();
    priority = input.getPriority();
    if (input.getTags() != null) {                  //append only if initialized
      addTags(input.getTags());
    }
  }
}
