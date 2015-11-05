//@@author A0112213E

package raijin.logic.api;

import raijin.common.exception.UnableToExecuteCommandException;

/**
 * 
 * Indicates that a command supports undo and redo
 * @author papa
 */
public interface UndoableRedoable {
  
  public void undo() throws UnableToExecuteCommandException;
  public void redo() throws UnableToExecuteCommandException;

}
