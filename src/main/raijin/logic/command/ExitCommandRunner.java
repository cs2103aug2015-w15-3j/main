package raijin.logic.command;

import java.io.IOException;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class ExitCommandRunner extends CommandRunner {

  public Status processCommand(ParsedInput cmd) {
	  try {
		  session.writeOnExit();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
	  
	  return new Status("Exiting");
  }

}
