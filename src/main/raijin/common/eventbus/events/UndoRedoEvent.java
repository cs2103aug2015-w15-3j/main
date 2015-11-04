//@@author A0112213E

package raijin.common.eventbus.events;

/**
 * Trigger undo and redo command when shortcut is pressed
 * @author papa
 *
 */
public class UndoRedoEvent {

  public boolean canUndo = false;
  public boolean canRedo = false;
  
  public UndoRedoEvent(boolean canUndo, boolean canRedo) {
    this.canUndo = canUndo;
    this.canRedo = canRedo;
  }
  
  
}
