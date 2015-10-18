/**
 * Class that handles DONE parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class DoneParser {
  
  private String[] wordsOfInput;
  private int id;
  
  public DoneParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
  }
  
  /**
   * Method that marks a task as done based on taskID input by user.
   * 
   * @returns   ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    try {
      id = Integer.parseInt(wordsOfInput[1]);
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.",
                                                Constants.CommandParam.ID);
    }
    
    return new ParsedInput.ParsedInputBuilder(Constants.Command.DONE).id(id);
  }
}
