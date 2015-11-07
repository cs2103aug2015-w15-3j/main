//@@author A0130720Y

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
import raijin.common.utils.filter.SortFilter;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.TasksManager;

public class DisplayCommandRunner extends CommandRunner {

	// Display options
	private static final String TYPE_ALL = "a"; 		// display ALL PENDING
	private static final String TYPE_PENDING = "p"; 	// display PENDING (for some day)
	private static final String TYPE_COMPLETED = "c"; 	// display COMPLETED
	private static final String TYPE_FLOATING = "f"; 	// display FLOATING
	private static final String TYPE_OVERDUE = "o"; 	// display OVERDUE

	// Head messages
	private static final String HEADMESSAGE_DEFAULT_PENDING = "Tasks pending for...";
	private static final String HEADMESSAGE_ALL_FLOATING = "All floating tasks";
	private static final String HEADMESSAGE_ALL_OVERDUE = "All overdue tasks";
	private static final String HEADMESSAGE_ALL_PENDING = "All pending tasks";
	private static final String HEADMESSAGE_COMPLETED = "Tasks completed as of today, ";

	// Feedback status messages
	private static final String FEEDBACK_DISPLAY = "Displaying: ";
	private static final String FEEDBACK_FLOATING = "floating tasks";
	private static final String FEEDBACK_ALL_PENDING = "all pending tasks";
	private static final String FEEDBACK_PENDING = "pending tasks";
	private static final String FEEDBACK_COMPLETED = "completed tasks";
	private static final String FEEDBACK_OVERDUE = "overdue tasks";

	// List is empty messages
	private static final String MESSAGE_SUCCESS = "Success";
	private static final String MESSAGE_NO_PENDING = "You have no pending tasks!";
	private static final String MESSAGE_NO_COMPLETED = "You have no completed tasks!";
	private static final String MESSAGE_NO_FLOATING = "You have no pending floating tasks!";
	private static final String MESSAGE_NO_OVERDUE = "You have no overdue tasks! Well done!";

	// Relevant DateTime & LocalDate objects
	private DateTime cmdDateTime;
	private DateTime taskDateTime;
	private LocalDate todayDate;
	private LocalDate tomorrowDate;

	// Lists
	private ArrayList<Task> pending;
	private ArrayList<Task> completed;
	private ArrayList<Task> relevant;
	
	// Getting the one instance of eventBus
	private RaijinEventBus eventbus = RaijinEventBus.getInstance();

	// DateTimeFormatter used solely for display
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy");
	
	// Attributes accessible to all methods
	private ParsedInput cmd;
	private String dateForDisplay;

	public Status processCommand(ParsedInput cmd) {
		this.cmd = cmd;

		todayDate = LocalDate.now();
		tomorrowDate = todayDate.plusDays(1);

		retrieveLists();
		cmdDateTime = getQueriedDate();
		dateForDisplay = cmdDateTime.getStartDate().format(dateFormatter);
		String feedbackMessage = "";

		String chosenDisplayType = cmd.getDisplayOptions();

		switch (chosenDisplayType) {

			case TYPE_PENDING:
				feedbackMessage = runTypePending();
				break;
	
			case TYPE_ALL:
				feedbackMessage = runTypeAll();
				break;
	
			case TYPE_FLOATING:
				feedbackMessage = runTypeFloating();
				break;
	
			case TYPE_COMPLETED:
				feedbackMessage = runTypeCompleted();
				break;
	
			case TYPE_OVERDUE:
				feedbackMessage = runTypeOverdue();
				break;
			}

		return new Status(FEEDBACK_DISPLAY + feedbackMessage, MESSAGE_SUCCESS);
	}

	/**
	 * This method is used to retrieve the updated list of tasks from
	 * TasksManager, and initialise a new list 'relevant' for filtering out the
	 * relevant tasks to be displayed.
	 */
	public void retrieveLists() {
		pending = new ArrayList<Task>(TasksManager.getManager().getPendingTasks().values());
		completed = new ArrayList<Task>(TasksManager.getManager().getCompletedTasks().values());
		relevant = new ArrayList<Task>();
	}

