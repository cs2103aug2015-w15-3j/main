package raijin.util.Task;

import raijin.util.Time.BaseDateTime;
import raijin.util.Time.SimpleDateTime;

public class SimpleTask extends BaseTask {
  
  private int id;
  private String name;
  private SimpleDateTime time;
  private String tag = "";              //Set empty tag 
  private String priority = "medium";   //Set default priority 

  public SimpleTask(int id, String name, SimpleDateTime time){
    this.id = id;
    this.name = name;
    this.time = time;
  }

  @Override
  public int getId() {
    return this.id;
  }
  @Override
  public String getName() {
    return this.name;
  }
  @Override
  public BaseDateTime getTime() {
    return this.time;
  }
  @Override
  public String getTag() {
    return this.tag;
  }
  @Override
  public String getPriority() {
    return this.priority;
  }

}
