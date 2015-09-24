package raijin.ui;

import javafx.scene.layout.BorderPane;

import java.io.IOException;

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
	
	private static final String INPUT_COMMAND_BAR_LAYOUT_FXML = "resources/layout/InputController.fxml";
	
	private MainApplication mainApp;
	
	public InputController(MainApplication mainApp) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(INPUT_COMMAND_BAR_LAYOUT_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.mainApp = mainApp;
	}
	
	@FXML
	public void onKeyPress(KeyEvent event) {
		mainApp.handleKeyPress(this, event.getCode(), inputCommandBar.getText());
	}
	
	public void setFeedback(String text) {
		feedbackBar.setText(text);
	}
}	
