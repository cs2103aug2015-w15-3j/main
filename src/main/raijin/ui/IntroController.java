//@@author A0129650E -unused 
//This class is not used anymore because we decided
//to go full keyboard in our program. This class
//will make user do some clicking in order to set 
//his directory. 

package raijin.ui;

import java.awt.Button;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import raijin.logic.api.Logic;

public class IntroController extends BorderPane {
	
	private Raijin mainApp;
	private Logic logic;
	private Stage stage;
	private DirectoryChooser directoryChooser;
	
	@FXML
	private Button getStarted;
	
	@FXML
	private Label message;

	
	public IntroController(Raijin mainApp, Logic logic, Stage stage) {
		this.mainApp = mainApp;
		this.logic = logic;
		this.stage = stage;
	}
	
	@FXML
	public void handleGettingStartedAction() {
		directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(this.stage);

		if (selectedDirectory == null) {
			System.out.println("did not set directory");
		} else {
		  logic.setChosenUserStorage(selectedDirectory.getAbsolutePath());
		  System.out.println("setted new directory");
		  mainApp.changeToMinimisedView();
		}
	}
}
