package raijin.util.Task;

import raijin.util.Time.BaseDateTime;

public abstract class BaseTask {

  public abstract int getId();
  public abstract String getName();
  public abstract BaseDateTime getTime();
  public abstract String getTag();
  public abstract String getPriority();
}
