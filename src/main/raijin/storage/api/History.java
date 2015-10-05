package raijin.storage.api;

import java.util.ArrayList;
import java.util.Stack;

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
    EventBus.getEventBus().setCurrentTasks(new ArrayList<Task>(
        tasksManager.getPendingTasks().values()));
    Session.getSession().commit();  //Write changes to temp file each time an undoable command is called
  }

  /*Calling class must catch EmptyStackException*/
  public void undo() throws UnableToExecuteCommandException {
    UndoableRedoable undoCommand = undoStack.pop();
    undoCommand.undo();
    redoStack.add(undoCommand);     //Add removed command to redo stack
  }

  /*Calling class must catch EmptyStackException*/
  public void redo() throws UnableToExecuteCommandException {
    UndoableRedoable redoCommand = redoStack.pop();
    redoCommand.redo();
    undoStack.add(redoCommand);     //Add command to redo stack
  }
  
}
