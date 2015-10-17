package raijin.logic.command;

import javafx.scene.input.KeyEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class RedoCommandRunner extends CommandRunner {

  public RedoCommandRunner() {
    handleKeyEvent();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    history.redo();
    return new Status(Constants.FEEDBACK_REDO_SUCCESS);
  }

  void handleKeyEvent() {
    EventBus eventbus = RaijinEventBus.getEventBus();           //Get universal eventbus
    MainSubscriber<KeyEvent> redoKeySubscriber = new MainSubscriber<KeyEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyEvent event) {
        if (Constants.KEY_REDO.match(event)) {
          try {
            history.redo();
          } catch (UnableToExecuteCommandException e) {
            logger.error(e.getMessage());
          }
        }
      }};
  }
}
