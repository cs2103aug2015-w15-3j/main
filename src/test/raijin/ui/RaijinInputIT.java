//@@author A0112213E

package raijin.ui;

import static org.junit.Assert.*;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseButton;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import raijin.common.datatypes.Constants;

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

}
