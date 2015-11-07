package raijin.ui;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.Parent;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.mockito.Mockito;

import raijin.common.datatypes.Task;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;

public class SidebarControllerTest {

  private static SidebarController sidebarController;

  private static GuiTest raijin;
  private static Logic logic;

  private static HashMap<Integer, Task> pendingTasks;

  private static HashMap<Integer, Task> completedTasks;

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @BeforeClass
  public static void setUpClass() throws InterruptedException, FileNotFoundException {
    //================
    // Init UI
    //================

    FXTestUtils.launchApp(Raijin.class);
    Thread.sleep(1000);                     //Wait for program to be launched
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
  @Ignore
  public void updateState_TestNumberOfTasks() {
    TasksChangedEvent changeEvent = new TasksChangedEvent();
  }

}
