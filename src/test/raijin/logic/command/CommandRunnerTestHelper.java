package raijin.logic.command;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.logic.parser.ParsedInput;

public class CommandRunnerTestHelper {
  
  public ParsedInput createAddTask(String inputName, DateTime dateTime) {
    ParsedInput addInput = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
        name(inputName).dateTime(dateTime).createParsedInput();
    return addInput;
  }

  public ParsedInput createDeleteTask(int id) {
    ParsedInput deletedInput = new ParsedInput.ParsedInputBuilder(Constants.Command.DELETE).
        id(id).createParsedInput();
    return deletedInput;
  }
}
