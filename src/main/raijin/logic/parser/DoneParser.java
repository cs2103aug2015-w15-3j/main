/**
 * Class that handles DONE parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class DoneParser {
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  private TreeSet<String> tags; 
  
  public DoneParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DONE);
    tags = new TreeSet<String>();
  }
  
  /**
   * Method that marks a task as done based on taskID input by user.
   * 
   * @returns   ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
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
