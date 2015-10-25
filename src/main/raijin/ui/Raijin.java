package raijin.ui;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.eventbus.events.SetFeedbackEvent;
import raijin.common.eventbus.events.SetFailureEvent;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Status;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.Logic;
import raijin.storage.api.Session;

public class Raijin extends Application implements NativeKeyListener {
  private static final String ROOT_LAYOUT_FXML_LOCATION = "resource/layout/RootLayout.fxml";
  private static final String INTRO_LAYOUT_FXML_LOCATION = "resource/layout/IntroLayout.fxml";
  private static final String TRAY_ICON_LOCATION = "resource/styles/raijin2.png";
  private static final String CSS_LOCATION = "resource/styles/RaijinStyle.css";
  private static final double MIN_WIDTH = 550.0;
  private static final double MIN_HEIGTH = 650.0;

  private static final String NO_DIRECTORY_SELECTED_FEEDBACK = "I'm sorry! You have not selected "
      + "a directory yet. Please try again!";
  private boolean isVisible = false;
  private BorderPane rootLayout, introLayout, inputController, displayController,
      sidebarController;
  private HBox hBox;
  private Stage stage;
  private Logic logic;
  private IntroController introController;
  private EventBus eventbus = RaijinEventBus.getEventBus();
  private SystemTray tray;
  final TrayIcon trayIcon = new TrayIcon(createImage(TRAY_ICON_LOCATION), "Raijin.java", null);


  public static void main(String[] args) {
    launch(args);
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
     * this.stage.widthProperty().greaterThan(750).addListener((obs, oldValue, newValue) -> { if
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

    /*
     * if (logic.isFirstTime()) { introController = new IntroController(this, logic, stage);
     * initIntroLayout();
     * 
     * this.stage.setScene(new Scene(introLayout)); } else {
     */
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
    if (response.equals("Exiting")) {
      System.exit(0);
    } else if (!isSuccessful) {
      System.out.println("yeaup");
      eventbus.post(new SetFailureEvent(response));
    } else {
      eventbus.post(new SetFeedbackEvent(response));
    }
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

    boolean isCtrlHPressed =
        arg0.getKeyCode() == NativeKeyEvent.VC_SPACE
            && NativeInputEvent.getModifiersText(arg0.getModifiers()).equals("Ctrl");

    if (isCtrlHPressed && isVisible) {
      hide(stage);
      try {
        tray.add(trayIcon);
      } catch (AWTException e) {
        System.out.println("TrayIcon could not be added.");
      }
      isVisible = false;
    } else if (isCtrlHPressed) {
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          stage.show();
          tray.remove(trayIcon);
        }
      });
      isVisible = true;
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
