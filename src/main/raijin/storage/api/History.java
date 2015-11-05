//@@author A0112213E

package raijin.storage.api;

import java.util.EmptyStackException;
import java.util.Stack;

import org.slf4j.Logger;

import raijin.common.datatypes.Constants;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.RaijinLogger;
import raijin.common.utils.TaskUtils;
import raijin.logic.api.UndoableRedoable;

public class History {

  private RaijinEventBus eventbus;
  private static History history = new History();
  private TasksManager tasksManager;
  private Stack<UndoableRedoable> undoStack;   
  private Stack<UndoableRedoable> redoStack;   
  private Logger logger;
  
  private History() {
    undoStack = new Stack<UndoableRedoable>();
    redoStack = new Stack<UndoableRedoable>();
    tasksManager = TasksManager.getManager();
    logger = RaijinLogger.getLogger();
    eventbus = RaijinEventBus.getInstance();
  }
  
  /*Helper to write changes to file and trigger view change*/
  void reflectChanges() {
    eventbus.post(new SetCurrentDisplayEvent(TaskUtils
        .getTasksList(tasksManager.getPendingTasks()), "All pending tasks"));
    //Notify tasks changed
    eventbus.post(new TasksChangedEvent());
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
  public void clear() {
    undoStack.clear();
    redoStack.clear();
  }

  /*Add a command runner to undo stack*/
  public void pushCommand(UndoableRedoable commandRunner) {
    logger.info("{} is added to undo stack", commandRunner.getClass());
    undoStack.push(commandRunner);
    reflectChanges();
  }

  /**
   * Invoke undo of a command and update view
   * @throws UnableToExecuteCommandException
   */
  public void undo() throws UnableToExecuteCommandException {
    try {
      UndoableRedoable undoCommand = undoStack.pop();
      undoCommand.undo();
      reflectChanges();
      redoStack.add(undoCommand); // Add removed command to redo stack
    } catch(EmptyStackException e) {
      throw new UnableToExecuteCommandException("Nothing to undo", 
          Constants.Command.UNDO, e);
    }
  }

  /**
   * Invoke redo of a command and update view
   * @throws UnableToExecuteCommandException
   */
  public void redo() throws UnableToExecuteCommandException {
    try {
      UndoableRedoable redoCommand = redoStack.pop();
      redoCommand.redo();
      reflectChanges();
      undoStack.add(redoCommand); // Add command to redo stack
    } catch(EmptyStackException e) {
      throw new UnableToExecuteCommandException("Nothing to redo", 
          Constants.Command.REDO, e);
    }
  }
  
}
