package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class EditParserTest {

  private ParsedInput.ParsedInputBuilder builder;
  private String[] wordsOfInput;
  
  @Before
  public void setUp() {
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT);
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidTaskNumberInput() throws IllegalCommandArgumentException {
    wordsOfInput = new String("edit something").split(" ");
    new EditParser(builder, wordsOfInput).process();
  }
  
  @Test
  public void testBasicEditing() throws IllegalCommandArgumentException {
    wordsOfInput = new String("edit 1 something by 28/12").split(" ");
    ParsedInput parsed = new EditParser(builder, wordsOfInput).process().createParsedInput();
    assertEquals(1, parsed.getId());
    assertEquals("something", parsed.getName());
    assertEquals(1, parsed.getId());
    assertEquals("2015-12-28", parsed.getDateTime().getStartDate().toString());
  }
}
