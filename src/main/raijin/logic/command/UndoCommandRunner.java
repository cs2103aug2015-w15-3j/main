//@@author A0112213E

package raijin.logic.command;

import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.MainSubscriber;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.UndoRedoEvent;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.CommandShortcut;
import raijin.logic.parser.ParsedInput;

/**
 * Executes undo of a command runner
 * @author papa
 *
 */
public class UndoCommandRunner extends CommandRunner implements CommandShortcut {

  public UndoCommandRunner() {
    handleKeyEvent();
  }

  @Override
  public Status processCommand(ParsedInput cmd) throws UnableToExecuteCommandException {
    history.undo();
    return new Status(Constants.FEEDBACK_UNDO_SUCCESS);
  }

  /*Update feedback bar*/
  void sendFeedbackEvent(String msg) {
    eventbus.post(new SetFeedbackEvent(msg));
  }

  /*handles shortcut to call undo*/
  public void handleKeyEvent() {
    MainSubscriber<UndoRedoEvent> undoKeySubscriber = new MainSubscriber<
        UndoRedoEvent>(eventbus.getEventBus()) {

      @Subscribe
      @Override
      public void handleEvent(UndoRedoEvent event) {
        if (event.canUndo) {
          try {
            Thread.sleep(500);
          } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          try {
            history.undo();
            sendFeedbackEvent(Constants.FEEDBACK_UNDO_SUCCESS);
          } catch (UnableToExecuteCommandException e) {
            logger.error(e.getMessage());
          }
        }
      }};
  }
  
}
