//@@author A0112213E

package raijin.common.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is a derivative work of Sujit Pal http://sujitpal.blogspot.sg
 * Stores strings in a trie to be used for prefix searching 
 * @author papa
 *
 */
public class SetTrie {

  private TreeSet<String> wordList;

  public SetTrie() {
    wordList = new TreeSet<String>();
  }

  /**
   * Inserts word that can be autocompleted
   * @param word
   */
  public void add(String word) {
    wordList.add(word);
  }

  public void addAll(Collection<String> collection) {
    wordList.addAll(collection);
  }

  /**
   * Checks if a prefix exists in the supported word list
   * @param prefix
   * @return boolean
   */
  public boolean contains(String prefix) {
    Set<String> tailSet = wordList.tailSet(prefix);
    for (String tail : tailSet) {
      if (tail.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Obtain list of words that matched given prefix
   * @param prefix
   * @return matched words
   */
  public List<String> getSuggestions(String prefix) {
    ArrayList<String> suggestions = new ArrayList<String>(); // Stores matched words
    Set<String> tailSet = wordList.tailSet(prefix);

    for (String tail : tailSet) {

      if (tail.startsWith(prefix)) {
        suggestions.add(tail);
      } else {
        break;
      }
    }

    return suggestions;
  }
  
  public TreeSet<String> getWordList() {
    return wordList;
  }
}
