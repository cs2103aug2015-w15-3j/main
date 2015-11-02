//@@author A0112213E

package raijin.logic.api;

import raijin.common.exception.UnableToExecuteCommandException;

/**
 * 
 * @author papa
 * Indicates that a command supports undo and redo
 */
public interface UndoableRedoable {
  
  public void undo() throws UnableToExecuteCommandException;
  public void redo() throws UnableToExecuteCommandException;

}
