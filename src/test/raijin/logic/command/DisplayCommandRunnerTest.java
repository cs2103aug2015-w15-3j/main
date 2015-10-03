package raijin.logic.command;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import raijin.common.datatypes.DateTime;

public class DisplayCommandRunnerTest {
	DisplayCommandRunner dcr = new DisplayCommandRunner();

	@Test
	public void test_isRelevantDate() {
		DateTime dateNowInDateTime = new DateTime("03/10");
		
		// These tests are only valid on 3rd Oct, testing afterwards require change of dates
		DateTime testTaskDate = new DateTime("02/10");
		DateTime testTaskDate2 = new DateTime("01/10", "1:00", "07/10", "2:00");
		
		assertEquals(true, dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}

}
