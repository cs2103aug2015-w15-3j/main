package raijin.common.datatypes;


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
  private IDManager idManager = IDManager.getIdManager();          //To generate unique id 
  private Constants.Priority priority = Constants.Priority.MID;    //Default priority level to medium


  /*Constructor for flexible task*/
  public Task(String name) {
    this.id = idManager.getId();
    this.name = name;
  }

  
  /*Constructor for task or event*/
  public Task(String name, DateTime dateTime) {
    this.id = idManager.getId();
    this.name = name;
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

}
