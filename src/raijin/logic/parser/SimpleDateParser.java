package raijin.logic.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class SimpleDateParser implements DateParserInterface{

  @Override
  public Date getDate(String rawInput) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    return formatter.parse(rawInput);
  }

  @Override
  public LocalTime getTime(String rawInput) {
    return LocalTime.parse(rawInput);
  }

}
