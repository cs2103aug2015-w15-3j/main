package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandArgumentException;

public class AddParserTest {

  private AddParser addParser;
  
  @Before
  public void setUp() throws Exception {
    String[] wordsOfInput = new String("").split(" ");
    addParser = new AddParser(new ParsedInput.ParsedInputBuilder(Constants.Command.ADD), wordsOfInput);
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartDate() throws IllegalCommandArgumentException {
    addParser.checkStartDate("30/02/2015", new DateTime("30/02/2015"));
  }
  
  @Test(expected=IllegalCommandArgumentException.class)
  public void testCheckStartEndDate() throws IllegalCommandArgumentException {
    addParser.checkStartEndDate("29/02/2015", "30/02/2015", 
        new DateTime("29/02/2015", "0800", "30/02/2015", "1000"));
  }

}