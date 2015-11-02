//@@author A0124745E
package raijin.logic.parser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import raijin.common.exception.FailedToParseException;

public class SearchParserTest {

  private SimpleParser parser;
  private ParsedInput searchCommand;
  
  @Before
  public void init() {
    parser = new SimpleParser();
  }
  
  @Test
  public void testBasicSearch() throws FailedToParseException {
    searchCommand = parser.parse("search everything including 1 is included 4");
    assertEquals(1, searchCommand.getIds().pollFirst().intValue());
    assertEquals(4, searchCommand.getIds().pollFirst().intValue());
    assertEquals("everything including 1 is included 4", searchCommand.getName());
  }
  
  @Test
  public void testSearchWithTag() throws FailedToParseException {
    searchCommand = parser.parse("search everything including #work");
    assertEquals("everything including", searchCommand.getName());
    assertEquals("work", searchCommand.getTags().pollFirst());
  }
  
  @Test
  public void testSearchWithPriority() throws FailedToParseException {
    searchCommand = parser.parse("search everything that is !h");
    assertEquals("everything that is", searchCommand.getName());
    assertEquals("h", searchCommand.getPriority());
  }
  
  @Test
  public void testAdvancedSearch() throws FailedToParseException {
    searchCommand = parser.parse("search everything including 1 except !l and #school is included ");
    assertEquals(1, searchCommand.getIds().pollFirst().intValue());
    assertEquals("everything including 1 except and is included", searchCommand.getName());
    assertEquals("school", searchCommand.getTags().pollFirst());
    assertEquals("l", searchCommand.getPriority());
    
  }
}
