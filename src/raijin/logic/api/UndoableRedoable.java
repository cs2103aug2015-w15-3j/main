package raijin.logic.api;

/**
 * 
 * @author papa
 * Indicates that a command supports undo and redo
 */
public interface UndoableRedoable {
  
  public void undo();
  public void redo();

}
