package raijin.logic.api;

import raijin.common.datatypes.Constants;
import raijin.logic.command.AddCommandRunner;
import raijin.logic.command.DeleteCommandRunner;
import raijin.logic.command.DisplayCommandRunner;
import raijin.logic.command.DoneCommandRunner;
import raijin.logic.command.EditCommandRunner;
import raijin.logic.command.ExitCommandRunner;
import raijin.logic.command.RedoCommandRunner;
import raijin.logic.command.UndoCommandRunner;
import raijin.logic.command.RedoCommandRunner;
import raijin.logic.command.HelpCommandRunner;

/**
 * 
 * @author papa
 * Factory to create CommandRunner objects based on available commands
 */
public class CommandRunnerFactory {

  private CommandRunnerFactory() {}

  public static CommandRunner getCommandRunner(Constants.Command cmd) {
    switch (cmd) {

      case ADD:
        return new AddCommandRunner();

      case EDIT:
        return new EditCommandRunner();

      case DELETE:
        return new DeleteCommandRunner();

      case DISPLAY:
        return new DisplayCommandRunner();

      case UNDO:
        return new UndoCommandRunner();
        
      case REDO:
        return new RedoCommandRunner();

      case EXIT:
        return new ExitCommandRunner();

      case DONE:
        return new DoneCommandRunner();
        
      case HELP:
        return new HelpCommandRunner();


      default:
        throw new IllegalArgumentException();
    }
  }

}
