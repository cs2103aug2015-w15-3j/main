package raijin.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.subscribers.MainSubscriber;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.stage.WindowEvent;

import raijin.common.datatypes.Constants;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.Logic;
import raijin.storage.api.Session;

public class Raijin extends Application {
	private static final String ROOT_LAYOUT_FXML_LOCATION = "resource/layout/RootLayout.fxml";
	private static final String INTRO_LAYOUT_FXML_LOCATION = "resource/layout/IntroLayout.fxml";
	private static final String TRAY_ICON_LOCATION = "resource/styles/raijin2.png";
	
	private static final String NO_DIRECTORY_SELECTED_FEEDBACK = "I'm sorry! You have not selected "
															+ "a directory yet. Please try again!";
	private BorderPane rootLayout, introLayout;
	private Stage stage;
	private Logic logic;
	private IntroController introController;
	private EventBus eventbus = RaijinEventBus.getEventBus();
	
	public static void main(String[] args) {
	  launch(args);
	}
	
  @Override
  public void start(Stage stage) throws Exception {
    /*Adding fxml */
    initPrimaryStage(stage);
    initLogic();
    decideScene();
    makeTray(stage); // listen out for any ctrl-h events
    Platform.setImplicitExit(false);
    handleMinimiseEvent();
    
    this.stage.show();
    
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
      
      this.stage.setScene(new Scene(rootLayout));
      ((InputController) rootLayout.getBottom()).getCommandBar().requestFocus();
      
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
  
  //
  // Methods to transfer to logic
  //
  
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

  //
  // Methods for Eventbus
  //

  public void handleMinimiseEvent() {
	  MainSubscriber<KeyPressEvent> activateHideSubscriber = new MainSubscriber<
			  KeyPressEvent>(eventbus) {

	      @Subscribe
	      @Override
	      public void handleEvent(KeyPressEvent event) {
	        if (Constants.KEY_HIDEUNHIDE.match(event.keyEvent)) {
	        	  hide(stage);
	        }};
	  };
  }
  
  //
  // Setting up Activate and Hide
  public void makeTray(final Stage stage) {
	  if (!SystemTray.isSupported()) {
		  System.out.println("Looks like you don't have System Tray on your Operating System!:(");
	  }
	  
	  final SystemTray tray = SystemTray.getSystemTray();
	  
	  final ActionListener closeListener = new ActionListener() {
		  @Override
		  public void actionPerformed(java.awt.event.ActionEvent e) {
			  System.exit(0);
		  }
	  };
	  
	  ActionListener showListener = new ActionListener() {
		  @Override
		  public void actionPerformed(java.awt.event.ActionEvent e) {
			  Platform.runLater(new Runnable() {
				  @Override
				  public void run() {
					  stage.show();
				  }
			  });
		  }
	  };
	  
	  PopupMenu popup = new PopupMenu();

      MenuItem showItem = new MenuItem("Show");
      showItem.addActionListener(showListener);
      popup.add(showItem);

      MenuItem closeItem = new MenuItem("Close");
      closeItem.addActionListener(closeListener);
      popup.add(closeItem);
      
      final TrayIcon trayIcon = new TrayIcon(createImage(TRAY_ICON_LOCATION), "Raijin.java", popup);
	  trayIcon.setImageAutoSize(true);
      trayIcon.addActionListener(showListener);
      
      try {
		  tray.add(trayIcon);
	  } catch (AWTException e) {
		  System.out.println("TrayIcon could not be added.");
	  }
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
	  
	  if(imageUrl == null) {
		  System.err.println("no image found: " + path);
		  return null;
	  } else {
		  return (new ImageIcon(imageUrl)).getImage();
	  }
  }
}