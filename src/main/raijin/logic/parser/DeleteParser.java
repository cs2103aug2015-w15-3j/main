/**
 * Class that handles DELETE parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class DeleteParser {
  
  private String[] wordsOfInput;
  private int id;
  
  public DeleteParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
  }
  
  /**
   * Method that deletes a task based on the taskID input by user.
   * 
   * @return    ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    try {
      id = Integer.parseInt(wordsOfInput[1]);
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.", 
                                                Constants.CommandParam.ID);
    }
    
    return new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE).id(id);
  }
}
