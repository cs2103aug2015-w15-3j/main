//@@author A0112213E

package raijin.ui;
  
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

public class RaijinInputIT {

  private static GuiTest raijin;
  private static RaijinEventBus eventbus;

  @BeforeClass
  public static void setUpClass() throws InterruptedException {
    FXTestUtils.launchApp(Raijin.class);
    Thread.sleep(7000);                     //Wait for program to be launched
    raijin = new GuiTest() {

      @Override
      protected Parent getRootNode() {
        return Raijin.getStage().getScene().getRoot();
      }

    };
    
    eventbus = RaijinEventBus.getInstance();
  }

  @Before
  public void setUp() {
    TasksManager.getManager().setPendingTasks(new HashMap<Integer, Task>());
  }

  @After
  public void cleanUp() {
    raijin.release(new KeyCode[] {});
    raijin.release(new MouseButton[] {});
    raijin.push(KeyCode.CONTROL, KeyCode.R);
  }

  @Test
  public void testAddCommandAutoComplete() throws InterruptedException {
    raijin.type(KeyCode.A).push(KeyCode.TAB);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
    assertEquals("add", result.getText());
  }

  @Test
  public void testMaximiseShortcut() throws InterruptedException {
    Platform.runLater(new Runnable() {
      
      @Override
      public void run() {
        Raijin.getStage().setMaximized(false);
      }
    });
    Thread.sleep(500);
    raijin.push(KeyCode.ALT, KeyCode.M);
    Thread.sleep(2000);
    assertTrue(Raijin.getStage().isMaximized());
  }

  @Test
  public void testClearShortcut() throws InterruptedException {
    String testInput = "Write for fun";
    raijin.type(testInput);
    raijin.push(KeyCode.CONTROL, KeyCode.R);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
    assertEquals("", result.getText());
  }
  
  @Test
  public void testGetPreviousCommand() throws InterruptedException {
    String testInput = "Write for fun";
    raijin.type(testInput).press(KeyCode.ENTER);
    raijin.type("Does not matter").press(KeyCode.UP);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
    assertEquals(testInput, result.getText());
  }

  @Test
  public void testSetFeedbackEvent() throws InterruptedException {
    String testInput = "This is a normal feedback";
    eventbus.post(new SetFeedbackEvent(testInput));
    Label result = (Label) GuiTest.find("#feedbackBar");

    assertEquals(testInput, result.getText());
  }

  @Test
  public void testHelpBar() {
    raijin.type("edit 20");
    TextFlow result = (TextFlow) GuiTest.find("#helpBar");
    assertTrue(!result.getChildren().isEmpty());
  }

  @Test
  public void testUndoShortcut() {
    raijin.type("add floating boy").push(KeyCode.ENTER);
    raijin.push(KeyCode.CONTROL, KeyCode.Z);
    Label result = (Label) GuiTest.find("#feedbackBar");
    assertEquals(Constants.FEEDBACK_UNDO_SUCCESS, result.getText());
  }

  @Test
  public void testRedoShortcut() {
    raijin.type("add meet at Limbo").push(KeyCode.ENTER);
    raijin.push(KeyCode.CONTROL, KeyCode.Z);
    raijin.push(KeyCode.CONTROL, KeyCode.Y);
    Label result = (Label) GuiTest.find("#feedbackBar");
    assertEquals(Constants.FEEDBACK_REDO_SUCCESS, result.getText());
  }

  @Test
  //@@author A0129650E
  public void testHelpAppear() throws InterruptedException {
    Thread.sleep(500);
	raijin.push(KeyCode.CONTROL, KeyCode.H);
	assertTrue(Raijin.isHelpOn);
	raijin.push(KeyCode.CONTROL, KeyCode.H);
  }
  
  @Test
  //@@author A0112213E
  public void testHelpUsingCommand() {
    raijin.type("help").push(KeyCode.ENTER);
	assertTrue(Raijin.isHelpOn);
	raijin.push(KeyCode.CONTROL, KeyCode.H);
  }

  @Test
  //@@author A0112213E
  public void testCutAndPasteShortcut() throws InterruptedException {
    Thread.sleep(500);
    raijin.type("help please cut");
    raijin.doubleClick().push(KeyCode.CONTROL, KeyCode.X);
	raijin.push(KeyCode.CONTROL, KeyCode.V);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
	assertEquals("help please cut", result.getText());
  }

  @Test
  //@@author A0112213E
  public void testGenerationOfTimeSlot() throws InterruptedException {
    Thread.sleep(500);
    raijin.type("add i am busy on 19/10 0800 to 1000").push(KeyCode.ENTER);
    raijin.type("add i am meeting on 19/10");
    HBox result = (HBox) GuiTest.find("#timeSlot");
	assertTrue(result.isVisible());
  }

}
