package raijin.logic.parser;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import raijin.logic.parser.SimpleDateParser;

public class SimpleDateParserTest {

  private static final String TEST_DATE = "15/9/2015";
  private SimpleDateParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new SimpleDateParser();
  }

  @Test
  public void checkLocalDate() throws ParseException {
    System.out.println(parser.getDate(TEST_DATE));
  }

}
