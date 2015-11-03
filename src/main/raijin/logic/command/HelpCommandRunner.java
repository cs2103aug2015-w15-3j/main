//@@author A0129650E

package raijin.logic.command;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Status;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.common.datatypes.Constants;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.RaijinEventBus;

import javafx.stage.*;


public class HelpCommandRunner extends CommandRunner {
 
  public Status processCommand(ParsedInput cmd) {
	
    return new Status(Constants.FEEDBACK_HELP_COMMAND);
  }

}