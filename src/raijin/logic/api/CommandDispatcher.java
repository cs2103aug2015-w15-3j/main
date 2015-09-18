package raijin.logic.api;

import java.util.HashMap;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.parser.Command;

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
  
  public Status handleCommand(Command command){
      CommandRunner commandRunner = commandRunners.get(command);
      return commandRunner.execute(command);
  }
  
  public void setupCommandUnit(){
    /*Setup each command runner based on commands*/
    for (Constants.Command cmd : Constants.Command.values()) {
      commandRunners.put(cmd, CommandRunnerFactory.getCommandRunner(cmd));
    }
  }
  
  public int getSizeOfCommandRunners() {
    return commandRunners.size();
  }

}
