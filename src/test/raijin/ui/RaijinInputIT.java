//@@author A0112213E

package raijin.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class RaijinInputIT {

  private static GuiTest raijin;

  @BeforeClass
  public static void setUpClass() throws InterruptedException {
    FXTestUtils.launchApp(Raijin.class);
    Thread.sleep(5000);                     //Wait for program to be launched
    raijin = new GuiTest() {

      @Override
      protected Parent getRootNode() {
        return Raijin.getStage().getScene().getRoot();
      }

    };
  }

  @After
  public void cleanUp() {
    raijin.release(new KeyCode[] {});
    raijin.release(new MouseButton[] {});
  }

  @Test
  public void testAddCommandAutoComplete() throws InterruptedException {
    raijin.type(KeyCode.A).push(KeyCode.TAB);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
    assertEquals("add", result.getText());
  }

  @Test
  public void testMaximiseShortcut() throws InterruptedException {
    raijin.push(KeyCode.ALT, KeyCode.ESCAPE);
    assertTrue(Raijin.getStage().isMaximized());
  }

//@@author A0129650E
  
  @Test
  public void testHelpAppear() throws InterruptedException {
	raijin.push(KeyCode.CONTROL, KeyCode.H);
	assertTrue(Raijin.isHelpOn);
  }
  
  @Test
  public void testHide() throws InterruptedException {
	raijin.push(KeyCode.CONTROL, KeyCode.SPACE);
	assertTrue(Raijin.isVisible);  
  }
  
  @Test
  public void testHideAndAppear() throws InterruptedException {
	raijin.push(KeyCode.CONTROL, KeyCode.SPACE);
	raijin.push(KeyCode.CONTROL, KeyCode.SPACE);
	assertTrue(Raijin.isVisible);  
  }
  
  @Test
  public void testHistoryCommand() throws InterruptedException {
	  
  }
  
}
