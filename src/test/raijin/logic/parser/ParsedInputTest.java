//@@author A0112213E
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
  
  //@@author A0124745E
  @Test
  public void parseAddCommandInSimpleParser() throws FailedToParseException {
    ParsedInput editCommand = parser.parse("+ full test from 1/1 800 to 12/5 000");
    assertEquals("full test", editCommand.getName());
    assertEquals("2016-01-01", editCommand.getDateTime().getStartDate().toString());
    assertEquals("08:00", editCommand.getDateTime().getStartTime().toString());
    assertEquals("2016-05-12", editCommand.getDateTime().getEndDate().toString());
    assertEquals("00:00", editCommand.getDateTime().getEndTime().toString());
  }
  
  @Test
  public void parseEditCommandInSimpleParser() throws FailedToParseException {
    ParsedInput editCommand = parser.parse("change 1 full test from 1/1 800 to 12/5 000");
    assertEquals(1, editCommand.getId());
    assertEquals("full test", editCommand.getName());
    assertEquals("2016-01-01", editCommand.getDateTime().getStartDate().toString());
    assertEquals("08:00", editCommand.getDateTime().getStartTime().toString());
    assertEquals("2016-05-12", editCommand.getDateTime().getEndDate().toString());
    assertEquals("00:00", editCommand.getDateTime().getEndTime().toString());
  }
  
  @Test
  public void parseDeleteCommandInSimpleParser() throws FailedToParseException {
    ParsedInput deleteCommand = parser.parse("del 12");
    assertEquals(12, deleteCommand.getId());
    
    deleteCommand = parser.parse("delete 1 work 2 school 3 ");
    assertEquals(1, deleteCommand.getIds().pollFirst().intValue());
    assertEquals(2, deleteCommand.getIds().pollFirst().intValue());
    assertEquals(3, deleteCommand.getIds().pollFirst().intValue());
    assertEquals("school", deleteCommand.getTags().pollFirst());
    assertEquals("work", deleteCommand.getTags().pollFirst());
  }
  
  @Test
  public void parseDoneCommandInSimpleParser() throws FailedToParseException {
    ParsedInput doneCommand = parser.parse("finish 28");
    assertEquals(28, doneCommand.getId());
    
    doneCommand = parser.parse("done 4 work 5 school 6");
    assertEquals(4, doneCommand.getIds().pollFirst().intValue());
    assertEquals(5, doneCommand.getIds().pollFirst().intValue());
    assertEquals(6, doneCommand.getIds().pollFirst().intValue());
    assertEquals("school", doneCommand.getTags().pollFirst());
    assertEquals("work", doneCommand.getTags().pollFirst());
  }
  
  @Test
  public void parseDeleteIDRange() throws FailedToParseException {
    ParsedInput deleteCommand = parser.parse("del 1-5 1-");
    assertEquals(1, deleteCommand.getIds().pollFirst().intValue());
    assertEquals(2, deleteCommand.getIds().pollFirst().intValue());
    assertEquals(3, deleteCommand.getIds().pollFirst().intValue());
    assertEquals("1-", deleteCommand.getTags().pollFirst());
  }
  
  @Test
  public void parseDoneIDRange() throws FailedToParseException {
    ParsedInput doneCommand = parser.parse("finish 5-1 a-a");
    assertEquals(1, doneCommand.getIds().pollFirst().intValue());
    assertEquals(2, doneCommand.getIds().pollFirst().intValue());
    assertEquals(3, doneCommand.getIds().pollFirst().intValue());
    assertEquals("a-a", doneCommand.getTags().pollFirst());
  }

  @Test
  public void getPriority_ReturnPriority() {
    ParsedInput input = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name("I am cute").createParsedInput();
    assertEquals(null, input.getPriority());
  }
  
  @Test(expected=FailedToParseException.class)
  public void testInvalidFilePathInput() throws FailedToParseException {
    parser.parse("set");
  }
  
  @Test(expected=FailedToParseException.class)
  public void testInvalidDeleteInput() throws FailedToParseException {
    parser.parse("delete");
  }
  
  @Test(expected=FailedToParseException.class)
  public void testInvalidDoneInput() throws FailedToParseException {
    parser.parse("done");
  }
  
  @Test(expected=FailedToParseException.class)
  public void testInvalidSearchInput() throws FailedToParseException {
    parser.parse("search");
  }
  
}
