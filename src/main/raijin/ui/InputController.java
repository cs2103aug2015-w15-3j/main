package raijin.ui;

import javafx.scene.layout.BorderPane;

import java.io.IOException;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class InputController extends BorderPane {
	@FXML
	public TextField inputCommandBar;
	
	@FXML
	private Label feedbackBar;
	
	private static final String INPUT_COMMAND_BAR_LAYOUT_FXML = "resource/layout/InputController.fxml";
	
	private Raijin mainApp;
	private EventBus eventbus = RaijinEventBus.getEventBus();
	
	public InputController(Raijin mainApp) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(INPUT_COMMAND_BAR_LAYOUT_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.mainApp = mainApp;
		handleSetFeedbackEvent();             //Handles any call to set feedback
	}
	
	public TextField getCommandBar() {
		return inputCommandBar;
	}
	
	public void clear() {
		inputCommandBar.clear();
	}
	
	@FXML
	public void onKeyPress(KeyEvent event) {
	    System.out.println(event.getText());
	    eventbus.post(new KeyPressEvent(event, inputCommandBar.getText()));
		mainApp.handleKeyPress(this, event.getCode(), inputCommandBar.getText());
	}
	
	public void setFeedback(String text) {
		feedbackBar.setText(text);
	}
	
	public void handleSetFeedbackEvent() {
	  MainSubscriber<SetFeedbackEvent> feedbackSubscriber = new MainSubscriber<
	      SetFeedbackEvent>(eventbus) {

	      @Subscribe
          @Override
          public void handleEvent(SetFeedbackEvent event) {
            setFeedback(event.output);
            
          }};
	}

	public void handleSetInputEvent() {
	  MainSubscriber<SetInputEvent> inputSubscriber = new MainSubscriber<
	      SetInputEvent>(eventbus) {

	      @Subscribe
          @Override
          public void handleEvent(SetInputEvent event) {
            inputCommandBar.setText(event.output);
            
          }};
	}
}	
