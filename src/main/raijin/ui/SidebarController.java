package raijin.ui;

import java.io.IOException;
import java.util.List;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.utils.filter.TypeFilter;
import raijin.logic.api.Logic;

public class SidebarController extends BorderPane {
  
  //===========================================================================
  // UI elements
  //===========================================================================

  @FXML
  private Button inbox;
  @FXML
  private Label numOfPending;
  @FXML
  private Button overdue;
  @FXML
  private Label numOfOverdue;
  @FXML
  private Button completed;
  @FXML
  private Label numOfCompleted;
  @FXML
  private Button today;
  @FXML
  private Label numOfToday;
  @FXML
  private Button tomorrow;
  @FXML
  private Label numOfTomorrow;
  @FXML
  private Button nextWeek;
  @FXML
  private Label numOfNextWeek;

  private Button currentFocusedButton;                //Determines current view


  private static final String SIDEBAR_LAYOUT_FXML = "resource/layout/SidebarController.fxml";
  private com.google.common.eventbus.EventBus eventbus;
  
  //===========================================================================
  // Domain objects
  //===========================================================================
  
  private Logic logic;
  private List<Task> pendingTasks;
  private List<Task> overdueTasks;
  private List<Task> completedTasks;
  private List<Task> pendingToday;
  private List<Task> pendingTomorrow;
  private List<Task> pendingNextWeek;

  public SidebarController(Logic logic) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(SIDEBAR_LAYOUT_FXML));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //=========================================================================
    // Initialisation
    //=========================================================================
    
    this.logic = logic;
    eventbus = RaijinEventBus.getEventBus();
    handleTaskChanged();
    handleChangeView();
    currentFocusedButton = inbox;
  }

  @FXML
  protected void handleInboxButtonAction(ActionEvent event) {
    triggerViewChange(Constants.View.INBOX);
  }

  @FXML
  protected void handleOverdueButtonAction(ActionEvent event) {
    triggerOverdueViewChange();
  }

  @FXML
  protected void handleCompletedButtonAction(ActionEvent event) {
    logic.executeCommand("display c");
  }

  @FXML
  protected void handleTodayButtonAction(final InputEvent event) {
    triggerViewChange(Constants.View.TODAY);
  }

  @FXML
  protected void handleTomorrowButtonAction(final InputEvent event) {
    triggerViewChange(Constants.View.TOMORROW);
  }

  @FXML
  protected void handleNextWeekButtonAction(final InputEvent event) {
    triggerViewChange(Constants.View.NEXT_WEEK);
  }


  // =========================================================================
  // Handlers
  // =========================================================================

  public void handleChangeView() {
    MainSubscriber<ChangeViewEvent> changeViewHandler =
        new MainSubscriber<ChangeViewEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(ChangeViewEvent event) {
            updateFocus(event);
          }
    };
  }

  public void handleTaskChanged() {
    MainSubscriber<TasksChangedEvent> tasksChangedHandler =
        new MainSubscriber<TasksChangedEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(TasksChangedEvent event) {
            updateState(event);
          }
    };
  }
  
  void updateState(TasksChangedEvent event) {
    pendingTasks = event.pendingTasks;
    overdueTasks = event.overdue;
    completedTasks = event.completedTasks;

    pendingToday = event.pendingToday;
    pendingTomorrow = event.pendingTomorrow;
    pendingNextWeek = event.pendingNextWeek;
    
    updateLabels();
  }
  

  void updateFocus(ChangeViewEvent event) {
    switch (event.typeOfView) {

      case INBOX:
        setNewFocus(inbox);
        break;

      case NEXT_WEEK:
        setNewFocus(nextWeek);
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

  //===========================================================================
  // Helper methods
  //===========================================================================
  
  /**
   * Update number of pending tasks when change occur to application 
   */
  void updateLabels() {
    numOfPending.setText(Integer.toString(pendingTasks.size()));
    numOfOverdue.setText(Integer.toString(overdueTasks.size()));
    numOfCompleted.setText(Integer.toString(completedTasks.size()));

    numOfToday.setText(Integer.toString(pendingToday.size()));
    numOfTomorrow.setText(Integer.toString(pendingTomorrow.size()));
    numOfNextWeek.setText(Integer.toString(pendingNextWeek.size()));
  }
  
  void triggerViewChange(Constants.View view) {
    eventbus.post(new ChangeViewEvent(pendingTasks, view));
  }
  
  void triggerOverdueViewChange() {
    eventbus.post(new SetCurrentDisplayEvent(overdueTasks, "Overdue"));
  }

  //Set button color when view changes
  void setNewFocus(Button newFocusedButton) {
    if (!newFocusedButton.equals(currentFocusedButton)) {
      System.out.println("Diff");
      currentFocusedButton.setStyle("-fx-background-color: #ffffff;");
      newFocusedButton.setStyle("-fx-background-color: #ccf8ff;");
      currentFocusedButton = newFocusedButton;
    }
  }

}
