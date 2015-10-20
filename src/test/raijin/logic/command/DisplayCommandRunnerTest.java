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
	
	/* Equivalence partitioning */

	/* This is a test case for if a task's start date is before the queried date and has no end date */
	@Test
	public void test_isRelevantDate_taskIsBefore() {
		DisplayCommandRunner dcr = new DisplayCommandRunner();
		
		DateTime dateNowInDateTime = new DateTime("03/10/2015");
		
		DateTime testTaskDate = new DateTime("02/10/2015");
		
		assertEquals(false, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}
	
	/* This is a test case for if a task's start date is before the queried date, and 
	 * task's end date is after the queried date */
	@Test
	public void test_isRelevantDate_taskIsDuring() {
		DisplayCommandRunner dcr = new DisplayCommandRunner();
		
		DateTime dateNowInDateTime = new DateTime("03/10/2015");
		
		DateTime testTaskDate = new DateTime("01/10/2015", "1300", "07/10/2015", "1400");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
		
	}
	
	/* This is a test case for if a task's start date is on the queried date */
	
	@Test
	public void test_isRelevantDate_taskIsOn() {
		DisplayCommandRunner dcr = new DisplayCommandRunner();
		
		DateTime dateNowInDateTime = new DateTime("03/10/2015");
		
		DateTime testTaskDate = new DateTime("03/10/2015", "1300", "07/10/2015", "1400");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
		
	}

}
