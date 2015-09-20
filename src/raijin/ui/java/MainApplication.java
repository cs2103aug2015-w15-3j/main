package raijin.ui.java;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class MainApplication extends Application {
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
    
    addDisplayBar(this);
    addFeedbackBar(this);
    addCommandInputBar(this);
  }

  private void initPrimaryStage(Stage stage) {
	this.stage = stage;
	this.stage.setTitle("Welcome to Raijin");
    this.stage.setScene(new Scene(rootLayout));
    this.stage.show();
  }

  private void initRootLayout() {
	FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("view/Scene.fxml"));
	try {
		rootLayout = loader.load();
	} catch (IOException e) {
		e.printStackTrace();
	}
  }
 
  private void addDisplayBar(MainApplication mainApp) {
	  rootLayout.setCenter(new DisplayBar());
  }
  
  private void addFeedbackBar(MainApplication mainApp) {
	  rootLayout.setBottom(new FeedbackBar());
  }
  
  private void addCommandInputBar(MainApplication mainApp) {
	  rootLayout.setBottom(new CommandInputBar());
  }
}
