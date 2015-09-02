package raijin.component.dispatcher;

import java.util.HashMap;

import raijin.util.Status;
import raijin.util.Task.UserInput;

public class CommandDispatcher {
  private HashMap<String, CommandUnitInterface> lib = new HashMap<String, CommandUnitInterface>();
  private static CommandDispatcher instance = new CommandDispatcher();
  
  private CommandDispatcher(){
    setupCommandUnit();
  }
  
  public static CommandDispatcher getDispatcher(){
    return instance;
  }
  
  public String handleCommand(UserInput in){
    String command = in.getCommand();
    if (lib.containsKey(command)){
      CommandUnitInterface commandUnit = lib.get(command);
      return commandUnit.executeCommand(in);
    } else {
      return Status.ERROR_UNKNOWN_COMMAND;
    }
  }
  
  public void setupCommandUnit(){
    lib.put("add", new AddCommandUnit());
    lib.put("edit", new EditCommandUnit());
    lib.put("delete", new DeleteCommandUnit());
    lib.put("display", new DisplayCommandUnit());
    lib.put("undo", new UndoCommandUnit());
  }
}
