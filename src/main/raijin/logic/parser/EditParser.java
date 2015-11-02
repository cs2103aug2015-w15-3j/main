/**
 * Class that handles EDIT parsing.
 * @author LingJie
 */
//@@author A0124745E
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
      id = Integer.parseInt(wordsOfInput[1]);
    } catch (NumberFormatException e) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_INVALID_ID,
          Constants.CommandParam.ID);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_TASK_ID,
          Constants.CommandParam.ID);
    }
    
    // Deletes taskID from wordsOfInput and makes use of parseAddTask() method.
    extractID();
    return new AddParser(builder, wordsOfInput, 1).process().id(id);
  }

  public void extractID() {
    String[] newWordsOfInput = new String[wordsOfInput.length-1];
    newWordsOfInput[0] = "edit";
    for (int i = 1; i < newWordsOfInput.length; i++) {
      newWordsOfInput[i] = wordsOfInput[i+1];
    }
    wordsOfInput = newWordsOfInput;
  }
  
}
