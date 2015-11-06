//@@author A0112213E

package raijin.logic.command;

import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.UndoRedoEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.CommandShortcut;
import raijin.logic.parser.ParsedInput;

/**
 * Executes redo command of a command runner
 * @author papa
 *
 */
public class RedoCommandRunner extends CommandRunner implements CommandShortcut {

  public RedoCommandRunner() {
    handleKeyEvent();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    history.redo();
    return new Status(Constants.FEEDBACK_REDO_SUCCESS);
  }

  /*Update feedback bar*/
  void sendFeedbackEvent(String msg) {
    eventbus.post(new SetFeedbackEvent(msg));
  }

  /*Handles shortcut to call redo*/
  public void handleKeyEvent() {
    MainSubscriber<UndoRedoEvent> redoKeySubscriber = new MainSubscriber<
        UndoRedoEvent>(eventbus.getEventBus()) {

      @Subscribe
      @Override
      public void handleEvent(UndoRedoEvent event) {
        if (event.canRedo) {
          try {
            history.redo();
            sendFeedbackEvent(Constants.FEEDBACK_REDO_SUCCESS);
          } catch (UnableToExecuteCommandException e) {
            logger.error(e.getMessage());
          }
        }
      }};
  }

}
