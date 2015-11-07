//@@A0112213E

package raijin.logic.realtime;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class SetTrieTest {

  private static ArrayList<String> names;
  private static SetTrie nameList;

  @BeforeClass
  public static void setUpClass() {
    names = new ArrayList<String>();
    names.add("attend meeting with Tom");
    names.add("attend meeting at Limbo");
    names.add("lunch with Cruise");
    names.add("lunch with myself");
    nameList = new SetTrie();
    nameList.addAll(names);
  }

  @Test
  public void contains_NotMatchedPrefix() {
    boolean result = nameList.contains("dinner");
    assertFalse(result);
  }

  @Test
  public void getSuggestions_TwoMatched() {
    String input = "lunch with";
    List<String> result = nameList.getSuggestions(input);
    assertEquals(2, result.size());
  }
  

}
