//@@author A0112213E

package raijin.ui;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.util.converter.LocalDateStringConverter;

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
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.utils.TaskUtils;
import raijin.helper.TestUtils;
import raijin.logic.api.Logic;
import raijin.storage.api.TasksManager;

public class DisplayControllerIT {

  private static final LocalDate overdue = LocalDate.now().minusDays(2L);
  private static final LocalDate today = LocalDate.now();
  private static final LocalDate tomorrow = today.plusDays(1L);
  private static final LocalDate future = today.plusDays(5L);

  private static final String specific = "19/09/2100";
  private static final String specific2 = "21/09/2100";

  private static DisplayController displayController;
  private static GuiTest raijin;
  private static Logic logic;
  private static RaijinEventBus eventbus = RaijinEventBus.getInstance();


  private static HashMap<Integer, Task> pendingTasks;
  private static HashMap<Integer, Task> completedTasks;
  private static TestUtils testUtils;

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
    testUtils = new TestUtils();
    pendingTasks = new HashMap<Integer, Task>();
    
    DateTime overdueDate = new DateTime(overdue, null, overdue, LocalTime.MAX);
    DateTime todayDate = new DateTime(today, null, today, LocalTime.MAX);
    DateTime tomorrowDate = new DateTime(tomorrow, null, tomorrow, LocalTime.MAX);
    DateTime futureDate = new DateTime(future, null, future, LocalTime.MAX);

    /*Adding floating tasks*/
    pendingTasks.put(1, new Task("test 1", 1));
    pendingTasks.put(2, new Task("test 2", 2));
    pendingTasks.put(3, new Task("test 3", 3));

    /*Adding overdue tasks*/
    pendingTasks.put(4, testUtils.createTask("meet james", overdueDate));
    pendingTasks.put(5, testUtils.createTask("meet thompson", overdueDate));

    /*Adding specific task*/
    pendingTasks.put(6, testUtils.createTask("meet thompson", new DateTime(
        LocalDate.of(2100, 9, 19), LocalDate.of(2100, 9, 19))));
    pendingTasks.put(13, testUtils.createTask("meet bomb", new DateTime(
        LocalDate.of(2100, 9, 21), LocalDate.of(2100, 9, 21))));
    /*Today's tasks*/
    pendingTasks.put(7, testUtils.createTask("meet owl", todayDate));
    pendingTasks.put(8, testUtils.createTask("meet hoot", todayDate));
    /*Tomorrow's tasks*/
    pendingTasks.put(9, testUtils.createTask("buy toy gun gun", tomorrowDate));
    pendingTasks.put(10, testUtils.createTask("meet pinu", tomorrowDate));
    /*Future's tasks*/
    pendingTasks.put(11, testUtils.createTask("buy toy gun gun", futureDate));

    /*Adding completed task*/
    completedTasks = new HashMap<Integer, Task>();
    completedTasks.put(12, new Task("test", 4));

    TasksManager.getManager().setPendingTasks(pendingTasks);
    TasksManager.getManager().setCompletedTasks(completedTasks);

    displayController = new DisplayController();
  }

  @Test
  public void displayFloating_ReturnThreeTasks() {
    logic.executeCommand("display f");
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(3, displayedTasks.size());
  }

  @Test
  public void displayOverdue_ReturnTwoTasks() {
    logic.executeCommand("display o");
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(2, displayedTasks.size());
  }

  @Test
  public void displayCompleted_ReturnOneTask() {
    logic.executeCommand("display c");
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(1, displayedTasks.size());
  }

  @Test
  public void displaySpecific_ReturnOneTask() {
    logic.executeCommand("display " + specific);
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(1, displayedTasks.size());
  }

  @Test
  public void displayAll_ReturnTwelveTasks() {
    logic.executeCommand("display a");
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(12, displayedTasks.size());
  }

  @Test
  public void displayDefault_ReturnTenTasks() {
    logic.executeCommand("display");
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(10, displayedTasks.size());
  }

  @Test
  public void displayRange_ReturnTwoTasks() {
    String dateInput = specific + " to " + specific2;
    logic.executeCommand("display " + dateInput);
    List<Task> displayedTasks = eventbus.getDisplayedTasks();
    assertEquals(2, displayedTasks.size());
  }

  @Test
  public void scrollUpDown_IndexReturnToOriginal() throws InterruptedException {
    raijin.push(KeyCode.PAGE_DOWN);
    raijin.push(KeyCode.PAGE_UP);
    int scrollIndex = DisplayController.scrollIndex;
    assertEquals(0, scrollIndex);
  }

}
