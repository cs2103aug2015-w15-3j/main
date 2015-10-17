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
import raijin.logic.api.CommandShortcut;
import raijin.logic.parser.ParsedInput;

public class UndoCommandRunner extends CommandRunner implements CommandShortcut {

  public UndoCommandRunner() {
    handleKeyEvent();
  }

  public Status processCommand(ParsedInput cmd) throws UnableToExecuteCommandException {
    history.undo();
    return new Status(Constants.FEEDBACK_UNDO_SUCCESS);
  }

  public void handleKeyEvent() {
    MainSubscriber<KeyEvent> undoKeySubscriber = new MainSubscriber<KeyEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyEvent event) {
        if (Constants.KEY_UNDO.match(event)) {
          try {
            history.undo();
          } catch (UnableToExecuteCommandException e) {
            logger.error(e.getMessage());
          }
        }
      }};
  }
}
