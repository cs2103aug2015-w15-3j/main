package raijin.common.datatypes;

import java.time.LocalTime;
import java.time.LocalDate;


public abstract class BaseDateTime {

  public abstract LocalDate getStartDate();
  public abstract LocalDate getEndDate();
  public abstract LocalTime getStartTime();
  public abstract LocalTime getEndTime();
  
}
