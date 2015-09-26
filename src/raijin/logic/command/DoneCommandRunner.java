package raijin.logic.command;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.Memory;

public class DoneCommandRunner implements CommandRunner {
	Memory memory = Memory.getMemory();
	int id;
	String taskDescription;
	
  public Status execute(ParsedInput input) {   
	this.id = input.getId();
    Task task = memory.getTask(id);
    taskDescription = task.getName();
    
	memory.addCompletedTask(task);
    
    return new Status("Nicely done! You have completed the task - " 
    					+ taskDescription + 
    					" Give yourself a pat on the back!");
  }
}
