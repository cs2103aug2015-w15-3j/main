package raijin.storage.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.EmptyStackException;

import org.junit.BeforeClass;
import org.junit.Test;

import raijin.logic.command.AddCommandRunner;


public class HistoryTest {

  private static History history;
  private static AddCommandRunner addCommandRunner;

  @BeforeClass
  public static void setUp() throws Exception {
    history = History.getHistory();
    addCommandRunner = mock(AddCommandRunner.class);
  }

  @Test
  public void isEmptyUndoStack_EmptyStack_ReturnFalse() {
    history.clear();
    assertTrue(history.isEmptyUndoStack());
  }

  @Test
  public void isEmptyRedoStack_EmptyStack_ReturnFalse() {
    history.clear();
    assertTrue(history.isEmptyRedoStack());
  }
  
  @Test
  public void addCommand_ValidCommand_VerifyAdded() {
    //Ensures start from clean slate
    history.clear();
    history.addCommand(addCommandRunner);
    assertFalse(history.isEmptyUndoStack());
  }
  
  @Test
  public void undo_VerifyAddedToRedoStack() {
    //Ensures start from clean slate
    history.clear();
    history.addCommand(addCommandRunner);
    history.undo();
    assertFalse(history.isEmptyRedoStack());
  }

  @Test
  public void redo_VerifyAddedToUndoStack() {
    //Ensures start from clean slate
    history.clear();
    history.addCommand(addCommandRunner);
    history.undo();
    history.redo();
    assertFalse(history.isEmptyUndoStack());
  }

  @Test(expected=EmptyStackException.class)
  public void undo_NoCommandsInStack_ThrowException() {
    history.clear();
    history.undo();
  }

  @Test(expected=EmptyStackException.class)
  public void redo_NoCommandsInStack_ThrowException() {
    history.clear();
    history.redo();
  }

}
