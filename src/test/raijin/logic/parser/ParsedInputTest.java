package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;

public class ParsedInputTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void parseAddCommand_ReturnParsedInput() {
    //Creating parsedInput for add using builder
    ParsedInput addCommand = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name("submit op1").dateTime(new DateTime("19/12/1993")).createParsedInput();

    assertEquals("submit op1", addCommand.getName());
  }
  
  @Test
  public void parseAddCommandInSimpleParser_WithStandardDate() {
    SimpleParser parser = new SimpleParser();
    ParsedInput addCommand = parser.parse("add finish parser by 26/12/2015");
    assertEquals("finish parser", addCommand.getName());
    assertEquals("2015-12-26", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void parseAddCommandInSimpleParser_WithInformalDate() {
    SimpleParser parser = new SimpleParser();
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
  public void parseEditCommandInSimpleParser() {
    SimpleParser parser = new SimpleParser();
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
  public void parseDeleteCommandInSimpleParser() {
    SimpleParser parser = new SimpleParser();
    ParsedInput deleteCommand = parser.parse("delete 12");
    assertEquals(12, deleteCommand.getId());
  }
  
  @Test
  public void parseDoneCommandInSimpleParser() {
    SimpleParser parser = new SimpleParser();
    ParsedInput doneCommand = parser.parse("done 28");
    assertEquals(28, doneCommand.getId());
  }
  
  @Test
  public void parseDisplayCommandInSimpleParser() {
    SimpleParser parser = new SimpleParser();
    ParsedInput displayCommand = parser.parse("display c");
    assertEquals("c", displayCommand.getDisplayOptions());
    
    displayCommand = parser.parse("display c 19/9");
    assertEquals("c", displayCommand.getDisplayOptions());
    assertEquals("2015-09-19", displayCommand.getDateTime().getStartDate().toString());
    
    displayCommand = parser.parse("display 1.1");
    assertEquals("p", displayCommand.getDisplayOptions());
    assertEquals("2015-01-01", displayCommand.getDateTime().getStartDate().toString());
  }

}
