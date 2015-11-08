//@@author A0112213E

package raijin.ui;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.mockito.Mockito;

import com.sun.javafx.robot.FXRobot;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.utils.TaskUtils;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;

public class SidebarControllerIT {

  private static SidebarController sidebarController;

  private static GuiTest raijin;
  private static Logic logic;

  private static HashMap<Integer, Task> pendingTasks;

  private static HashMap<Integer, Task> completedTasks;

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Before
  public void setUp() {
    Platform.runLater(new Runnable() {
      
      @Override
      public void run() {
        Raijin.getStage().setMaximized(true);
      }
    });
  }

  @BeforeClass
  public static void setUpClass() throws InterruptedException, FileNotFoundException {
    //================
    // Init UI
    //================

    FXTestUtils.launchApp(Raijin.class);
    Thread.sleep(5000);                     //Wait for program to be launched
    raijin = new GuiTest() {

      @Override
      protected Parent getRootNode() {
        return Raijin.getStage().getScene().getRoot();
      }

    };


    //====================
    // Init domain objects
    //====================
    
    logic = new Logic();
    pendingTasks = new HashMap<Integer, Task>();
    pendingTasks.put(1, new Task("test 1", 1));
    pendingTasks.put(2, new Task("test 2", 2));
    pendingTasks.put(3, new Task("test 3", 3));

    completedTasks = new HashMap<Integer, Task>();
    completedTasks.put(4, new Task("test", 4));

    TasksManager.getManager().setPendingTasks(pendingTasks);
    TasksManager.getManager().setCompletedTasks(completedTasks);

    sidebarController = new SidebarController(logic);
  }
  
  @Test
  public void updateState_TestNumberOfPendingTasks() {
    TasksChangedEvent changeEvent = new TasksChangedEvent();
    sidebarController.updateState(changeEvent);
    assertEquals("3", sidebarController.numOfPending.getText());
  }

  @Test
  public void updateState_TestNumberOfCompletedTasks() {
    TasksChangedEvent changeEvent = new TasksChangedEvent();
    sidebarController.updateState(changeEvent);
    assertEquals("1", sidebarController.numOfCompleted.getText());
  }

  @Test
  public void updateView_ChangeToToday() {
    ChangeViewEvent event = new ChangeViewEvent(TaskUtils.getTasksList(
        pendingTasks), Constants.View.TODAY);
    sidebarController.updateFocus(event);
    assertEquals(sidebarController.currentFocusedButton, 
        sidebarController.today);
  }

  @Test
  public void updateView_ChangeToTomorrow() {
    ChangeViewEvent event = new ChangeViewEvent(TaskUtils.getTasksList(
        pendingTasks), Constants.View.TOMORROW);
    sidebarController.updateFocus(event);
    assertEquals(sidebarController.currentFocusedButton, 
        sidebarController.tomorrow);
  }
  
  @Test
  public void clickTomorrow_FocusOnTomorrowButton() throws InterruptedException {
    Button tomorrow = (Button) GuiTest.find("#tomorrow");
    raijin.move(tomorrow).click(tomorrow);
    Button focusButton = sidebarController.currentFocusedButton;
    assertEquals(focusButton, sidebarController.tomorrow);
  }

  @Test
  public void clickToday_FocusOnNextWeekButton() {
    Button future = (Button) GuiTest.find("#future");
    raijin.move(future).click(future);
    Button focusButton = sidebarController.currentFocusedButton;
    assertEquals(focusButton, sidebarController.future);
  }

}
