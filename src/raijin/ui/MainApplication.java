package raijin.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class MainApplication extends Application {
	private static final String ROOT_LAYOUT_FXML_LOCATION = "resources/layout/RootLayout.fxml";
	
	private BorderPane rootLayout;
	private Stage stage;
	
	public static void main(String[] args) {
		    launch(args);
	}
	
  @Override
  public void start(Stage stage) throws Exception {
    /*Adding fxml */
    initRootLayout();
    initPrimaryStage(stage);
    
   // addDisplayController(this);
   addInputController(this);
  }

  private void initPrimaryStage(Stage stage) {
	this.stage = stage;
	this.stage.setTitle("Welcome to Raijin");
    this.stage.setScene(new Scene(rootLayout));
    this.stage.show();
  }

  private void initRootLayout() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT_FXML_LOCATION));
	try {
		rootLayout = loader.load();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
 
  /**
   * method to put the DisplayController class that is another FXML
   * file containing information about the display bar ONLY.
   * 
   * @param mainApp
   */
  private void addDisplayController(MainApplication mainApp) {
	  rootLayout.setCenter(new DisplayController());
  }
  
  /**
   * method to put only the command bar into the main layout.
   * InputController is also another FXML file with its own
   * information.
   * 
   * @param mainApp
   */
  private void addInputController(MainApplication mainApp) {
	  rootLayout.setBottom(new InputController(mainApp));
  }
  
  // Methods to transfer to logic
  
  public void handleKeyPress(InputController inputController,
		  KeyCode key,
		  String userInput) {
	  if (key==KeyCode.ENTER) {
		  handleEnterPress(inputController, userInput);
	  }
  }
  
  private void handleEnterPress(InputController inputController, String userInput) {
	  inputController.setFeedback(userInput);
  }
}
