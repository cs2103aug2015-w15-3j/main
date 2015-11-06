//@@author A0130720Y

package raijin.logic.command;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Test;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.IDManager;
import raijin.logic.parser.ParsedInput;

public class DisplayCommandRunnerTest {
	
	DisplayCommandRunner dcr = new DisplayCommandRunner();
	private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE);
	
	DateTime dateNowInDateTime = new DateTime("03/10/2015");
	DateTime rangeOfDateInDateTime = new DateTime(LocalDate.parse("01/10/2015", dateFormatter), 
												  LocalDate.parse("09/10/2015", dateFormatter));
	
	/* This is a test case for if a task's start date is before the queried date and has no end date */
	@Test
	public void test_isRelevantDate_taskIsBefore() {
		DateTime testTaskDate = new DateTime("02/10/2015");
		assertFalse(dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}
	
	/* This is a test case for if a task's start date is before the queried date, and 
	 * task's end date is after the queried date */
	@Test
	public void test_isRelevantDate_taskIsDuring() {
		DateTime testTaskDate = new DateTime("01/10/2015", "1300", "07/10/2015", "1400");
		assertTrue(dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
		
	}
	
	/* This is a test case for if a task's start date is on the queried date */
	@Test
	public void test_isRelevantDate_taskIsOn() {
		DateTime testTaskDate = new DateTime("03/10/2015", "1300", "07/10/2015", "1400");
		assertTrue(dcr.isRelevantDate(dateNowInDateTime, testTaskDate));
	}
	
	/* This is a test case for if a task is an event, end date is before the queried end date 
	 * note: the queried date is now a range of dates */
	@Test
	public void test_isRelevantDate_taskIsAnEvent() {
		ParsedInput input1 = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
		        name("submit op1").dateTime(new DateTime(LocalDate.parse("25/09/2015", dateFormatter), 
						  LocalDate.parse("09/10/2015", dateFormatter))).createParsedInput();
		Task task = new Task("submit op1", IDManager.getIdManager().getId(), input1);
		
		assertTrue(dcr.isRelevantDate(rangeOfDateInDateTime, task.getDateTime()));
	}
	
	/* Test if a task is overdue */
	@Test
	public void test_isOverdue() {
		ParsedInput input1 = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
		        name("submit op1").dateTime(new DateTime("02/10/2015")).createParsedInput();
		ParsedInput input2 = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD).
		        name("submit op2").dateTime(new DateTime("02/10/2070")).createParsedInput();
		Task overdueTask = new Task("submit op1", IDManager.getIdManager().getId(), input1);
		Task notOverdueTask = new Task("submit op2", IDManager.getIdManager().getId(), input2);
		
		assertTrue(dcr.isOverdue(overdueTask));
		assertFalse(dcr.isOverdue(notOverdueTask));
		
	}
	
	/* Tests if a task is already in a list */
	@Test
	public void test_isAlreadyInList() {
		Task task = new Task ("test", IDManager.getIdManager().getId());
		ArrayList<Task> list = new ArrayList<Task>();
		
		list.add(task);
		assertTrue(dcr.isAlreadyInList(task, list));
		
		list.clear();
		assertFalse(dcr.isAlreadyInList(task, list));
	}

}
