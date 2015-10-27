package raijin.logic.command;

import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.UndoRedoEvent;
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
    MainSubscriber<UndoRedoEvent> undoKeySubscriber = new MainSubscriber<
        UndoRedoEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(UndoRedoEvent event) {
        if (event.canUndo) {
          try {
            history.undo();
            sendFeedbackEvent(Constants.FEEDBACK_UNDO_SUCCESS);
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
