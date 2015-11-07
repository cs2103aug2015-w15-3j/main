//@@author A0129650E

package raijin.ui;

import java.awt.AWTException;
import java.awt.Image;

import javafx.scene.image.*;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetFailureEvent;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.logic.api.Logic;

public class Raijin extends Application implements NativeKeyListener {
  
  //===========================================
  // Variables used in Raijin
  //===========================================
	
  //location of files used
  private static final String ROOT_LAYOUT_FXML_LOCATION = "resource/layout/RootLayout.fxml";
  private static final String TRAY_ICON_LOCATION = "resource/styles/raijin2.png";
  private static final String CSS_LOCATION = "resource/styles/RaijinStyle.css";
  private static final String CSS_LOCATION_HELP = "resource/styles/Help.css";
  /*
   * code eventually unused because images were not clear;
  private static final String HELP_ADD = "resource/styles/helpAdd.jpg";
  private static final String HELP_DELETE = "resource/styles/helpDelete.jpg";
  private static final String HELP_DISPLAY = "resource/styles/helpDisplay.jpg";
  private static final String HELP_DONE = "resource/styles/helpDone.jpg";
  private static final String HELP_EDIT = "resource/styles/helpEdit.jpg";
  private static final String HELP_SEARCH  = "resource/styles/helpSearch.jpg";
  private static final String HELP_SET = "resource/styles/helpSet.jpg";
  private static final String HELP_UNDOREDO = "resource/styles/helpUndoRedo.jpg";
  private static final String KEYBOARD_SHORTCUTS = "resource/styles/keyboardShort.jpg";
  private static final String[] helpImg = {"resource/styles/helpAdd.jpg",
		  "resource/styles/helpDelete.jpg",
		  "resource/styles/helpDisplay.jpg",
		  "resource/styles/helpDone.jpg",
		  "resource/styles/helpEdit.jpg",
		  "resource/styles/helpSearch.jpg",
		  "resource/styles/helpSet.jpg",
		  "resource/styles/helpUndoRedo.jpg",
		  "resource/styles/keyboardShort.jpg"};	
  */
  
  private static final double MIN_WIDTH = 550.0; // width values for stage
  private static final double MIN_HEIGTH = 650.0; // height values for stage
  private double dragX = 0; // values for dragging of help pane
  private double dragY = 0;
  
  static boolean isVisible = false; // boolean that checks if Raijin is currently being shown
  static boolean isHelpOn = false; // boolean that checks if help is currently being shown
  
  private SystemTray tray;
  final TrayIcon trayIcon = new TrayIcon(createImage(TRAY_ICON_LOCATION), "Raijin.java", null);
  
  BorderPane rootLayout, introLayout, inputController, displayController,
      sidebarController;
  
  private HBox hBox;
  private static Stage stage;
private Stage helpStage;
  private Logic logic;
  private RaijinEventBus eventbus = RaijinEventBus.getInstance();
  
  @FXML
  private TextFlow helpBar = new TextFlow();
  @FXML
  ListView<TextFlow> helpContain;
  
  //===========================================
  //Start of Raijin UI Methods
  //===========================================
  public static void main(String[] args) {
    launch(args);
  }

  public static Stage getStage() {
    return stage;
  }

