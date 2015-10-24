package raijin.ui;

import java.io.IOException;

import com.google.common.eventbus.Subscribe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.TasksChangedEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.logic.api.Logic;

public class SidebarController extends BorderPane {
  
  private static final String TODAY = "Today %d";
  private static final String TOMORROW = "Tomorrow %d";
  private static final String NEXT_WEEK = "Next week %d";
  private static final String OVERDUE = "Overdue %d";
  private static final String COMPLETED = "Completed %d";
  private static final Color numberOfTasks = Color.rgb(213,102,102);

  @FXML
  private Button today;

  @FXML
  private Button tomorrow;

  @FXML
  private Button nextWeek;

  @FXML
  private Button overdue;

  @FXML
  private Button completed;

  private static final String SIDEBAR_LAYOUT_FXML = "resource/layout/SidebarController.fxml";
  private Logic logic;
  private com.google.common.eventbus.EventBus eventbus;

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
  }

  @FXML
  protected void handleTodayButtonAction(ActionEvent event) {
    logic.executeCommand("display");
  }

  @FXML
  protected void handleTomorrowButtonAction(ActionEvent event) {
    logic.executeCommand("display");
  }

  @FXML
  protected void handleNextWeekButtonAction(ActionEvent event) {

  }

  @FXML
  protected void handleProjectsButtonAction(ActionEvent event) {

  }

  @FXML
  protected void handleCompletedButtonAction(ActionEvent event) {
    logic.executeCommand("display c");
  }

  @FXML
  protected void handleTodayButtonKeyAction(final InputEvent event) {
    if (event instanceof KeyEvent) {
      final KeyEvent keyEvent = (KeyEvent) event;
      if (keyEvent.getCode() == KeyCode.ENTER) {
        logic.executeCommand("display p");
      }
    }
  }

  @FXML
  protected void handleTomorrowButtonKeyAction(final InputEvent event) {
    if (event instanceof KeyEvent) {
      final KeyEvent keyEvent = (KeyEvent) event;
      if (keyEvent.getCode() == KeyCode.ENTER) {
        // TODO
      }
    }
  }

  @FXML
  protected void handleNextWeekButtonKeyAction(final InputEvent event) {
    if (event instanceof KeyEvent) {
      final KeyEvent keyEvent = (KeyEvent) event;
      if (keyEvent.getCode() == KeyCode.ENTER) {
        // TODO
      }
    }
  }

  @FXML
  protected void handleProjectsButtonKeyAction(final InputEvent event) {
    if (event instanceof KeyEvent) {
      final KeyEvent keyEvent = (KeyEvent) event;
      if (keyEvent.getCode() == KeyCode.ENTER) {
        // TODO
      }
    }
  }

  @FXML
  protected void handleCompletedButtonKeyAction(final InputEvent event) {
    if (event instanceof KeyEvent) {
      final KeyEvent keyEvent = (KeyEvent) event;
      if (keyEvent.getCode() == KeyCode.ENTER) {
        logic.executeCommand("display c");
      }
    }
  }

  // =========================================================================
  // Handlers
  // =========================================================================

  public void handleTaskChanged() {
    MainSubscriber<TasksChangedEvent> tasksChangedHandler =
        new MainSubscriber<TasksChangedEvent>(eventbus) {

          @Override
          public void handleEvent(TasksChangedEvent event) {
            updateButtons(event);
          }
    };
  }
  
  void updateButtons(TasksChangedEvent event) {
    today.setText(String.format(TODAY, event.pendingToday.size()));
    tomorrow.setText(String.format(TOMORROW, event.pendingTomorrow.size()));
    nextWeek.setText(String.format(NEXT_WEEK, event.pendingNextWeek.size()));
    overdue.setText(String.format(OVERDUE, event.overdue.size()));
    completed.setText(String.format(COMPLETED, event.completedTasks.size()));
  }
  

}
