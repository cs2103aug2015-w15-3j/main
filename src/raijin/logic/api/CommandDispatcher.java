package raijin.logic.api;

import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.parser.Command;

public class CommandDispatcher {
  private HashMap<Constants.Command, CommandRunner> lib = new HashMap<Constants.Command, CommandRunner>();
  private static CommandDispatcher commandDispatcher = new CommandDispatcher();
  
  private CommandDispatcher(){
    setupCommandUnit();     //Setup all supported commands
  }
  
  public static CommandDispatcher getDispatcher(){
    return commandDispatcher;
  }
  
  public Status handleCommand(Command command){
      CommandRunner commandRunner = lib.get(command);
      return commandRunner.execute(command);
  }
  
  public void setupCommandUnit(){
    /*Setup each command runner based on commands*/
    for (Constants.Command cmd : Constants.Command.values()) {
      lib.put(cmd, CommandRunnerFactory.getCommandRunner(cmd));
    }
  }
}
