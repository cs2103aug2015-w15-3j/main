package raijin.ui;

import com.google.common.eventbus.EventBus;

import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.SetFailureEvent;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.logic.api.Logic;

public class Raijin extends Application implements NativeKeyListener {
  private static final String ROOT_LAYOUT_FXML_LOCATION = "resource/layout/RootLayout.fxml";
  private static final String INTRO_LAYOUT_FXML_LOCATION = "resource/layout/IntroLayout.fxml";
  private static final String TRAY_ICON_LOCATION = "resource/styles/raijin2.png";
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
  private static final String CSS_LOCATION = "resource/styles/RaijinStyle.css";
  private static final double MIN_WIDTH = 550.0;
  private static final double MIN_HEIGTH = 650.0;

  private static final String NO_DIRECTORY_SELECTED_FEEDBACK = "I'm sorry! You have not selected "
      + "a directory yet. Please try again!";
  
  private boolean isVisible = false;
  private boolean isHelpOn = false;
  BorderPane rootLayout, introLayout, inputController, displayController,
      sidebarController;
  private double dragX = 0;
  private double dragY = 0;
  private HBox hBox;
  private ImageView[] helpImg_ImageView = new ImageView[9];
  private static Stage stage, helpStage;
  private Logic logic;
  private IntroController introController;
  private EventBus eventbus = RaijinEventBus.getEventBus();
  private SystemTray tray;
  final TrayIcon trayIcon = new TrayIcon(createImage(TRAY_ICON_LOCATION), "Raijin.java", null);


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

  private void initRootLayout() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT_FXML_LOCATION));
    try {
      rootLayout = loader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String CSS = getClass().getResource(CSS_LOCATION).toExternalForm();
    rootLayout.getStylesheets().add(CSS);
  }

  private void initLogic() throws FileNotFoundException {
    logic = new Logic();
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

  public void decideScene() {
    initRootLayout();
    changeToMinimisedView();
    this.stage.setScene(new Scene(rootLayout));
  }

  private void setUpVariables() throws IOException {
    this.hBox = new HBox();
    hBox.setMaxWidth(Double.MAX_VALUE);
    this.inputController = new InputController(this);
    this.displayController = new DisplayController();
    this.sidebarController = new SidebarController(this.logic);
    ((InputController) inputController).timeSlot.setVisible(false);
  }

  //
  // Methods to transfer to logic
  //

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
	  helpStage.initStyle(StageStyle.TRANSPARENT);
	  
	  helpStage.initModality(Modality.NONE);
	  helpStage.initOwner(stage);
	  
	  StackPane helpRoot = new StackPane();
	  ScrollPane innerStage = new ScrollPane();
	  
	  VBox dialogVbox = new VBox(20);
	  
	  helpRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
	  		public void handle (MouseEvent me) {
            dragX = me.getScreenX() - helpStage.getX();
            dragY = me.getScreenY() - helpStage.getY();
         }
      });
      
      helpRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
         public void handle (MouseEvent me) {   
             helpStage.setX(me.getScreenX() - dragX);
             helpStage.setY(me.getScreenY() - dragY);
         }
      });
      
      for(int i = 0; i < helpImg_ImageView.length; i++) {
    	  helpImg_ImageView[i] = new ImageView(getClass().getResource(helpImg[i])
    	     .toString());
    	  helpImg_ImageView[i].setFitWidth(583);
    	  helpImg_ImageView[i].setPreserveRatio(true);
    	  dialogVbox.getChildren().add(helpImg_ImageView[i]);
      }
      
      /*
	  ImageView helpAdd = new ImageView(HELP_ADD);
	  ImageView helpDelete = new ImageView(HELP_DELETE);
	  ImageView helpDisplay = new ImageView(HELP_DISPLAY);
	  ImageView helpDone = new ImageView(HELP_DONE);
	  ImageView helpEdit = new ImageView(HELP_EDIT);
	  ImageView helpSearch = new ImageView(HELP_SEARCH);
	  ImageView helpSet = new ImageView(HELP_SET);
	  ImageView helpUndoRedo = new ImageView(HELP_UNDOREDO);
	  ImageView keyboardShort = new ImageView(KEYBOARD_SHORTCUTS);
	  
	  
	  dialogVbox.getChildren().addAll(helpAdd, helpDelete, helpDisplay,
			  						  helpDone, helpEdit, helpSearch,
			  						  helpSet, helpUndoRedo, keyboardShort);
	 */
	  innerStage.setContent(dialogVbox);
	  helpRoot.getChildren().add(innerStage);
	  
	  Scene dialogScene = new Scene(helpRoot, 600, 600);
	  helpStage.setScene(dialogScene);
	  helpStage.show();
  }
  //
  // Methods for Eventbus
  //

  //
  // Setting up Activate and Hide
  //
  public void makeTray(final Stage stage) {
    if (!SystemTray.isSupported()) {
      System.out.println("Looks like you don't have System Tray on your Operating System!:(");
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
    
    if (isCtrlSpacePressed && isVisible) {
      hide(stage);
      try {
        tray.add(trayIcon);
      } catch (AWTException e) {
        System.out.println("TrayIcon could not be added.");
      }
      isVisible = false;
    } else if (isCtrlSpacePressed) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          stage.show();
          tray.remove(trayIcon);
        }
      });
      isVisible = true;
    } else if(isCtrlHPressed && !isHelpOn) {
    	isHelpOn = true;
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	bringUpHelpSection();
            }
          });
    } else if(isCtrlHPressed && isHelpOn) {
    	isHelpOn = false;
    	hide(helpStage);
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent arg0) {

  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent arg0) {
    // TODO Auto-generated method stub

  }

  void turnOffLogger() {
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

  void setFocusAtCommandBar() {
    ((InputController) rootLayout.getBottom()).getCommandBar().requestFocus();
  }
  
  //Toggle between minimize and maximize mode
  public void resizeWindow() {
    if (stage.isMaximized()) {
      stage.setMaximized(false);
    } else {
      stage.setMaximized(true);
    }
  }
  
}
