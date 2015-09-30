package raijin.logic.api;

import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.exception.NonExistentTaskException;
import raijin.logic.parser.ParsedInput;

public class CommandDispatcher {
  private HashMap<Constants.Command, CommandRunner> commandRunners;     //Collection of command runners
  private static CommandDispatcher commandDispatcher = new CommandDispatcher();
  
  private CommandDispatcher(){
    commandRunners = new HashMap<Constants.Command, CommandRunner>();
    setupCommandUnit();     //Setup all supported commands
  }
  
  public static CommandDispatcher getDispatcher(){
    return commandDispatcher;
  }
  
  public Status delegateCommand(ParsedInput input) throws NonExistentTaskException{
      CommandRunner commandRunner = commandRunners.get(input.getCommand());
      return commandRunner.execute(input);
  }
  
  void setupCommandUnit(){
    /*Setup each command runner based on commands*/
    for (Constants.Command cmd : Constants.Command.values()) {
      commandRunners.put(cmd, CommandRunnerFactory.getCommandRunner(cmd));
    }
  }

  int getSizeOfCommandRunners() {
    return commandRunners.size();
  }
  
}
