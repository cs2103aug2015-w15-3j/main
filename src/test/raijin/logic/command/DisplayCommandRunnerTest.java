package raijin.logic.command;

import static org.junit.Assert.*;

import org.junit.Test;

import raijin.common.datatypes.DateTime;

public class DisplayCommandRunnerTest {

	@Test
	public void test_isRelevantDate() {
		DisplayCommandRunner dcr = new DisplayCommandRunner();
		
		DateTime dateNowInDateTime = new DateTime("03/10/2015");
		
		// These tests are only valid on 3rd Oct, testing afterwards require change of dates
		DateTime testTaskDate = new DateTime("02/10/2015");
		DateTime testTaskDate2 = new DateTime("01/10/2015", "1:00", "07/10/2015", "2:00");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}

}