	/**
	 * This method returns a DateTime object for the queried date as specified
	 * by user. If no date has been specified, it defaults to the today's date.
	 * 
	 * @param cmd
	 *            ParsedInput that the user keyed in.
	 * @return the queried DateTime
	 */
	public DateTime getQueriedDate() {
		if (cmd.getDateTime() != null) {
			cmdDateTime = cmd.getDateTime();
		} else {
			cmdDateTime = new DateTime(String.format("%02d", todayDate.getDayOfMonth()) + "/"
					+ String.format("%02d", todayDate.getMonthValue()) + "/" + todayDate.getYear());
		}

		return cmdDateTime;
	}


	/**
	 * Runs if user's display option is PENDING
	 * @return feedbackMessage for the eventbus
	 */
	private String runTypePending() {
		String message;
		String feedbackMessage;
		
		// If user's queried date is not today
		if (!cmdDateTime.getStartDate().isEqual(todayDate)) {
			message = "Tasks pending for " + dateForDisplay;
			feedbackMessage = FEEDBACK_PENDING;

			// If user is querying for a range of dates
			if (!cmdDateTime.getStartDate().equals(cmdDateTime.getEndDate())) {
				message += " ~ " + cmdDateTime.getEndDate().format(dateFormatter);

				ArrayList<Task> relevant = new ArrayList<Task>();

				for (Task currentTask : pending) {
					if (isRelevantDate(cmd.getDateTime(), currentTask.getDateTime())
							&& !isAlreadyInList(currentTask, relevant)) {

						relevant.add(currentTask);
					}
				}

				if (relevant.isEmpty()) {
					eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
				} else {
					Collections.sort(relevant);
					eventbus.post(new SetCurrentDisplayEvent(relevant, message));
				}
			} 
			
			// User is only querying for a specific date that is not today.
			else {
				message += " & next day";

				ArrayList<Task> thisDay = new ArrayList<Task>();
				ArrayList<Task> nextDay = new ArrayList<Task>();

				boolean thisDayIsEmpty = true;

				for (Task currentTask : pending) {
					taskDateTime = currentTask.getDateTime();

					if (currentTask.getType() != Constants.TYPE_TASK.FLOATING
							&& isRelevantDate(cmdDateTime, taskDateTime)) {

						thisDayIsEmpty = false;
						thisDay.add(currentTask);
					}
				}

				if (!thisDayIsEmpty) {
					Collections.sort(thisDay);
				}

				boolean nextDayIsEmpty = true;
				LocalDate nextDayDate = cmdDateTime.getStartDate().plusDays(1);

				for (Task currentTask : pending) {
					taskDateTime = currentTask.getDateTime();

					if (currentTask.getType() != Constants.TYPE_TASK.FLOATING
							&& isRelevantDate(new DateTime(nextDayDate, nextDayDate), taskDateTime)
							&& !isAlreadyInList(currentTask, thisDay)) {

						nextDayIsEmpty = false;
						nextDay.add(currentTask);
					}
				}

				if (!nextDayIsEmpty) {
					Collections.sort(nextDay);
					thisDay.addAll(nextDay);
				}

				if (thisDayIsEmpty && nextDayIsEmpty) {
					eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
				} else {
					eventbus.post(new SetCurrentDisplayEvent(thisDay, message));
				}
			}

		} else { // User's queried date is today
			message = HEADMESSAGE_DEFAULT_PENDING;

			feedbackMessage = FEEDBACK_PENDING + " for today, tomorrow and future";
			ArrayList<Task> today = new ArrayList<Task>();
			ArrayList<Task> tomorrow = new ArrayList<Task>();

			boolean todayIsEmpty = true;

			// Getting today's tasks
			for (Task currentTask : pending) {
				taskDateTime = currentTask.getDateTime();

				if (currentTask.getType() != Constants.TYPE_TASK.FLOATING
						&& isRelevantDate(new DateTime(todayDate, todayDate), taskDateTime)) {

					todayIsEmpty = false;
					today.add(currentTask);
				}
			}

			if (!todayIsEmpty) {
				Collections.sort(today);
			}

			boolean tomorrowIsEmpty = true;

			// Getting tomorrow's tasks
			for (Task currentTask : pending) {
				taskDateTime = currentTask.getDateTime();

				if (currentTask.getType() != Constants.TYPE_TASK.FLOATING
						&& isRelevantDate(new DateTime(tomorrowDate, tomorrowDate), taskDateTime)
						&& !isAlreadyInList(currentTask, today)) {

					tomorrowIsEmpty = false;
					tomorrow.add(currentTask);
				}
			}

			if (!tomorrowIsEmpty) {
				Collections.sort(tomorrow);
				today.addAll(tomorrow);
			}

			ArrayList<Task> temp = new ArrayList<Task>(pending);
			Collections.sort(temp);
			for (Task task : today) {
				temp.remove(task);
			}
			int i = 0;
			boolean restIsEmpty = true;
			for (int size = today.size(); size < 20 && i < temp.size(); size++) {
				Task task = temp.get(i++);
				if (!isOverdue(task)) {
					today.add(task);
					restIsEmpty = false;
				}
			}

			if (todayIsEmpty && tomorrowIsEmpty && restIsEmpty) {
				eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
			} else {
				eventbus.post(new SetCurrentDisplayEvent(today, message));
			}

		}
		return feedbackMessage;
	}

