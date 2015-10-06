package raijin.storage.api;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.exception.NoSuchTaskException;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.EventBus;
import raijin.logic.api.CommandRunner;
import raijin.logic.api.UndoableRedoable;

public class History {

  private static History history = new History();
  private TasksManager tasksManager;
  private Stack<UndoableRedoable> undoStack;   //Stores commandRunner created via user input
  private Stack<UndoableRedoable> redoStack;   //Stores commandRunner undo(ed) via user input
  
  private History() {
    undoStack = new Stack<UndoableRedoable>();
    redoStack = new Stack<UndoableRedoable>();
    tasksManager = TasksManager.getManager();
  }
  
  /*Helper to write changes to file and trigger view change*/
  void reflectChanges() {
    EventBus.getEventBus().setCurrentTasks(new ArrayList<Task>(
        tasksManager.getPendingTasks().values()));
    Session.getSession().commit();
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

  public void pushCommand(UndoableRedoable commandRunner) {
    undoStack.push(commandRunner);
    reflectChanges();
  }

  /*Calling class must catch EmptyStackException*/
  public void undo() throws UnableToExecuteCommandException {
    try {
      UndoableRedoable undoCommand = undoStack.pop();
      undoCommand.undo();
      reflectChanges();
      redoStack.add(undoCommand); // Add removed command to redo stack
    } catch(EmptyStackException e) {
      throw new UnableToExecuteCommandException("Nothing to undo", Constants.Command.UNDO, e);
    }
  }

  /*Calling class must catch EmptyStackException*/
  public void redo() throws UnableToExecuteCommandException {
    try {
      UndoableRedoable redoCommand = redoStack.pop();
      redoCommand.redo();
      reflectChanges();
      undoStack.add(redoCommand); // Add command to redo stack
    } catch(EmptyStackException e) {
      throw new UnableToExecuteCommandException("Nothing to redo", Constants.Command.REDO, e);
    }
  }
  
}
