package raijin.component.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.util.UserInput;

public class SimpleParserTest {

  private SimpleParser parser;
  private static final String  TEST_STRING = "This is a test";
  private static final String TEST_COMMAND = "add submit OP1 8/9";

  @Before
  public void setUp() throws Exception {
    this.parser = SimpleParser.getParser();
  }

  @Test
  public void compareStringArray() {
    String[] testString = new String[] {"This", 
        "is", "a", "test"};
    assertArrayEquals(parser.getTokens(TEST_STRING), testString);
  }
  
  @Test
  public void checkParsedInput() {
    UserInput test = parser.getParsedInput(TEST_COMMAND);
    assertEquals(test.getCommand(), "add");
    assertArrayEquals(test.getKeywords(),
        new String[]{"submit", "OP1", "8/9"});
  }

}
