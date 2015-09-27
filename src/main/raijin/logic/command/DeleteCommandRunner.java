package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.Memory;
import raijin.common.datatypes.Task;

public class DeleteCommandRunner implements CommandRunner, UndoableRedoable {
	Memory memory = Memory.getMemory();
	int id;
	String taskDescription;
	
  public Status execute(ParsedInput input) {   
	this.id = input.getId();
    Task task = memory.getTask(id);
    taskDescription = task.getName();
    
	memory.deleteTask(id);
    
    return new Status("You have just deleted " + taskDescription + "!");
  }

  public void undo() {
    memory.undo();
  }

  public void redo() {
    memory.redo();
  }

}
