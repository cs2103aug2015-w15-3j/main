package raijin.ui;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DisplayController extends Application {
	
	Label test; //to be removed
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		test = new Label("test"); //to be removed
		stage.show();
	}

}
