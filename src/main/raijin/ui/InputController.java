package raijin.ui;

import javafx.scene.layout.BorderPane;

import java.io.IOException;

import raijin.common.utils.EventBus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class InputController extends BorderPane {
	@FXML
	private TextField inputCommandBar;
	
	@FXML
	private Label feedbackBar;
	
	private static final String INPUT_COMMAND_BAR_LAYOUT_FXML = "resource/layout/InputController.fxml";
	
	private Raijin mainApp;
	private EventBus eventBus = EventBus.getEventBus();
	
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
		
		//Setup
		eventBus.feedBackProperty().addListener( (v, oldVal, newVal) -> {
		  setFeedback(newVal);
		});
	}
	
	public void clear() {
		inputCommandBar.clear();
	}
	
	@FXML
	public void onKeyPress(KeyEvent event) {
		mainApp.handleKeyPress(this, event.getCode(), inputCommandBar.getText());
	}
	
	public void setFeedback(String text) {
		feedbackBar.setText(text);
	}
}	
