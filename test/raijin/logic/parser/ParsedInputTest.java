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

}
