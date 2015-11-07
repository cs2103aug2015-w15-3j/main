//@@author A0112213E

package raijin.ui;

import java.io.IOException;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.MainSubscriber;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.filter.DateFilter;
import raijin.common.filter.TypeFilter;
import raijin.logic.api.Logic;

/**
 * Contains useful information such number of pending tasks for 
 * different categories of views
 * @author papa
 *
 */
public class SidebarController extends BorderPane {
  
  //============
  // UI elements
  //============

  @FXML
  Button inbox;
  @FXML
  Label numOfPending;
  @FXML
  Button overdue;
  @FXML
  Label numOfOverdue;
  @FXML
  Button completed;
  @FXML
  Label numOfCompleted;
  @FXML
  Button floating;
  @FXML
  Label numOfFloating;
  @FXML
  Button today;
  @FXML
  Label numOfToday;
  @FXML
  Button tomorrow;
  @FXML
  Label numOfTomorrow;
  @FXML
  Button future;
  @FXML
  Label numOfNextTasks;

  /*Reference to button focused by user*/
  Button currentFocusedButton;               


  private static final String SIDEBAR_LAYOUT_FXML = "resource/layout/SidebarController.fxml";
  private RaijinEventBus eventbus;
  
  //===============
  // Domain objects
  //===============
  
  private Logic logic;
  private List<Task> pendingTasks;
  private List<Task> overdueTasks;
  private List<Task> completedTasks;
  private List<Task> floatingTasks;
  private List<Task> pendingToday;
  private List<Task> pendingTomorrow;
  private List<Task> pendingNextWeek;
  private TypeFilter floatingFilter;

  public SidebarController(Logic logic) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(SIDEBAR_LAYOUT_FXML));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.logic = logic;
    eventbus = RaijinEventBus.getInstance();
    init();
  }

  void init() {
    //Initialize states
    pendingTasks = logic.getPendingTasks();
    pendingToday = new DateFilter(pendingTasks, Constants.View.TODAY.getDateTime()).
        filter(pendingTasks);
    pendingTomorrow = new DateFilter(pendingTasks, Constants.View.TOMORROW.getDateTime()).
        filter(pendingTasks);
    pendingNextWeek = new DateFilter(pendingTasks, Constants.View.FUTURE.getDateTime()).
        filter(pendingTasks, Constants.View.FUTURE);
    overdueTasks = new TypeFilter(Constants.TYPE_TASK.OVERDUE).filter(pendingTasks);
    completedTasks = logic.getCompletedTasks();
    floatingFilter = new TypeFilter(Constants.TYPE_TASK.FLOATING);
    floatingTasks = floatingFilter.filter(pendingTasks);
    
    /*initialize labels*/
    updateLabels();
    
    /*initialize handlers*/
    handleTaskChanged();
    handleChangeView();
    
    /*Set current focused button*/
    currentFocusedButton = inbox;
  }

  /**
   * Update number of pending tasks when change occur to application 
   */
  void updateLabels() {
    numOfPending.setText(Integer.toString(pendingTasks.size()));
    numOfOverdue.setText(Integer.toString(overdueTasks.size()));
    numOfCompleted.setText(Integer.toString(completedTasks.size()));
    numOfFloating.setText(Integer.toString(floatingTasks.size()));

    numOfToday.setText(Integer.toString(pendingToday.size()));
    numOfTomorrow.setText(Integer.toString(pendingTomorrow.size()));
    numOfNextTasks.setText(Integer.toString(pendingNextWeek.size()));
  }

  /**
   * Sync domain object whenever any changes occur
   * @param event
   */
  void updateState(TasksChangedEvent event) {
    pendingTasks = event.pendingTasks;
    overdueTasks = event.overdue;
    completedTasks = event.completedTasks;
    floatingTasks = floatingFilter.filter(pendingTasks);

    pendingToday = event.pendingToday;
    pendingTomorrow = event.pendingTomorrow;
    pendingNextWeek = event.pendingNextWeek;
    updateLabels();
  }
  

  /**
   * Sets focus button to the one holding current view
   * @param event
   */
  void updateFocus(ChangeViewEvent event) {
    switch (event.typeOfView) {

      case INBOX:
        setNewFocus(inbox);
        break;

      case FUTURE:
        setNewFocus(future);
        break;

      case TODAY:
        setNewFocus(today);
        break;

      case TOMORROW:
        setNewFocus(tomorrow);
        break;

      default:
        break;
      
    }
  }

  /*sets button color when view changes*/
  void setNewFocus(Button newFocusedButton) {
    if (!newFocusedButton.equals(currentFocusedButton)) {
      currentFocusedButton.setStyle("-fx-background-color: #ffffff;");
      newFocusedButton.setStyle("-fx-background-color: #ccf8ff;");
      currentFocusedButton = newFocusedButton;
    }
  }

  /**
   * Fire change view event to trigger change in display
   * @param view        view that will be displayed to user
   */
  void triggerViewChange(Constants.View view) {
    eventbus.post(new ChangeViewEvent(pendingTasks, view));
  }
  
  /**
   * Fire overdue view event to trigger change in display
   * @param view        view that will be displayed to user
   */
  void triggerOverdueViewChange() {
    eventbus.post(new SetCurrentDisplayEvent(overdueTasks, Constants.DISPLAY_OVERDUE));
  }

  // ========
  // Handlers
  // ========

  @FXML
  protected void handleInboxButtonAction(ActionEvent event) {
    triggerViewChange(Constants.View.INBOX);
    setNewFocus(inbox);
  }

  @FXML
  protected void handleOverdueButtonAction(ActionEvent event) {
    setNewFocus(overdue);
    triggerOverdueViewChange();
  }

  @FXML
  protected void handleCompletedButtonAction(ActionEvent event) {
    logic.executeCommand("display c");
    setNewFocus(completed);
  }

  @FXML
  protected void handleTodayButtonAction(ActionEvent event) {
    triggerViewChange(Constants.View.TODAY);
    setNewFocus(today);
  }

  @FXML
  protected void handleTomorrowButtonAction(ActionEvent event) {
    triggerViewChange(Constants.View.TOMORROW);
    setNewFocus(tomorrow);
  }

  @FXML
  protected void handleFutureButtonAction(ActionEvent event) {
    triggerViewChange(Constants.View.FUTURE);
    setNewFocus(future);
  }


  @FXML
  protected void handleFloatingButtonAction(ActionEvent event) {
    logic.executeCommand("display f");
    setNewFocus(floating);
  }

  public void handleChangeView() {
    MainSubscriber<ChangeViewEvent> changeViewHandler =
        new MainSubscriber<ChangeViewEvent>(eventbus.getEventBus()) {

          @Subscribe
          @Override
          public void handleEvent(ChangeViewEvent event) {
            updateFocus(event);
          }
    };
  }

  public void handleTaskChanged() {
    MainSubscriber<TasksChangedEvent> tasksChangedHandler =
        new MainSubscriber<TasksChangedEvent>(eventbus.getEventBus()) {

          @Subscribe
          @Override
          public void handleEvent(TasksChangedEvent event) {
            updateState(event);
          }
    };
  }
  
}
