package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class EditParserTest {

  private EditParser editParser;
  private ParsedInput.ParsedInputBuilder builder;
  
  @Before
  public void setUp() {
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT);
    String[] wordsOfInput = new String("edit something").split(" ");
    editParser = new EditParser(builder, wordsOfInput);
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testInvalidTaskNumberInput() throws IllegalCommandArgumentException {
    editParser.process();
  }
}
