package raijin.logic.command;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import javafx.application.Application;
import javafx.stage.Stage;
import raijin.common.datatypes.DateTime;

public class DisplayCommandRunnerTest {
	
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
	public void test_isRelevantDate() {
		DisplayCommandRunner dcr = new DisplayCommandRunner();
		
		DateTime dateNowInDateTime = new DateTime("03/10/2015");
		
		// These tests are only valid on 3rd Oct, testing afterwards require change of dates
		DateTime testTaskDate = new DateTime("02/10/2015");
		DateTime testTaskDate2 = new DateTime("01/10/2015", "1300", "07/10/2015", "1400");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}

}
