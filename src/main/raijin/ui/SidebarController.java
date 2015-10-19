package raijin.ui;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import raijin.logic.api.Logic;

public class SidebarController extends BorderPane {

	@FXML
	private Button today;
	
	@FXML
	private Button tomorrow;
	
	@FXML
	private Button nextWeek;
	
	@FXML
	private Button projects;
	
	@FXML
	private Button completed;
	
	private static final String SIDEBAR_LAYOUT_FXML = "resource/layout/SidebarController.fxml";
	private Logic logic;
	
	public SidebarController(Logic logic) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(SIDEBAR_LAYOUT_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.logic = logic;
	}
	
	@FXML
	protected void handleTodayButtonAction(ActionEvent event) {
		logic.executeCommand("display p");
	}
	
	@FXML
	protected void handleTomorrowButtonAction(ActionEvent event) {
		
	}
	
	@FXML
	protected void handleNextWeekButtonAction(ActionEvent event) {
		
	}
	
	@FXML
	protected void handleProjectsButtonAction(ActionEvent event) {
		
	}
	
	@FXML
	protected void handleCompletedButtonAction(ActionEvent event) {
		logic.executeCommand("display c");
	}
	
}
