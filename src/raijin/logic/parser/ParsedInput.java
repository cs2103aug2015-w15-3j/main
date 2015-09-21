package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;

/**
 * 
 * @author papa
 */
public class ParsedInput {
  
  private Constants.Command command;
  
  /*Parser will output ParsedInput with user command*/
  public ParsedInput(Constants.Command command) {
    this.command = command;
  }
  
  /*Returns command associated with this parsedInput*/
  public Constants.Command getCommand() {
    return command;
  }
  

}
