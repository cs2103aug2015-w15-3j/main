package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.IllegalCommandArgumentException;

public class AddParserTest {

  private SimpleParser parser;
  private AddParser addParser;
  private ParsedInput addCommand;
  private ParsedInput.ParsedInputBuilder builder;
  
  @Before
  public void setUp() throws IllegalCommandArgumentException {
    parser = new SimpleParser();
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD);
    String[] wordsOfInput = new String("add testing").split(" ");
    addParser = new AddParser(builder, wordsOfInput, 0);
  }
  
  @Test
  public void testAddWithStandardDate() throws FailedToParseException {
    addCommand = parser.parse("add finish parser by 26/12/2015");
    assertEquals("finish parser", addCommand.getName());
    assertEquals("2015-12-26", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void testAddWithInformalDate() throws FailedToParseException {
    /* This is a boundary case for the ‘exact value’ partition */
    addCommand = parser.parse("add finish something by 28/2");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2016-02-28", addCommand.getDateTime().getStartDate().toString());

    addCommand = parser.parse("add finish something at 1-1");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2016-01-01", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void testAddWithDifferentDateTimeInputs() throws FailedToParseException{
    addCommand = parser.parse("add finish something by 27/12/15 000");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    assertEquals("00:00", addCommand.getDateTime().getStartTime().toString());
    
    addCommand = parser.parse("add finish something from 27.deC to 28-12");
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    assertEquals("2015-12-28", addCommand.getDateTime().getEndDate().toString());
    
    /* This is a boundary case for the ‘exact value’ partition */
    addCommand = parser.parse("add attend something from 27.dec 000 till 2359");
    assertEquals("attend something", addCommand.getName());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    assertEquals("00:00", addCommand.getDateTime().getStartTime().toString());
    assertEquals("23:59", addCommand.getDateTime().getEndTime().toString());
    
    addCommand = parser.parse("add finish something from 0800 to 0900");
    assertEquals("finish something", addCommand.getName());
    assertEquals("08:00", addCommand.getDateTime().getStartTime().toString());
    assertEquals("09:00", addCommand.getDateTime().getEndTime().toString());
  }
  
  @Test
  public void testAddWithPriorityAndTagsAfterDate() throws FailedToParseException {
    addCommand = parser.parse("add finish work from 0800 to 0900 !h #work #school");
    assertEquals("finish work", addCommand.getName());
    assertEquals("08:00", addCommand.getDateTime().getStartTime().toString());
    assertEquals("09:00", addCommand.getDateTime().getEndTime().toString());
    assertEquals(Constants.PRIORITY_HIGH, addCommand.getPriority());
    assertEquals("school", addCommand.getTags().pollFirst());
    assertEquals("work", addCommand.getTags().pollFirst());
  }
  
  @Test
  public void testAddWithPriorityAndTagsBeforeDate() throws FailedToParseException {
    addCommand = parser.parse("add finish work !h #work #school from 0800 to 0900");
    assertEquals("finish work", addCommand.getName());
    assertEquals("08:00", addCommand.getDateTime().getStartTime().toString());
    assertEquals("09:00", addCommand.getDateTime().getEndTime().toString());
    assertEquals(Constants.PRIORITY_HIGH, addCommand.getPriority());
    assertEquals("school", addCommand.getTags().pollFirst());
    assertEquals("work", addCommand.getTags().pollFirst());
  }
  
  @Test
  public void testAddSubtasking() throws FailedToParseException {
    addCommand = parser.parse("add finish work @3");
    assertEquals("finish work", addCommand.getName());
    assertEquals(3, addCommand.getSubTaskOf());
  }
  
  @Test
  public void testAddWithTagsPriorityAtEnd() throws FailedToParseException {
    addCommand = parser.parse("add finish work by 27/12 !h #work");
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
    assertEquals(Constants.PRIORITY_HIGH, addCommand.getPriority());
    assertEquals("work", addCommand.getTags().pollFirst());
  }
  
  @Test
  public void testBatchAdding() throws FailedToParseException {
    addCommand = parser.parse("add finish work ; do more work ; do somemore!");
    assertEquals("do more work", addCommand.getNames().pollFirst());
    assertEquals("do somemore!", addCommand.getNames().pollFirst());
    assertEquals("finish work", addCommand.getNames().pollFirst());
  }
  
  @Test
  public void testBatchAddWithDates() throws FailedToParseException {
    addCommand = parser.parse("add finish work by 24/12 ; do more work ; do somemore!");
    assertEquals("do more work", addCommand.getNames().pollFirst());
    assertEquals("do somemore!", addCommand.getNames().pollFirst());
    assertEquals("finish work", addCommand.getNames().pollFirst());
    assertEquals("2015-12-24", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void testBatchAddWithDatesAtEnd() throws FailedToParseException {
    addCommand = parser.parse("add finish work ; do more work ; do somemore! by 27/12");
    assertEquals("do more work", addCommand.getNames().pollFirst());
    assertEquals("do somemore!", addCommand.getNames().pollFirst());
    assertEquals("finish work", addCommand.getNames().pollFirst());
    assertEquals("2015-12-27", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testNoNameGivenError() throws IllegalCommandArgumentException {
    new AddParser(builder, new String("add").split(" "), 0).process();
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidSubTaskInput() throws IllegalCommandArgumentException {
    new AddParser(builder, new String("add work @a").split(" "), 0).process();
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidPriorityInput() throws IllegalCommandArgumentException {
    new AddParser(builder, new String("add work !a").split(" "), 0).process();
  }
  
  /* This is a boundary case for the ‘negative value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidStartTimeFormat() throws IllegalCommandArgumentException {
    new AddParser(builder, new String("add finish something by 27-DEC 0").split(" "), 0).process();
  }
  
  /* This is a boundary case for the ‘negative value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidEndDateAfterStartDate() throws IllegalCommandArgumentException {
    String[] wordsOfInput = new String("add finish something from 27-DEC to 0/1").split(" ");
    new AddParser(builder, wordsOfInput, 0).process();
  }
  
  /* This is a boundary case for the ‘positive value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidEndTimeAfterStartTime() throws IllegalCommandArgumentException {
    String[] wordsOfInput = new String("add finish something from 1800 to 2500").split(" ");
    new AddParser(builder, wordsOfInput, 0).process();
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidEndDateAfterStartDateTime() throws IllegalCommandArgumentException {
    String[] wordsOfInput = new String("add finish something from 27-DEC 1800 to 0/1").split(" ");
    new AddParser(builder, wordsOfInput, 0).process();
  }
  
  /* This is a boundary case for the ‘negative value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidEndTimeAfterStartDateTime() throws IllegalCommandArgumentException {
    String[] wordsOfInput = new String("add finish something from 27-DEC 1800 to 00").split(" ");
    new AddParser(builder, wordsOfInput, 0).process();
  }
  
  /* This is a boundary case for the ‘negative value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidEndTimeAfterStartDateTimeEndDate() throws IllegalCommandArgumentException {
    String[] wordsOfInput = new String
                            ("add finish something from 27-DEC 1800 to 28.12 00").split(" ");
    new AddParser(builder, wordsOfInput, 0).process();
  }
  
  /* This is a boundary case for the ‘positive value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartDate() throws IllegalCommandArgumentException {
    addParser.checkStartDate("30/02/2015", new DateTime("30/02/2015"));
  }
  
  /* This is a boundary case for the ‘positive value’ partition */
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartEndDate() throws IllegalCommandArgumentException {
    addParser.checkStartEndDate("29/02/2015", "30/02/2015", 
        new DateTime("29/02/2015", "0800", "30/02/2015", "1000"));
  }

}