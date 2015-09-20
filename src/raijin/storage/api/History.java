package raijin.storage.api;

import java.util.Stack;

import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;

public class History {

  private static History history = new History();
  private Stack<UndoableRedoable> undoStack;   //Stores commandRunner created via user input
  private Stack<UndoableRedoable> redoStack;   //Stores commandRunner undo(ed) via user input
  
  private History() {
    undoStack = new Stack<UndoableRedoable>();
    redoStack = new Stack<UndoableRedoable>();
  }
  
  public static History getHistory() {
    return history;
  }

  /*Checks if there are no commands executed by users*/
  public boolean isEmptyUndoStack() {
    return undoStack.isEmpty();
  }

  /*Checks if there are no undos executed by users*/
  public boolean isEmptyRedoStack() {
    return redoStack.isEmpty();
  }
  
  /*Clears both undo and redo stacks*/
  void clear() {
    undoStack.clear();
    redoStack.clear();
  }

  void addCommand(UndoableRedoable commandRunner) {
    undoStack.push(commandRunner);
  }

  /*Calling class must catch EmptyStackException*/
  void undo() {
    UndoableRedoable undoCommand = undoStack.pop();
    undoCommand.undo();
    redoStack.add(undoCommand);     //Add removed command to redo stack
  }

  /*Calling class must catch EmptyStackException*/
  void redo() {
    UndoableRedoable redoCommand = redoStack.pop();
    redoCommand.redo();
    undoStack.add(redoCommand);     //Add command to redo stack
  }
  
}
