package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.exception.FailedToParseException;

public class DisplayParserTest {

  private SimpleParser parser;
  private ParsedInput displayCommand;
  
  @Before
  public void init() {
    parser = new SimpleParser();
  }
  
  @Test
  public void parseDisplayDate() throws FailedToParseException {
    displayCommand = parser.parse("display 1.1");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-01-01", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 30/10");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-10-30", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 30-feb");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-02-28", displayCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void parseDisplayCompleted() throws FailedToParseException {
    displayCommand = parser.parse("display c 19/9");
    assertEquals("c", displayCommand.getDisplayOptions());
    assertEquals("2015-09-19", displayCommand.getDateTime().getStartDate().toString());
    displayCommand = parser.parse("display completed");
    assertEquals("c", displayCommand.getDisplayOptions());
  }

  @Test
  public void parseDisplayAll() throws FailedToParseException {
    displayCommand = parser.parse("display a");
    assertEquals("a", displayCommand.getDisplayOptions());
    displayCommand = parser.parse("display all");
    assertEquals("a", displayCommand.getDisplayOptions());
  }
  
  @Test
  public void parseDisplayPending() throws FailedToParseException {
    displayCommand = parser.parse("display p");
    assertEquals("p", displayCommand.getDisplayOptions());
    displayCommand = parser.parse("display today");
    assertEquals("p", displayCommand.getDisplayOptions());
  }
  
  @Test
  public void parseDisplayFloating() throws FailedToParseException {
    displayCommand = parser.parse("display f");
    assertEquals("f", displayCommand.getDisplayOptions());
    displayCommand = parser.parse("display floating");
    assertEquals("f", displayCommand.getDisplayOptions());
  }
  
  @Test
  public void parseDisplayOverdue() throws FailedToParseException {
    displayCommand = parser.parse("display o");
    assertEquals("o", displayCommand.getDisplayOptions());
    displayCommand = parser.parse("display overdue");
    assertEquals("o", displayCommand.getDisplayOptions());
  }
}
