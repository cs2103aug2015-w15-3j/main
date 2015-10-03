package raijin.storage.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.EmptyStackException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import raijin.common.exception.NonExistentTaskException;
import raijin.logic.command.AddCommandRunner;


public class HistoryTest {

  private static History history;
  private static AddCommandRunner addCommandRunner;

  @Rule public TemporaryFolder programDirectory = new TemporaryFolder();

  @BeforeClass
  public static void setUpClass() throws Exception {
    history = History.getHistory();
    addCommandRunner = mock(AddCommandRunner.class);
  }

  @Before
  public void setUp() throws IOException {
    programDirectory.newFolder("data");
    Session.getSession().setupBase(programDirectory.getRoot().getAbsolutePath());
    Session.getSession().setupStorage();
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
    history.pushCommand(addCommandRunner);
    assertFalse(history.isEmptyUndoStack());
  }
  
  @Test
  public void undo_VerifyAddedToRedoStack() throws NonExistentTaskException {
    //Ensures start from clean slate
    history.clear();
    history.pushCommand(addCommandRunner);
    history.undo();
    assertFalse(history.isEmptyRedoStack());
  }

  @Test
  public void redo_VerifyAddedToUndoStack() throws NonExistentTaskException {
    //Ensures start from clean slate
    history.clear();
    history.pushCommand(addCommandRunner);
    history.undo();
    history.redo();
    assertFalse(history.isEmptyUndoStack());
  }

  @Test(expected=EmptyStackException.class)
  public void undo_NoCommandsInStack_ThrowException() throws NonExistentTaskException {
    history.clear();
    history.undo();
  }

  @Test(expected=EmptyStackException.class)
  public void redo_NoCommandsInStack_ThrowException() {
    history.clear();
    history.redo();
  }

}
