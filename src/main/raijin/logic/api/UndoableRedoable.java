package raijin.logic.api;

import raijin.common.exception.NonExistentTaskException;

/**
 * 
 * @author papa
 * Indicates that a command supports undo and redo
 */
public interface UndoableRedoable {
  
  public void undo() throws NonExistentTaskException;
  public void redo() throws NonExistentTaskException;

}
