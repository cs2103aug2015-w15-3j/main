package raijin.common.utils;

import static org.junit.Assert.*;

import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Application;
import javafx.stage.Stage;
import raijin.common.datatypes.Task;
import raijin.logic.command.DisplayCommandRunnerTest.AsNonApp;

public class TaskPaneTest {
	
	public static class AsNonApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        // noop
	    }
	}

	@BeforeClass
	public static void initJFX() {
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(AsNonApp.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	}
	


	@Test
	public void test_retrieveTags() {
		TaskPane taskpane = new TaskPane();
		Task task = new Task("test task", 1);
		TreeSet<String> tags = new TreeSet<String>();
		tags.add("apple");
		tags.add("banana");
		task.addTags(tags);
		
		String expected = "apple, banana";
		assertEquals(expected, taskpane.retrieveTags(task));
	}

}
