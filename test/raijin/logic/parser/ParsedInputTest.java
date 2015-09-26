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
    ParsedInput addCommand = parser.parse("add finish parser by 26/09/2015");
    assertEquals("finish parser", addCommand.getName());
    assertEquals("2015-09-26", addCommand.getDateTime().getStartDate().toString());
  }
  
  @Test
  public void parseAddCommandInSimpleParser_WithInformalDate() {
    SimpleParser parser = new SimpleParser();
    ParsedInput addCommand = parser.parse("add finish something by 27/09"); // CURRENTLY CAN'T WORK WITH FLEXIBLE DATE FORMATS
    assertEquals("finish something", addCommand.getName());
    assertEquals("2015-09-27", addCommand.getDateTime().getStartDate().toString());
  }

}
