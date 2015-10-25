package raijin.logic.command;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.utils.EventBus;
import raijin.common.utils.filter.SortFilter;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class DisplayCommandRunner extends CommandRunner {

	private static final String TYPE_ALL = "a";   // display ALL PENDING
	private static final String TYPE_PENDING = "p";  // display PENDING (for today)
	private static final String TYPE_COMPLETED = "c";     // display COMPLETED
	private static final String TYPE_FLOATING = "f";  // display FLOATING
	private static final String TYPE_OVERDUE = "o";  // display OVERDUE

	private static final String FEEDBACK_DISPLAY = "Displaying: ";
	private static final String FEEDBACK_FLOATING = "floating tasks";
	private static final String FEEDBACK_ALL_PENDING = "all pending tasks";
	private static final String FEEDBACK_PENDING = "pending tasks";
	private static final String FEEDBACK_COMPLETED = "completed tasks";
	private static final String FEEDBACK_OVERDUE = "overdue tasks";

	private static final String MESSAGE_SUCCESS = "Success";
	private static final String MESSAGE_NO_PENDING = "You have no pending tasks!";
	private static final String MESSAGE_NO_COMPLETED = "You have no completed tasks!";
	private static final String MESSAGE_NO_FLOATING = "You have no pending floating tasks!";
	private static final String MESSAGE_NO_OVERDUE = "You have no overdue tasks! Well done!";

	private DateTime cmdDateTime;
	private DateTime taskDateTime;
	private LocalDate now;
	private LocalDate tomorrowDate;

	private ArrayList<Task> pending;
	private ArrayList<Task> completed;
	private ArrayList<Task> relevant;

	private EventBus eventBus = EventBus.getEventBus();
	private com.google.common.eventbus.EventBus eventbus = RaijinEventBus.getEventBus();

	final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy");

	public Status processCommand(ParsedInput cmd) {

		now = LocalDate.now();
		tomorrowDate = now.plusDays(1);

		retrieveLists();
		cmdDateTime = getQueriedDate(cmd);

		String dateForDisplay = cmdDateTime.getStartDate().format(dateFormatter);
		String message = "";
		String feedbackMessage = "";

		boolean isEmpty = true;

		switch (cmd.getDisplayOptions()) {

		case TYPE_PENDING:
			if (!cmdDateTime.getStartDate().isEqual(now)) {
				message = "Tasks pending for " 
						+ dateForDisplay;

				feedbackMessage = FEEDBACK_PENDING;
				for (Task currentTask : pending) {
					taskDateTime = currentTask.getDateTime();

					if (currentTask.getType() != Constants.TYPE_TASK.FLOATING && 
							isRelevantDate(cmdDateTime, taskDateTime)) {

						isEmpty = false;
						relevant.add(currentTask);
					}
				}

				if (isEmpty) {
					//eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
					eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
				} else {
					Collections.sort(relevant);
					//eventBus.setCurrentTasks(relevant);
					eventbus.post(new SetCurrentDisplayEvent(relevant, message));
				}
			} else {
				message = "Tasks pending for...";

				feedbackMessage = FEEDBACK_PENDING + " for today & tomorrow";
				ArrayList<Task> today = new ArrayList<Task>();
				ArrayList<Task> tomorrow = new ArrayList<Task>();
				
				boolean isTodayEmpty = true;
				
				// Getting today's tasks
				for (Task currentTask : pending) {
					taskDateTime = currentTask.getDateTime();

					if (currentTask.getType() != Constants.TYPE_TASK.FLOATING && 
							isRelevantDate(new DateTime(now, now), taskDateTime)) {

						isTodayEmpty = false;
						today.add(currentTask);
					}
				}
				
				if (!isTodayEmpty) {
					Collections.sort(today);
				}
				
				boolean isTomorrowEmpty = true;
				
				// Getting tomorrow's tasks
				for (Task currentTask : pending) {
					taskDateTime = currentTask.getDateTime();

					if (currentTask.getType() != Constants.TYPE_TASK.FLOATING && 
							isRelevantDate(new DateTime(tomorrowDate, tomorrowDate), taskDateTime)) {

						isTomorrowEmpty = false;
						tomorrow.add(currentTask);
					}
				}
				
				if (!isTomorrowEmpty) {
					Collections.sort(tomorrow);
					today.addAll(tomorrow);
				}
				
				ArrayList<Task> temp = new ArrayList<Task>(pending);
				Collections.sort(temp);
				for (Task task : today) {
					temp.remove(task);
				}
				int i = 0;
				for (int size=today.size(); size < 20 && i < temp.size(); size++) {
					today.add(temp.get(i++));
				}
				
				
				if (isTodayEmpty && isTomorrowEmpty) {
					eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
				} else {
					eventbus.post(new SetCurrentDisplayEvent(today, message));
				}
				
			}

			break;

		case TYPE_ALL:
			feedbackMessage = FEEDBACK_ALL_PENDING;
			message = "All pending tasks";

			if (pending.isEmpty()) {
				//eventBus.setCurrentTasks(MESSAGE_NO_PENDING);
				eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
			} else {
				relevant = new ArrayList<Task>(pending);
				Collections.sort(relevant);
				//eventBus.setCurrentTasks(relevant);
				eventbus.post(new SetCurrentDisplayEvent(relevant, message));
			}

			break;

		case TYPE_FLOATING:
			feedbackMessage = FEEDBACK_FLOATING;
			message = "All floating tasks";

			for (Task currentTask : pending) {

				if (currentTask.getType().equals(Constants.TYPE_TASK.FLOATING)) {
					relevant.add(currentTask);
					isEmpty = false;
				}
			}

			if (isEmpty) {
				//eventBus.setCurrentTasks(MESSAGE_NO_FLOATING);
				eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_FLOATING, message));
			} else {
				//eventBus.setCurrentTasks(relevant);
			    relevant = (ArrayList<Task>) new SortFilter(Constants.SORT_CRITERIA.PRIORITY).filter(relevant);
				eventbus.post(new SetCurrentDisplayEvent(relevant, message));
			}

			break;

		case TYPE_COMPLETED:
			feedbackMessage = FEEDBACK_COMPLETED;
			message = "Tasks completed as of today, " + dateForDisplay;

			for (Task currentTask : completed) {
				relevant.add(currentTask);
				isEmpty = false;
			}

			if (isEmpty) {
				//eventBus.setCurrentTasks(MESSAGE_NO_COMPLETED);
				eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_COMPLETED, message));
			} else {
				Collections.sort(relevant);
				//eventBus.setCurrentTasks(relevant);
				eventbus.post(new SetCurrentDisplayEvent(relevant, message));
			}


			break;

		case TYPE_OVERDUE:
			feedbackMessage = FEEDBACK_OVERDUE;
			message = "All overdue tasks";

			for (Task currentTask : pending) {
				DateTime currentTaskDateTime;

				try {
					currentTaskDateTime = currentTask.getDateTime();
				} catch (NullPointerException e) {
					continue;
				}

				if (isOverdue(currentTaskDateTime)) {
					relevant.add(currentTask);
					isEmpty = false;
				}
			}

			if (isEmpty) {
				//eventBus.setCurrentTasks(MESSAGE_NO_OVERDUE);
				eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_OVERDUE, message));
			} else {
				Collections.sort(relevant);
				//eventBus.setCurrentTasks(relevant);
				eventbus.post(new SetCurrentDisplayEvent(relevant, message));
			}


			break;
		}

		return new Status(FEEDBACK_DISPLAY + feedbackMessage, MESSAGE_SUCCESS);
	}



	/**
	 * This method is used to retrieve the updated list of tasks from
	 * TasksManager, and initialise a new list 'relevant' for filtering
	 * out the relevant tasks to be displayed.
	 */
	public void retrieveLists() {
		pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
		completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
		relevant = new ArrayList<Task>();
	}

	/**
	 * This method returns a DateTime object for the queried date
	 * as specified by user. If no date has been specified, it
	 * defaults to the today's date.
	 * 
	 * @param cmd   ParsedInput that the user keyed in.
	 * @return the queried DateTime
	 */

	public DateTime getQueriedDate(ParsedInput cmd) {
		if (cmd.getDateTime() != null) {
			cmdDateTime = cmd.getDateTime();
		} else {
			cmdDateTime = new DateTime(String.format("%02d", now.getDayOfMonth()) 
					+ "/" 
					+ String.format("%02d", now.getMonthValue())
					+ "/"
					+ now.getYear());
		}

		return cmdDateTime;
	}

	/**
	 * This method is used to determine whether a task's date is relevant
	 * to the date that the user has specified. e.g. does start/end fall on
	 * specified date, or the specified date falls in between the queried date.
	 * 
	 * @param cmdDateTime     Specified/queried DateTime by user.
	 * @param taskDateTime A task's DateTime.
	 * @return true if relevant, false if otherwise
	 */
	public boolean isRelevantDate(DateTime cmdDateTime, DateTime taskDateTime) {
		LocalDate taskStart;
		LocalDate taskEnd;

		//TODO this needs to change. start=end default?
		// Don't display floating tasks
		try {
			taskStart = taskDateTime.getStartDate();
			taskEnd = taskDateTime.getEndDate();
		} catch (NullPointerException e) {
			return false;
		}

		LocalDate date = cmdDateTime.getStartDate();

		if (taskStart.isBefore(date) && taskEnd.isAfter(date)) {
			return true;
		} else if (taskStart.isBefore(date) && taskEnd.isBefore(date)) {
			return false;
		} else if (taskStart.isEqual(date) || taskEnd.isEqual(date)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method determines whether a task is overdue as of today.
	 * 
	 * @param taskDateTime    A task's DateTime.
	 * @return
	 */
	public boolean isOverdue(DateTime taskDateTime) {
		LocalDate taskEndDate;
		LocalTime taskEndTime;

		LocalDate nowDate = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		try {
			taskEndDate = taskDateTime.getEndDate();
			//taskEndTime = taskDateTime.getEndTime();
		} catch (NullPointerException e) {
			return false;
		}

		if (taskEndDate.isBefore(nowDate)) {
			return true;
		} else if (taskEndDate.isEqual(nowDate)) {
			try {
				taskEndTime = taskDateTime.getEndTime(); 
				if (taskEndTime.isBefore(nowTime)) {
					return true;
				}
			} catch (NullPointerException e) {

			}

		}

		return false;
	}

}
