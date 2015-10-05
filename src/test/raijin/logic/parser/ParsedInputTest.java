package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.IllegalCommandArgumentException;

public class ParsedInputTest {

  private SimpleParser parser;
  
  @Before
  public void setUp() throws Exception {
    parser = new SimpleParser();
  }

  @Test
  public void parseAddCommand_ReturnParsedInput() {
    //Creating parsedInput for add using builder
    ParsedInput addCommand = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name("submit op1").dateTime(new DateTime("19/12/1993")).createParsedInput();

    assertEquals("submit op1", addCommand.getName());
  }
  
  @Test
  public void parseAddCommandInSimpleParser_WithStandardDate() throws FailedToParseException {
    ParsedInput addCommand = parser.parse("add finish parser by 26/12/2015");
    assertEquals("finish parser", addCommand.getName());
    assertEquals("2015-12-26", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void parseAddCommandInSimpleParser_WithInformalDate() throws FailedToParseException {
    ParsedInput addCommand = parser.parse("add finish something by 27/12");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    
    addCommand = parser.parse("add finish something by 27/12/15");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    
    addCommand = parser.parse("add finish something on 27.dec");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    
    addCommand = parser.parse("add finish something BY 27-DEc");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    
    addCommand = parser.parse("add finish something by 1-1");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2016-01-01", addCommand.getDateTime().getStartDate().toString());
    
    addCommand = parser.parse("add attend something from 27.dec 0800 till 900");
    assertEquals("attend something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    assertEquals("08:00", addCommand.getDateTime().getStartTime().toString());
    assertEquals("09:00", addCommand.getDateTime().getEndTime().toString());
  }
  
  @Test
  public void parseEditCommandInSimpleParser() throws FailedToParseException {
    ParsedInput editCommand = parser.parse("edit 12 something");
    assertEquals("something", editCommand.getName());
    
    editCommand = parser.parse("edit 12 by 1/1 1800");
    assertEquals(12, editCommand.getId());
    assertEquals("2016-01-01", editCommand.getDateTime().getStartDate().toString());
    assertEquals("18:00", editCommand.getDateTime().getStartTime().toString());
    
    editCommand = parser.parse("edit 1 full test from 1/1 800 to 12/5 000");
    assertEquals(1, editCommand.getId());
    assertEquals("full test", editCommand.getName());
    assertEquals("2016-01-01", editCommand.getDateTime().getStartDate().toString());
    assertEquals("08:00", editCommand.getDateTime().getStartTime().toString());
    assertEquals("2016-05-12", editCommand.getDateTime().getEndDate().toString());
    assertEquals("00:00", editCommand.getDateTime().getEndTime().toString());
  }
  
  @Test
  public void parseDeleteCommandInSimpleParser() throws FailedToParseException {
    ParsedInput deleteCommand = parser.parse("delete 12");
    assertEquals(12, deleteCommand.getId());
  }
  
  @Test
  public void parseDoneCommandInSimpleParser() throws FailedToParseException {
    ParsedInput doneCommand = parser.parse("done 28");
    assertEquals(28, doneCommand.getId());
  }
  
  @Test
  public void parseDisplayCommandInSimpleParser() throws FailedToParseException {
    ParsedInput displayCommand = parser.parse("display c");
    assertEquals("c", displayCommand.getDisplayOptions());
    
    displayCommand = parser.parse("display c 19/9");
    assertEquals("c", displayCommand.getDisplayOptions());
    assertEquals("2015-09-19", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 1.1");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-01-01", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 30/10");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-10-30", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 30/2");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-02-28", displayCommand.getDateTime().getStartDate().toString());
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartDate() throws IllegalCommandArgumentException {
    parser.checkStartDate("30/02/2015", new DateTime("30/02/2015"));
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartEndDate() throws IllegalCommandArgumentException {
    parser.checkStartEndDate("29/02/2015", "30/02/2015", 
        new DateTime("29/02/2015", "0800", "30/02/2015", "1000"));
  }

}
