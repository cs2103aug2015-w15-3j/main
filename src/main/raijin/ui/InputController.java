package raijin.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.io.IOException;
import java.util.ArrayList;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.datatypes.Constants;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.SetFailureEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputController extends BorderPane {
  @FXML
  public TextField inputCommandBar;

  @FXML
  private Label feedbackBar;

  private static final String INPUT_COMMAND_BAR_LAYOUT_FXML =
      "resource/layout/InputController.fxml";

  private Raijin mainApp;
  private EventBus eventbus = RaijinEventBus.getEventBus();
  /* Stores previously entered command */
  private ArrayList<String> commandHistory = new ArrayList<String>();
  private static int upCount = 0; // Count number of UP pressed
  private Clipboard clipboard; // System clipboard
  private javafx.scene.input.Clipboard fxClipboard;

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
    handleAllEvents();
  }

  public TextField getCommandBar() {
    return inputCommandBar;
  }

  public void clear() {
    inputCommandBar.clear();
  }

  @FXML
  public void onKeyPress(KeyEvent event) {
    handleIOEvent(event);
    if (Constants.KEY_CLEAR.match(event)) {
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

  /* This is needed because TextArea is not updated till keypress is shot */
  void handleKeyReleaseEvent() {
    inputCommandBar.setOnKeyReleased(new EventHandler<KeyEvent>() {

      @Override
      public void handle(KeyEvent event) {
        String userInput = inputCommandBar.getText();
        eventbus.post(new KeyPressEvent(event, userInput));
      }

    });
  }

  void handleAllEvents() {
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
   * Handles cut and copy operation
   * @param isCut determines whether selected text will be removed
   */
  public void copyToClipboard(boolean isCut) {
    /*
    ClipboardContent content = new ClipboardContent();
    content.putString(inputCommandBar.getSelectedText());
    fxClipboard.setContent(content);
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

}
