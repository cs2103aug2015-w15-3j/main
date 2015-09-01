package raijin.util.Task;

import raijin.util.Time.BaseDateTime;

public abstract class BaseTask {

  private int id;
  private String name;
  private BaseDateTime time;
  private String tag;
  private String priority;
  
  public abstract int getId();
  public abstract String getName();
  public abstract BaseDateTime getTime();
  public abstract String getTag();
  public abstract String getPriority();
}
