package raijin.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import edu.emory.mathcs.backport.java.util.Collections;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.HelpMessage;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.ScrollEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.SetFailureEvent;
import raijin.common.eventbus.events.SetHelpCommandEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.events.SetTimeSlotEvent;
import raijin.common.eventbus.events.UndoRedoEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputController extends BorderPane {
  @FXML
  public TextField inputCommandBar;

  @FXML
  private Label feedbackBar;

  @FXML
  private TextFlow helpBar;
  
  @FXML
  public HBox timeSlot;
  
  @FXML
  private VBox feedbackVBox;

  private static final String INPUT_COMMAND_BAR_LAYOUT_FXML =
      "resource/layout/InputController.fxml";

  private static final String INPUT_FONT = "resource/styles/DejaVuSans.ttf";

  private Raijin mainApp;
  private EventBus eventbus = RaijinEventBus.getEventBus();
  /* Stores previously entered command */
  private ArrayList<String> commandHistory = new ArrayList<String>();
  private static int upCount = 0; // Count number of UP pressed
  private Clipboard clipboard; // System clipboard
  private javafx.scene.input.Clipboard fxClipboard;

  private Font inputFont;

  public InputController(Raijin mainApp) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(INPUT_COMMAND_BAR_LAYOUT_FXML));
    loader.setController(this);
    loader.setRoot(this);
    try {
      loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    this.mainApp = mainApp;
    this.setStyle("-fx-background-color:white;");
    clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    fxClipboard = javafx.scene.input.Clipboard.getSystemClipboard();
    setupStyles();
    handleAllEvents();
    helpBar.setVisible(false);
  }

  public TextField getCommandBar() {
    return inputCommandBar;
  }

  public HBox getTimeSlot() {
    return timeSlot;
  }

  public void clear() {
    inputCommandBar.clear();
  }

  @FXML
  public void onKeyPress(KeyEvent event) {
    handleIOEvent(event);
    handleScrollEvent(event);
    if (Constants.KEY_MINMAX.match(event)) {
      mainApp.resizeWindow();
    } else if (Constants.KEY_CLEAR.match(event)) {
      clear();
    } else if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {
      getPreviousCommands(event);
      event.consume();
    } else {
      upCount = 0;
    }
    mainApp.handleKeyPress(this, event.getCode(), inputCommandBar.getText());
  }

  public void setFeedback(String text) {
    // feedbackBar.setTextFill(Color.BLACK);
    feedbackBar.setText(text);
  }

  public void setInput(String text) {
    inputCommandBar.setText(text);
    inputCommandBar.positionCaret(text.length());
  }

  void handleSetFeedbackEvent() {
    MainSubscriber<SetFeedbackEvent> feedbackSubscriber =
        new MainSubscriber<SetFeedbackEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(SetFeedbackEvent event) {
            feedbackBar.setTextFill(Color.BLACK);
            setFeedback(event.output);
          }
        };
  }

  void handleSetTimeSlotEvent() {
    MainSubscriber<SetTimeSlotEvent> timeSlotSubscriber =
        new MainSubscriber<SetTimeSlotEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(SetTimeSlotEvent event) {
            if (event.isVisible) {
              generateTimeSlots(event.busySlots);
              timeSlot.setVisible(true);
            } else {
              timeSlot.setVisible(false);
            }
          }
        };
  }

  void handleSetHelpEvent() {
    MainSubscriber<SetHelpCommandEvent> helpSubscriber =
        new MainSubscriber<SetHelpCommandEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(SetHelpCommandEvent event) {
            if (event.isVisible) {
              helpBar.getChildren().clear();
              HelpMessage msg = new HelpMessage(event.commandFormat, event.description);
              helpBar.getChildren().addAll(msg.helpMessage);
              helpBar.setVisible(true);
            } else {
              helpBar.setVisible(false);
            }
          }
        };
  }

  public void setFailureFeedback(String text) {
    // feedbackBar.setTextFill(Color.RED);
    setFeedback(text);
  }

  void handleSetFailureEvent() {
    MainSubscriber<SetFailureEvent> failureSubscriber =
        new MainSubscriber<SetFailureEvent>(eventbus) {

          @Subscribe
          @Override
          public void handleEvent(SetFailureEvent event) {
            feedbackBar.setTextFill(Color.RED);
            setFailureFeedback(event.output);
          }
        };
  }

  void handleSetInputEvent() {
    MainSubscriber<SetInputEvent> inputSubscriber = new MainSubscriber<SetInputEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(SetInputEvent event) {
        setInput(event.output);
      }
    };
  }

  void handleTabEvent() {
    inputCommandBar.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
      if (event.getCode() == KeyCode.TAB) {
        eventbus.post(event);
        event.consume();
      }
    });
  }

  void handleHelpBar() {
    helpBar.visibleProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
        if (t1.booleanValue()) { // If maximized
          helpBar.setMaxSize(inputCommandBar.getMaxWidth(), 
              inputCommandBar.getMaxHeight());
        } else {
          helpBar.setMaxSize(0, 0);
          helpBar.setMinSize(0, 0);
        }
      }
    });
  }

  void handleTimeSlot() {
    timeSlot.visibleProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
        if (t1.booleanValue()) { // If maximized
          /*
          timeSlot.setMaxSize(inputCommandBar.getMaxWidth(), 
              timeSlot.getMaxHeight());
              */
          feedbackVBox.getChildren().clear();
          feedbackVBox.getChildren().addAll(feedbackBar, timeSlot);
        } else {
          /*
          timeSlot.setMaxSize(0, 0);
          timeSlot.setMinSize(0, 0);
          */
          feedbackVBox.getChildren().clear();
          feedbackVBox.getChildren().add(feedbackBar);
        }
      }
    });
  }

  /* This is needed because TextArea is not updated till keypress is shot */
  void handleKeyReleaseEvent() {
    inputCommandBar.setOnKeyReleased(new EventHandler<KeyEvent>() {

      @Override
      public void handle(KeyEvent event) {
        if (Constants.KEY_UNDO.match(event)) {
          eventbus.post(new UndoRedoEvent(true, false));
          event.consume();
        } else if (Constants.KEY_REDO.match(event)) {
          eventbus.post(new UndoRedoEvent(false, true));
          event.consume();
        } else {
          String userInput = inputCommandBar.getText();
          eventbus.post(new KeyPressEvent(event, userInput));
        }
        event.consume();
      }

    });
  }

  void handleAllEvents() {
    handleHelpBar();
    handleTimeSlot();
    handleSetTimeSlotEvent();
    handleSetHelpEvent();
    handleTabEvent();
    handleSetFailureEvent();
    handleSetFeedbackEvent();
    handleSetInputEvent();
    handleKeyReleaseEvent();
  }

  public void updateCommandHistory(String command) {
    commandHistory.add(command);
  }

  void getPreviousCommands(KeyEvent event) {
    int index;
    if (!commandHistory.isEmpty()) {
      if (event.getCode() == KeyCode.UP) {
        index = Math.floorMod((commandHistory.size() - (++upCount)), commandHistory.size());
      } else {
        index = Math.floorMod((commandHistory.size() - (--upCount)), commandHistory.size());
      }
      setInput(commandHistory.get(index));
    }
  }

  /**
   * Handles copy, cut, and paste operation
   * 
   * @param event
   */
  void handleIOEvent(KeyEvent event) {
    if (Constants.KEY_COPY.match(event)) {
      copyToClipboard(false);
    } else if (Constants.KEY_PASTE.match(event)) {
      getClipboardContent();
      event.consume();
    } else if (Constants.KEY_CUT.match(event)) {

    }
  }

  /**
   * Handles scroll event using keyboard
   * 
   * @param event
   */
  void handleScrollEvent(KeyEvent event) {
    if (Constants.SCROLL_UP.match(event)) {
      eventbus.post(new ScrollEvent(-1));
      event.consume();
    } else if (Constants.SCROLL_DOWN.match(event)) {
      eventbus.post(new ScrollEvent(1));
      event.consume();
    }
  }

  /**
   * Handles cut and copy operation
   * 
   * @param isCut determines whether selected text will be removed
   */
  public void copyToClipboard(boolean isCut) {
    /*
     * ClipboardContent content = new ClipboardContent();
     * content.putString(inputCommandBar.getSelectedText()); fxClipboard.setContent(content);
     */
    if (isCut) {
      inputCommandBar.cut();
    } else {
      inputCommandBar.copy();
    }
  }

  void getClipboardContent() {
    String data; // To be displayed message
    if (fxClipboard.hasString()) {
      data = fxClipboard.getString();
      int caretPosition = inputCommandBar.getCaretPosition();
      inputCommandBar.insertText(caretPosition, data);
    }
  }
  
  void generateTimeSlots(List<DateTime> slots) {
    timeSlot.getChildren().clear();
    Label intro = new Label("You are busy from: ");
    intro.setTextAlignment(TextAlignment.CENTER);
    intro.setPadding(new Insets(5, 5, 5, 5));
    intro.setStyle("-fx-font-size: 14");
    timeSlot.getChildren().add(intro);
    Collections.sort(slots);

    for (DateTime slot : slots) {
      timeSlot.getChildren().add(createTimeSlot(slot));
    }
  }

  Label createTimeSlot(DateTime slot) {
    String startTime = slot.getStartTime().toString();
    String endTime = slot.getEndTime().toString();
    String duration = startTime + " ~ " + endTime;
    Label timeSlot = new Label(duration);
    timeSlot.setPadding(new Insets(5, 5, 5, 5));
    timeSlot.setStyle("-fx-font-size: 14; -fx-background-color: #FF8000; "
        + "-fx-border-radius: 5 5 5 5; -fx-background-radius: 5 5 5 5;");
    return timeSlot;
  }

  void setupStyles() {
    inputFont = Font.loadFont(getClass().getResource(INPUT_FONT).toString(), 18);
    inputCommandBar.setFont(inputFont);
  }

}
