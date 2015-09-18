package raijin.storage.api;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HistoryTest {

  private static History history;

  @BeforeClass
  public static void setUp() throws Exception {
    history = History.getHistory();
  }

  @Test
  public void isEmptyUndoStack_EmptyStack_ReturnFalse() {
    assertTrue(history.isEmptyUndoStack());
  }

  @Test
  public void isEmptyRedoStack_EmptyStack_ReturnFalse() {
    assertTrue(history.isEmptyRedoStack());
  }
}
