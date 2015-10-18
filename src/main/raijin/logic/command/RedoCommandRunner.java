package raijin.logic.command;

import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.CommandShortcut;
import raijin.logic.parser.ParsedInput;

public class RedoCommandRunner extends CommandRunner implements CommandShortcut {

  public RedoCommandRunner() {
    handleKeyEvent();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    history.redo();
    return new Status(Constants.FEEDBACK_REDO_SUCCESS);
  }

  public void handleKeyEvent() {
    MainSubscriber<KeyPressEvent> redoKeySubscriber = new MainSubscriber<
        KeyPressEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyPressEvent event) {
        if (Constants.KEY_REDO.match(event.keyEvent)) {
          try {
            history.redo();
            sendFeedbackEvent(Constants.FEEDBACK_REDO_SUCCESS);
          } catch (UnableToExecuteCommandException e) {
            logger.error(e.getMessage());
          }
        }
      }};
  }

  void sendFeedbackEvent(String msg) {
    eventbus.post(new SetFeedbackEvent(msg));
  }
}
