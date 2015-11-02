//@@author A0124745E

package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class SetParser {

  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  public SetParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.SET);
  }
  
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    try {
      return builder.helperOption(wordsOfInput[1]);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_FILEPATH,
          Constants.CommandParam.HELPEROPTION);
    }
  }
}
