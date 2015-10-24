package raijin.logic.command;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Constants;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.RaijinEventBus;


public class HelpCommandRunner extends CommandRunner {
  private EventBus eventBus = RaijinEventBus.getEventBus();
  
  public Status processCommand(ParsedInput cmd) {
	  String message = "Help!";

    eventBus.post(new SetCurrentDisplayEvent(Constants.HELP_MESSAGE, message));
	
    return new Status(Constants.FEEDBACK_INFO_SUCCESS);
  }

}