package raijin.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

  @Override
  public void start(Stage stage) throws Exception {
    /*Adding fxml */
    FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("view/Scene.fxml"));
    StackPane root = new StackPane();
    Scene scene = new Scene(root,300,250);
    stage.setTitle("Welcome to Raijin");
    stage.setScene(scene);
    stage.show();
  }
  
  public static void main(String[] args) {
    launch(args);
  }

}
