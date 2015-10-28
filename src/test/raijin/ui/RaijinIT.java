package raijin.ui;

import static org.junit.Assert.*;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;

import raijin.common.datatypes.Constants;

public class RaijinIT {

  private static GuiTest raijin;

  @BeforeClass
  public static void setUpClass() throws InterruptedException {
    FXTestUtils.launchApp(Raijin.class);
    Thread.sleep(2000);
    raijin = new GuiTest() {

      @Override
      protected Parent getRootNode() {
        return Raijin.getStage().getScene().getRoot();
      }

    };
  }

  @Test
  public void testAddCommandAutoComplete() throws InterruptedException {
    raijin.type(KeyCode.A);
    raijin.press(KeyCode.TAB);
    Thread.sleep(2000);
    TextField result = (TextField) GuiTest.find("#inputCommandBar");
    assertEquals("add", result.getText());
  }

  @Test
  public void testMaximiseShortcut() throws InterruptedException {
    raijin.push(KeyCode.ALT, KeyCode.ESCAPE);
    Thread.sleep(1000);
    assertTrue(Raijin.getStage().isMaximized());
  }

}
