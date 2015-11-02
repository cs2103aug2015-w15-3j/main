//@@author A0112213E

package raijin.common.eventbus.events;

public class UndoRedoEvent {

  public boolean canUndo = false;
  public boolean canRedo = false;
  
  public UndoRedoEvent(boolean canUndo, boolean canRedo) {
    this.canUndo = canUndo;
    this.canRedo = canRedo;
  }
  
  
}
