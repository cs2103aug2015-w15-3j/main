package raijin.logic.parser;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;

public interface DateParserInterface {

  public Date getDate(String rawInput) throws ParseException;
  public LocalTime getTime(String rawInput);
}
