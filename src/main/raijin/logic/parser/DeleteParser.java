/**
 * Class that handles DELETE parsing.
 * @author LingJie
 */
//@@author A0124745E
package raijin.logic.parser;

import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class DeleteParser {
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  private TreeSet<String> tags; 
  
  public DeleteParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE);
    tags = new TreeSet<String>();
  }
  
  /**
   * Method that deletes a task based on the taskID input by user.
   * 
   * @return    ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    if (wordsOfInput.length < 2) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_TASK_ID,
          Constants.CommandParam.ID);
    }
    
    for (int i = 1; i < wordsOfInput.length; i++) {
      try {
        builder.id(Integer.parseInt(wordsOfInput[i]));
      } catch (NumberFormatException e) {
        if (wordsOfInput[i].contains("-")) {
          processIdRange(i);
        } else {
          tags.add(wordsOfInput[i]);
        }
      } 
    }
    
    return builder.tag(tags);
  }

  /**
   * Attempts to process deleting of tasks over a range of IDs. If it fails, treat it as a tag
   * instead.
   * 
   * @param i       Index of the string input array.
   */
  public void processIdRange(int i) {
    String[] idRange = wordsOfInput[i].split("-");
    try {
      int start = Integer.parseInt(idRange[0]);
      int end = Integer.parseInt(idRange[1]);
      
      // In case start > end, we still allow processing.
      if (start > end) {
        int temp = start;
        start = end;
        end = temp;
      }
      
      for (int j = start; j < end; j++) {
        builder.id(j);
      }
    } catch(NumberFormatException e1) {
      tags.add(wordsOfInput[i]);
    } catch (ArrayIndexOutOfBoundsException e2) {
      tags.add(wordsOfInput[i]);
    }
  }
}