	/**
	 * Runs if user's display option is ALL
	 * @return feedbackMessage for the eventbus
	 */
	private String runTypeAll() {
		String message;
		String feedbackMessage;
		feedbackMessage = FEEDBACK_ALL_PENDING;
		message = HEADMESSAGE_ALL_PENDING;

		if (pending.isEmpty()) {
			eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_PENDING, message));
		} else {
			relevant = new ArrayList<Task>(pending);
			Collections.sort(relevant);
			eventbus.post(new SetCurrentDisplayEvent(relevant, message));
		}
		return feedbackMessage;
	}

	/**
	 * Runs if user's display option is FLOATING
	 * @return feedbackMessage for the eventbus
	 */
	public String runTypeFloating() {
		String message;
		String feedbackMessage;
		feedbackMessage = FEEDBACK_FLOATING;
		message = HEADMESSAGE_ALL_FLOATING;
		
		boolean isEmpty = true;

		for (Task currentTask : pending) {

			if (currentTask.getType().equals(Constants.TYPE_TASK.FLOATING)) {
				relevant.add(currentTask);
				isEmpty = false;
			}
		}

		if (isEmpty) {
			eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_FLOATING, message));
		} else {
			relevant = (ArrayList<Task>) new SortFilter(Constants.SORT_CRITERIA.PRIORITY).filter(relevant);
			eventbus.post(new SetCurrentDisplayEvent(relevant, message));
		}
		return feedbackMessage;
	}

	/**
	 * Runs if user's display option is COMPLETED
	 * @return feedbackMessage for the eventbus
	 */
	public String runTypeCompleted() {
		String message;
		String feedbackMessage;
		feedbackMessage = FEEDBACK_COMPLETED;
		message = HEADMESSAGE_COMPLETED + dateForDisplay;
		
		boolean isEmpty = true;

		for (Task currentTask : completed) {
			relevant.add(currentTask);
			isEmpty = false;
		}

		if (isEmpty) {
			eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_COMPLETED, message));
		} else {
			Collections.sort(relevant);
			eventbus.post(new SetCurrentDisplayEvent(relevant, message));
		}
		return feedbackMessage;
	}

	/**
	 * Runs if user's display option is OVERDUE
	 * @return feedbackMessage for the eventbus
	 */
	public String runTypeOverdue() {
		String message;
		String feedbackMessage;
		feedbackMessage = FEEDBACK_OVERDUE;
		message = HEADMESSAGE_ALL_OVERDUE;
		
		boolean isEmpty = true;

		for (Task currentTask : pending) {

			if (isOverdue(currentTask)) {
				relevant.add(currentTask);
				isEmpty = false;
			}
		}

		if (isEmpty) {
			eventbus.post(new SetCurrentDisplayEvent(MESSAGE_NO_OVERDUE, message));
		} else {
			Collections.sort(relevant);
			eventbus.post(new SetCurrentDisplayEvent(relevant, message));
		}
		return feedbackMessage;
	}
	/**
	 * This method is used to determine whether a task's date is relevant to the
	 * date that the user has specified. e.g. does start/end fall on specified
	 * date, or the specified date falls in between the queried date.
	 * 
	 * @param cmdDateTime
	 *            Specified/queried DateTime by user.
	 * @param taskDateTime
	 *            A task's DateTime.
	 * @return true if relevant, false if otherwise
	 */
	public boolean isRelevantDate(DateTime cmdDateTime, DateTime taskDateTime) {
		LocalDate taskStart;
		LocalDate taskEnd;

		try {
			taskStart = taskDateTime.getStartDate();
			taskEnd = taskDateTime.getEndDate();
		} catch (NullPointerException e) {
			return false;
		}

		LocalDate queriedStart = cmdDateTime.getStartDate();
		LocalDate queriedEnd = cmdDateTime.getEndDate();

		// If user's query is only one specific date
		if (queriedStart.isEqual(queriedEnd)) {
			if (taskStart.isBefore(queriedStart) && taskEnd.isAfter(queriedStart)) {
				return true;
			} else if (taskStart.isEqual(queriedStart) || taskEnd.isEqual(queriedStart)) {
				return true;
			} else if (taskStart.isBefore(queriedStart) && taskEnd.isBefore(queriedStart)) {
				return false;
			} else {
				return false;
			}
		}

		// If user's query is a range of dates
		else {
			if (taskStart.isBefore(queriedStart) && taskEnd.isAfter(queriedStart)) {
				return true;
			} else if (taskStart.isEqual(queriedStart) || taskStart.isEqual(queriedEnd) || taskEnd.isEqual(queriedStart)
					|| taskEnd.isEqual(queriedEnd)) {
				return true;
			} else if (taskStart.isAfter(queriedStart) && taskStart.isBefore(queriedEnd)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * This method determines whether a task is overdue as of today.
	 * 
	 * @param task
	 *            A task
	 * @return true if overdue, false if otherwise
	 */
	public boolean isOverdue(Task task) {
		DateTime taskDateTime;

		try {
			taskDateTime = task.getDateTime();
		} catch (NullPointerException e) {
			// has no date, so definitely not overdue
			return false;
		}

		LocalDate taskEndDate;
		LocalTime taskEndTime;

		LocalDate todayDate = LocalDate.now();
		LocalTime todayNowTime = LocalTime.now();

		try {
			taskEndDate = taskDateTime.getEndDate();
		} catch (NullPointerException e) {
			return false;
		}

		if (taskEndDate.isBefore(todayDate)) {
			return true;
		} else if (taskEndDate.isEqual(todayDate)) {
			try {
				taskEndTime = taskDateTime.getEndTime();
				if (taskEndTime.isBefore(todayNowTime)) {
					return true;
				}
			} catch (NullPointerException e) {

			}

		}

		return false;
	}

	/**
	 * This method determines whether a task has already been added into a
	 * particular list.
	 * 
	 * @param task
	 *            The task that you want to check for.
	 * @param list
	 *            List to check if a task already exists.
	 * @return true if task exists in list, false if otherwise.
	 */
	boolean isAlreadyInList(Task task, ArrayList<Task> list) {
		if (list.contains(task)) {
			return true;
		} else {
			return false;
		}
	}

}
