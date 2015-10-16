package raijin.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;
import raijin.logic.api.Logic;
import raijin.storage.api.Session;

public class Raijin extends Application {
	private static final String ROOT_LAYOUT_FXML_LOCATION = "resource/layout/RootLayout.fxml";
	private static final String INTRO_LAYOUT_FXML_LOCATION = "resource/layout/IntroLayout.fxml";
	
	private static final String NO_DIRECTORY_SELECTED_FEEDBACK = "I'm sorry! You have not selected "
															+ "a directory yet. Please try again!";
	private BorderPane rootLayout, introLayout;
	private Stage stage;
	private Scene workingScene, introScene;
	private Logic logic;
	private IntroController introController;
	private Session session;
	
	public static void main(String[] args) {
	  launch(args);
	}
	
  @Override
  public void start(Stage stage) throws Exception {
    /*Adding fxml */
    initPrimaryStage(stage);
    initLogic();
    initSession();
    decideScene();
    
    this.stage.show();
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	public void handle(WindowEvent onClose) {
    		session.writeOnExit();
    	}
    });
  }

  private void initPrimaryStage(Stage stage) {
	this.stage = stage;
	this.stage.setTitle("Welcome to Raijin");
  }

  private void initRootLayout() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT_FXML_LOCATION));
	try {
		rootLayout = loader.load();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
 
  private void initLogic() throws FileNotFoundException {
	  logic = new Logic();
  }
  
  private void initSession() {
	  this.session = logic.getSession();
  }
  
  private void initIntroLayout() {
	  FXMLLoader loader = new FXMLLoader(getClass().getResource(INTRO_LAYOUT_FXML_LOCATION));
	  loader.setController(introController);
	  loader.setRoot(introController);
	  
	  try {
			introLayout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
  }
  
  public void initMainLayout() {
	  initRootLayout();
  	  addDisplayController(this);
      addInputController(this);
      ((InputController) rootLayout.getBottom()).getCommandBar().requestFocus();
  
      this.stage.setScene(new Scene(rootLayout));
  }
  
  public void decideScene() {
	 
	  if (logic.isFirstTime()) {
		  introController = new IntroController(this, logic, stage);
		  initIntroLayout();
	
		  this.stage.setScene(new Scene(introLayout));
	  } else {
		  initMainLayout();
	  }  
  }
  
  /**
   * method to put the DisplayController class that is another FXML
   * file containing information about the display bar ONLY.
   * 
   * @param mainApp
   */
  private void addDisplayController(Raijin mainApp) {
	  rootLayout.setCenter(new DisplayController());
  }
  
  /**
   * method to put only the command bar into the main layout.
   * InputController is also another FXML file with its own
   * information.
   * 
   * @param mainApp
   */
  private void addInputController(Raijin mainApp) {
	  rootLayout.setBottom(new InputController(mainApp));
  }
  
  // Methods to transfer to logic
  
  public void handleKeyPress(InputController inputController,
		  KeyCode key,
		  String userInput) {
	  if (key==KeyCode.ENTER) {
		  inputController.clear();
		  handleEnterPress(inputController, userInput);
	  }
  }
  
  private void handleEnterPress(InputController inputController, String userInput) {
	  String response = logic.executeCommand(userInput).getFeedback();
	  if (response.equals("Exiting")) {
		  System.exit(0);
	  } else {
		  inputController.setFeedback(response);
	  } 
  }
}
