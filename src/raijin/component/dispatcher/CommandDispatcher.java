package raijin.component.dispatcher;

import java.util.HashMap;

import raijin.util.Task.UserInput;

public class CommandDispatcher {
  private HashMap<String, CommandUnit> lib = new HashMap<String, CommandUnit>();
  private CommandDispatcher instance = new CommandDispatcher();
  
  private CommandDispatcher(){}
  
  public CommandDispatcher getDispatcher(){
    return instance;
  }
  
  public void handleCommand(UserInput in){
  }
  
  public void setupCommandUnit(){
    lib.put("add", new AddCommandUnit());
    lib.put("edit", new EditCommandUnit());
    lib.put("delete", new DeleteCommandUnit());
    lib.put("display", new DisplayCommandUnit());
    lib.put("undo", new UndoCommandUnit());
  }
}
