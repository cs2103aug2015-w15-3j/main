/**
 * Class that handles EDIT parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class EditParser {
  
  private int id;
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  public EditParser(ParsedInput.ParsedInputBuilder builder, String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    this.builder = builder;
  }
  
  /**
   * Method that parses the input for any modification users want to make for a specific task.
   * Able to modify name, date, or time.
   * 
   * @return    ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    try {
      //TODO EDIT'S OWN ALGO
      id = Integer.parseInt(wordsOfInput[1]);
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException("Invalid task number format.",
                                                Constants.CommandParam.ID);
    }
    
    // Deletes taskID from wordsOfInput and makes use of parseAddTask() method.
    String[] newWordsOfInput = new String[wordsOfInput.length-1];
    newWordsOfInput[0] = "edit";
    for (int i = 1; i < newWordsOfInput.length; i++) {
      newWordsOfInput[i] = wordsOfInput[i+1];
    }
    wordsOfInput = newWordsOfInput;
    return new AddParser(builder, wordsOfInput).process().id(id);
  }
  
}
