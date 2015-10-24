/**
 * Class that handles DELETE parsing.
 * @author LingJie
 */
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
        tags.add(wordsOfInput[i]);
      } 
    }
    
    return builder.tag(tags);
  }
}
