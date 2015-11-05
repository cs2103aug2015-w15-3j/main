//@@author A0130720Y

package raijin.logic.command;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import raijin.common.datatypes.DateTime;

public class DisplayCommandRunnerTest {
	
	DisplayCommandRunner dcr = new DisplayCommandRunner();
	DateTime dateNowInDateTime = new DateTime("03/10/2015");

	/* This is a test case for if a task's start date is before the queried date and has no end date */
	@Test
	public void test_isRelevantDate_taskIsBefore() {
		
		DateTime testTaskDate = new DateTime("02/10/2015");
		
		assertEquals(false, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}
	
	/* This is a test case for if a task's start date is before the queried date, and 
	 * task's end date is after the queried date */
	@Test
	public void test_isRelevantDate_taskIsDuring() {
		
		DateTime testTaskDate = new DateTime("01/10/2015", "1300", "07/10/2015", "1400");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
		
	}
	
	/* This is a test case for if a task's start date is on the queried date */
	
	@Test
	public void test_isRelevantDate_taskIsOn() {
		
		DateTime testTaskDate = new DateTime("03/10/2015", "1300", "07/10/2015", "1400");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
		
	}

}
