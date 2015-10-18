package raijin.common.datatypes;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SetTrieTest {

  private SetTrie taskList;

  @Before
  public void setUp() throws Exception {
    taskList = new SetTrie();
  }

  @Test
  public void testStringsWithSpace() {
    taskList.add("I am cute");
    taskList.add("I am batu");
    List<String> result = taskList.getSuggestions("I am");
    assertEquals(2, result.size());
  }

}