  @Override
  public void start(Stage stage) throws Exception {
    /* Adding fxml */
	initPrimaryStage(stage);
    initLogic();
    setUpVariables();
    decideScene();
    makeTray(stage); // listen out for any ctrl-h events
    Platform.setImplicitExit(false);

    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(this);
    turnOffLogger(); // Turn off JNativeHook Logging

    changeToMinimisedView();

    this.stage.show();
    this.isVisible = true;

    /*
     * this.stage.widthProperty().greaterThan(add750).addListener((obs, oldValue, newValue) -> { if
     * (!newValue) { changeToMinimisedView(); } else { changeToMaximisedView(); } });
     */

    handleMaximized(); // Handle simple & advanced mode
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
      public void handle(WindowEvent we) {
        logic.executeCommand("exit");
        System.exit(0);
      }
    });

  }

  private void initPrimaryStage(Stage stage) {
    this.stage = stage;
    this.stage.setTitle("Welcome to Raijin");
    this.stage.setMinWidth(MIN_WIDTH);
    this.stage.setMinHeight(MIN_HEIGTH);
  }

  private void initLogic() throws FileNotFoundException {
    logic = new Logic();
  }

  private void setUpVariables() throws IOException {
	this.hBox = new HBox();
	hBox.setMaxWidth(Double.MAX_VALUE);
	    
	this.inputController = new InputController(this);
	((InputController) inputController).timeSlot.setVisible(false);
	    
	this.displayController = new DisplayController();
	this.sidebarController = new SidebarController(this.logic);
	    
	String CSS = getClass().getResource(CSS_LOCATION).toExternalForm();
	displayController.getStylesheets().add(CSS);
  }
  
  public void decideScene() {
	initRootLayout();
	changeToMinimisedView();
	this.stage.setScene(new Scene(rootLayout));
  }
  
  private void initRootLayout() {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT_FXML_LOCATION));
	try {
	  rootLayout = loader.load();
	} catch (IOException e) {
	  e.printStackTrace();
	}
 }
  
  public void changeToMinimisedView() {
    rootLayout.setCenter(displayController);
    rootLayout.setBottom(inputController);
    setFocusAtCommandBar();
  }

  private void changeToMaximisedView() {
    hBox.getChildren().clear();
    hBox.getChildren().addAll(sidebarController, displayController);
    rootLayout.setCenter(hBox);
    rootLayout.setBottom(inputController);
    setFocusAtCommandBar();
  }

  // =========================================
  // Methods to transfer to logic
  // =========================================

  public void handleKeyPress(InputController inputController, KeyCode key, String userInput) {
    if (key == KeyCode.ENTER) {
      inputController.updateCommandHistory(userInput); // Update list of user input
      inputController.clear();
      handleEnterPress(inputController, userInput);
    } else if (key == KeyCode.ESCAPE) {
      logic.loadCustomData(inputController.inputCommandBar.getText().trim());
    }
  }

  private void handleEnterPress(InputController inputController, String userInput) {
    Status result = logic.executeCommand(userInput);
    Boolean isSuccessful = result.isSuccess();
    String response = result.getFeedback();
    
    if (response.equals(Constants.FEEDBACK_EXIT_SUCCESS)) {
      System.exit(0);
    } else if (response.equals(Constants.FEEDBACK_HELP_COMMAND) && !isHelpOn) {
      isHelpOn = true;
      bringUpHelpSection();	
  	} else if (!isSuccessful) {
      eventbus.post(new SetFailureEvent(response));
    } else {
      eventbus.post(new SetFeedbackEvent(response));
    }
  }
  
  private void bringUpHelpSection() {
	  helpStage = new Stage();	  
	  StackPane helpBase = new StackPane();
	  initiateHelpBase(helpBase);
	  
	  helpContain = new ListView<TextFlow>();
	  
	  setUpHelpStage();
	  
	  ArrayList<String> allCommand = new ArrayList<String>();
	  addToAllCommandArray(allCommand);
	  
	  ArrayList<String> allDescription = new ArrayList<String>();
	  addToAllDescription(allDescription);

	  addAllMessagesToHelpDisplay(allCommand, allDescription);
	  
	  helpContain.setOnMouseDragged(new EventHandler<MouseEvent>() {
	         public void handle (MouseEvent me) {   
	        	 helpStage.setX(me.getScreenX() - dragX);
	             helpStage.setY(me.getScreenY() - dragY);
	         }
	      });
	  
	  helpContain.setOnMousePressed(new EventHandler<MouseEvent>() {
	  		public void handle (MouseEvent me) {
            dragX = me.getScreenX() - helpStage.getX();
            dragY = me.getScreenY() - helpStage.getY();
         }
      });
	  
	  Scene dialogScene = new Scene(helpBase, 800, 600);
	  
	  helpBase.getChildren().add(helpContain);
      
	  helpStage.setScene(dialogScene);
	  helpStage.show();
  }

  private void initiateHelpBase(StackPane helpBase) {
	  String CSS_HELP = getClass().getResource(CSS_LOCATION_HELP).toExternalForm();
	  helpBase.getStylesheets().add(CSS_HELP);
  }

  private void addAllMessagesToHelpDisplay(ArrayList<String> allCommand, 
		  			                       ArrayList<String> allDescription) {
	HelpMessage helpHeader = new HelpMessage("Help\n");
	helpBar.getChildren().addAll(helpHeader.helpMessage);
	  
	  for (int cmdAndDescripFormatId = 1; 
		       cmdAndDescripFormatId < allCommand.size(); 
			   cmdAndDescripFormatId++) {
		  if (allCommand.get(cmdAndDescripFormatId).equals("Keyboard Shortcuts")) {
			  HelpMessage keyboardHeader = new HelpMessage("Keyboard Shortcuts\n");
			  helpBar.getChildren().addAll(keyboardHeader.helpMessage);
		  } else {
			  HelpMessage fullMessage = new HelpMessage(allCommand.get(cmdAndDescripFormatId), 
					  									allDescription.get(cmdAndDescripFormatId));
			  helpBar.getChildren().addAll(fullMessage.helpMessage);  
		  }
     }
	  
	  helpContain.setItems(FXCollections.observableArrayList(helpBar));
  }

  private void setUpHelpStage() {
	  helpStage.initStyle(StageStyle.TRANSPARENT);
	  helpStage.initModality(Modality.NONE);
	  helpStage.initOwner(stage);
  }

  private void addToAllDescription(ArrayList<String> allDescription) {
	  allDescription.add(Constants.ADD_FLOATING_DESC + "\n\n");
	  allDescription.add(Constants.ADD_SPECIFIC_DESC + "\n\n");
	  allDescription.add(Constants.ADD_EVENT_SAME_DATE_DESC + "\n\n");
	  allDescription.add(Constants.ADD_EVENT_DIFFERENT_DATE_DESC + "\n\n");
	  allDescription.add(Constants.ADD_BATCH_DESC + "\n\n");
	  allDescription.add(Constants.EDIT_TASK_DESC + "\n\n");
	  allDescription.add(Constants.DISPLAY_DESC + "\n\n");
	  allDescription.add(Constants.DONE_DESC + "\n\n");
	  allDescription.add(Constants.DELETE_DESC + "\n\n");
	  allDescription.add(Constants.UNDO_DESC + "\n\n");
	  allDescription.add(Constants.REDO_DESC + "\n\n");
	  allDescription.add(Constants.SEARCH_DESC + "\n\n");
	  allDescription.add(Constants.SET_DESC + "\n\n");
	 
	  allDescription.add("  \n");
	  
	  allDescription.add(Constants.KEY_UNDO_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_REDO_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_CLEAR_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_COPY_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_CUT_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_PASTE_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_TAB_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_VIEW_DOWN_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_VIEW_UP_HELP_DESC + "\n\n");
	  allDescription.add(Constants.KEY_MINMAX_HELP_DESC + "\n\n");
	  allDescription.add(Constants.SCROLL_UP_HELP_DESC + "\n\n");
	  allDescription.add(Constants.SCROLL_DOWN_HELP_DESC + "\n\n");
  }

  private void addToAllCommandArray(ArrayList<String> allCommand) {
	  allCommand.add(Constants.ADD_FLOATING);
	  allCommand.add(Constants.ADD_SPECIFIC);
	  allCommand.add(Constants.ADD_EVENT_SAME_DATE);
	  allCommand.add(Constants.ADD_EVENT_DIFFERENT_DATE);
	  allCommand.add(Constants.ADD_BATCH);
	  allCommand.add(Constants.EDIT_TASK);
	  allCommand.add(Constants.DISPLAY);
	  allCommand.add(Constants.DONE);
	  allCommand.add(Constants.DELETE);
	  allCommand.add(Constants.UNDO);
	  allCommand.add(Constants.REDO);
	  allCommand.add(Constants.SEARCH);
	  allCommand.add(Constants.SET);
	 
	  allCommand.add("Keyboard Shortcuts");
	  
	  allCommand.add(Constants.KEY_UNDO_HELP);
	  allCommand.add(Constants.KEY_REDO_HELP);
	  allCommand.add(Constants.KEY_CLEAR_HELP);
	  allCommand.add(Constants.KEY_COPY_HELP);
	  allCommand.add(Constants.KEY_CUT_HELP);
	  allCommand.add(Constants.KEY_PASTE_HELP);
	  allCommand.add(Constants.KEY_TAB_HELP);
	  allCommand.add(Constants.KEY_VIEW_DOWN_HELP);
	  allCommand.add(Constants.KEY_VIEW_UP_HELP);
	  allCommand.add(Constants.KEY_MINMAX_HELP);
	  allCommand.add(Constants.SCROLL_UP_HELP);
	  allCommand.add(Constants.SCROLL_DOWN_HELP);
  }
  
  // ============================================
  // Methods for Eventbus
  // ============================================

  // Setting up Activate and Hide
  public void makeTray(final Stage stage) {
	  if (!SystemTray.isSupported()) {
		Status result = new Status("Looks like you don't have System Tray on your Operating System!:(",
									false);
	    String response = result.getFeedback();   
	    eventbus.post(new SetFailureEvent(response));
	  }

	  this.tray = SystemTray.getSystemTray();

	  trayIcon.setImageAutoSize(true);
  }

  private void hide(final Stage stage) {
	  Platform.runLater(new Runnable() {
		  @Override
	      public void run() {
	        if (SystemTray.isSupported()) {
	          stage.hide();
	        } else {
	          System.exit(0);
	        }
	      }
	  });
  }

  protected static Image createImage(String path) {
	    URL imageUrl = Raijin.class.getResource(path);
	
	    if (imageUrl == null) {
	      System.err.println("no image found: " + path);
	      return null;
	    } else {
	      return (new ImageIcon(imageUrl)).getImage();
	    }
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent arg0) {
	    // Enable user to start typing whenever the application is open
	    if (isVisible) {
	      Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	          setFocusAtCommandBar();
	        }
	      });
	    }

	    boolean isCtrlSpacePressed =
	        arg0.getKeyCode() == NativeKeyEvent.VC_SPACE
	            && NativeInputEvent.getModifiersText(arg0.getModifiers()).equals("Ctrl");
	
	    boolean isCtrlHPressed =
	        arg0.getKeyCode() == NativeKeyEvent.VC_H
	            && NativeInputEvent.getModifiersText(arg0.getModifiers()).equals("Ctrl");
	    
	    if (isCtrlSpacePressed && isVisible) { // for hide feature: to hide the program
	      hide(stage);
	      try {
	        tray.add(trayIcon);
	      } catch (AWTException e) {
	        System.out.println("TrayIcon could not be added.");
	      }
	      isVisible = false;
	    } else if (isCtrlSpacePressed) { // for hide feature: to bring up the program
	      Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	          stage.show();
	          tray.remove(trayIcon);
	        }
	      });
	      isVisible = true;
	    } else if(isCtrlHPressed && !isHelpOn) { // for help feature: to bring up the help menu
	    	isHelpOn = true;
	    	Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	bringUpHelpSection();
	            }
	          });
	    } else if(isCtrlHPressed && isHelpOn) { // for help feature: to hide the help menu
	    	isHelpOn = false;
	    	hide(helpStage);
	    }
  }
 
  void setFocusAtCommandBar() {
    ((InputController) rootLayout.getBottom()).getCommandBar().requestFocus();
  }
  
  //compulsory & unremovable methods from jnativehook library
  @Override
  public void nativeKeyReleased(NativeKeyEvent arg0) {

  }
  
  //compulsory & unremovable methods from jnativehook library
  @Override
  public void nativeKeyTyped(NativeKeyEvent arg0) {
    // TODO Auto-generated method stub

  }

  void turnOffLogger() { //turn off jnativehook logger
    Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setLevel(Level.OFF);
  }

  void handleMaximized() {
    stage.maximizedProperty().addListener(new ChangeListener<Boolean>() {

      @Override
      public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
        if (t1.booleanValue()) { // If maximized
          changeToMaximisedView();
        } else {
          changeToMinimisedView();
        }
      }
    });
  }

  //Toggle between minimize and maximize mode
  public void resizeWindow() {
    if (stage.isMaximized()) {
      stage.setMaximized(false);
    } else {
      stage.setMaximized(true);
    }
  }
  
  public void testTwoImage() {
    javafx.scene.image.Image fximg = new javafx.scene.image.Image("nonsense");
  }
}
