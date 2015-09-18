package raijin.storage.api;

import java.util.Stack;

import raijin.logic.api.CommandRunner;

public class History {

  private static History history = new History();
  private Stack<CommandRunner> undoStack;   //Stores commandRunner created via user input
  private Stack<CommandRunner> redoStack;   //Stores commandRunner undo(ed) via user input
  
  private History() {
    undoStack = new Stack<CommandRunner>();
    redoStack = new Stack<CommandRunner>();
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
  public void clear() {
    undoStack.clear();
    redoStack.clear();
  }

  public void addCommand(CommandRunner commandRunner) {
    undoStack.push(commandRunner);
  }

  /*Calling class must catch EmptyStackException*/
  public void undo() {
    CommandRunner undoCommand = undoStack.pop();
    redoStack.add(undoCommand);     //Add removed command to redo stack
  }

  /*Calling class must catch EmptyStackException*/
  public void redo() {
    CommandRunner redoCommand = redoStack.pop();
    undoStack.add(redoCommand);     //Add command to redo stack
  }
  
}
