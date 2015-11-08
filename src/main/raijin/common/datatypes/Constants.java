//@@author A0112213E

package raijin.common.datatypes;

import java.time.LocalDate;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Constants {
  

  //===========================================================================
  // Storage Constants
  //===========================================================================
  
  public static final String NAME_USER_DATA = "/data.json";      //Default name used to store data 
  public static final String NAME_USER_CONFIG = "/config.json";  //Default name used to store user config
  public static final String NAME_USER_FOLDER = "/data";         //Default folder name for storage
  public static final String NAME_BASE_CONFIG = "/base.cfg";     //Base config which stores location of storage
  public static final String NAME_TEMP_DATA = "RaijinData";

  //===========================================================================
  // Status and Exception messages
  //===========================================================================

  public static enum Error {
    NoSuchTask, IllegalCommand, IllegalCommandArgument, UnableToExecuteCommand, 
    FailedToParse
  }

  public static final String FEEDBACK_INFO_SUCCESS = "Operation is successful.";
  public static final String FEEDBACK_ADD_SUCCESS = "You have added the task(s) successfully";
  public static final String FEEDBACK_ADD_FAILURE = "Some task(s) already exist";
  public static final String FEEDBACK_EDIT_SUCCESS = "Task \"%s\" edited successfully.";
  public static final String FEEDBACK_DELETE_SUCCESS = "You have just deleted \"%s\" !";
  public static final String FEEDBACK_DELETE_FAILURE = "Failed to delete. Task(s) don't exist!";
  public static final String FEEDBACK_DONE_FAILURE = "Failed to mark as done. Task(s) don't exist!";
  public static final String FEEDBACK_DONE_SUCCESS = "Nicely done! Give yourself a pat on the back!";
  public static final String FEEDBACK_HELP_COMMAND = "Help";
  public static final String FEEDBACK_EXIT_SUCCESS = "Exiting";
  public static final String FEEDBACK_UNDO_SUCCESS = "Undo successfully";
  public static final String FEEDBACK_REDO_SUCCESS = "Redo successfully";
  public static final String FEEDBACK_ERROR_FAILEDPARSING = "Failed to parse \"%s\"";
  public static final String FEEDBACK_ERROR_FAILEDCOMMAND = "Failed to execute \"%s\" command";
  
  //@@author A0124745E
  public static final String FEEDBACK_NO_TASK_NAME = "Error: Please specify a name for your task!";
  public static final String FEEDBACK_NO_TASK_ID = "Error: Please specify a task ID!";
  public static final String FEEDBACK_NO_KEYWORDS = "Error: Please specify keywords to search!";
  public static final String FEEDBACK_NO_FILEPATH = "Error: Please specify a file path!";
  public static final String FEEDBACK_INVALID_CMD = "Error: Please use a valid command!";
  public static final String FEEDBACK_INVALID_ID = "Error: Please specify a number for the task ID!";
  public static final String FEEDBACK_INVALID_DATE = "Error: The date you gave doesn't exist!";
  public static final String FEEDBACK_INVALID_SUBTASK = "Error: Subtask ID needs to be a number!";
  public static final String FEEDBACK_INVALID_PRIORITY = "Error: Invalid priority type!";
  public static final String FEEDBACK_INVALID_STARTTIME = "Error: Invalid start time format!";
  public static final String FEEDBACK_INVALID_ENDDATE = "Error: Invalid end date format!";
  public static final String FEEDBACK_INVALID_ENDTIME = "Error: Invalid end time format!";
  public static final String FEEDBACK_INVALID_ENDDATETIME = "Error: Invalid end date/time format!";
  
  //==================
  // Display 
  //==================
  //@@author A0112213E
  public static final String DISPLAY_OVERDUE = "All overdue tasks";
  public static final String DISPLAY_COMPLETED = "Tasks completed as of today, %s";
  public static final String DISPLAY_FLOATING = "All floating tasks";
  public static final String DISPLAY_ALL = "All pending tasks";
  
  //===========================================================================
  // Datatypes Constants
  //===========================================================================

  public enum SORT_CRITERIA {
    DEADLINE, PRIORITY, NAME, TAG
  }

  public enum TYPE_TASK {
    FLOATING, EVENT, SPECIFIC, OVERDUE
  }
  
  /*Levels of priority supported*/
  public static final String PRIORITY_LOW = "l";
  public static final String PRIORITY_MID = "m";
  public static final String PRIORITY_HIGH = "h";

  /*Universal date format used*/
  public static final String FORMAT_DATE = "dd/MM/yyyy";
  public static final String FORMAT_TIME = "HHmm";
  
  /*Initial number of IDs created*/
  public static final int MAX_ID = 200;
  
  /*Max number of tasks displayed for a specific view*/
  public static final int MAX_TASKS = 20;

  //===========================================================================
  // Parser Constants
  //===========================================================================
  //@@author A0124745E
  
  // Regex for recognizing date patterns. Available test cases at: http://fiddle.re/56t2j6
  public static final String DATE_PATTERN = "^(0?[1-9]|[12][0-9]|3[01])(\\/|-|\\.)((0?[1-9]|1[012])"
      + "|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)((\\/|-|\\.)(\\d{4}|\\d{2}))?$";
  
  public static final String DATE_START_PREPOSITION = "by|at|on|during|from";
  public static final String DATE_END_PREPOSITION = "to|till|until|-";
  
  // Regex for recognizing a date operator. Used for splitting into String array.
  public static final String DATE_OPERATOR = "(\\/|-|\\.)";
  
  // Regex for recognizing priority/subtask/tag operator.
  public static final String PREFIXES = "^[!|@|#|$].*";

  // Regex for recognizing 24hr time patterns. Available test cases at: http://fiddle.re/bc9mj6
  public static final String TIME24_PATTERN = "^([01]?[0-9]|2[0-3])[0-5][0-9]$";
  //Regex for recognizing 24hr time patterns. Available test cases at: http://fiddle.re/dfduv6
  public static final String TIME12_PATTERN = "(0?[1-9]|1[0-2])((.|:)[0-5][0-9])?(am?|pm?)";
  public static final String TIME_PATTERN = TIME24_PATTERN +"|"+ TIME12_PATTERN;
  
  public static final String[] MONTHS = new String[]{"jan","feb","mar","apr","may","jun","jul",
      "aug","sep","oct","nov","dec"};
  
  // Regex for recognizing days input.
  public static final String DAYS = "mond?a?y?|tues?d?a?y?|wedn?e?s?d?a?y?|thur?s?d?a?y?|frid?a?y?"
      + "|satu?r?d?a?y?|sund?a?y?";
  public static final String[] DAYS_LIST = new String[]{"mond?a?y?", "tues?d?a?y?", 
      "wedn?e?s?d?a?y?", "thur?s?d?a?y?", "frid?a?y?", "satu?r?d?a?y?", "sund?a?y?"};
  
  //===========================================================================
  // Command related constants
  //===========================================================================
  //@@author A0112213E
  
  public static final int MAX_NAME_LENGTH = 90;

  public enum CommandParam {
   ID, NAME, DATETIME, PRIORITY, HELPEROPTION, SUBTASKOF
  }

  public static enum Command { 
    ADD, EDIT, DISPLAY, SEARCH, DELETE, DONE, EXIT, UNDO, REDO, HELP,
    SET
  }
  
  //@@author A0124745E-unused
  // Currently using images instead of string feedback messages.
  public static final String HELP_MESSAGE = "\n<----==== Raijin to the Rescue! ====---->"
      + "\nADD <Task Name>\nAdds a task with a specified name.\n"
      + "ADD <Task Name> <DateTime*>\nAdds a task with specified name and timeline.\n"
      + "ADD <Task Name> <DateTime*> <Tag*> <Priority*>"
      + "\nAdds a task with specified name, timeline, tag and priority.\n"
      + "\nEDIT <Task ID> <Task Name>\nEdits a task's name based on its associated ID.\n"
      + "EDIT <Task ID> <DateTime*>\nEdits a task's timeline* based on its associated ID.\n"
      + "EDIT <Task ID> <Task Name> <DateTime*>\nEdits both the task's name and timeline.\n"
      + "EDIT <Task ID> <Task Name> <DateTime*> <Tag*> <Priority*>"
      + "\nEdits the task's name, timeline, tag and priority.\n"
      + "\nDELETE <Task ID> or <Tag*>\nDelete a task.\n"
      + "\nDISPLAY <DateTime*> or <Type>"
      + "\nDisplay list of tasks depending on type or timeline specified.\n"
      + "Types: \"c\" for completed tasks, \"o\" for overdue tasks,"
      + " \"f\" for floating tasks and \"a\" for all tasks.\n" 
      + "Default: Pending tasks.\n"
      + "\nDONE <Task ID> or <Tag*>\nMark a task as done.\n"
      + "\nSEARCH <Keywords> <Tag*> <Priority*>"
      + "\nSearches for tasks based on keywords, tags or priorities.\n"
      + "\nSET <File path>\nSets a directory as the destination for the storage file.\n"
      + "\nUNDO/REDO\nUndo a command you previously did (CTRL+Z),"
      + " or redo a command you undid (CTRL+Y).\n"
      + "\n*DateTime: Can be input in flexible formats. Below are relevant examples:\n"
      + "by 18/3\ton 18/3 1800\tfrom 18/3 1800 to 2000\tfrom 18/3 1800 till 19/3 2000\n"
      + "*Tag: Tags need to include a \"#\" in front of them. Examples: #work #school\n"
      + "*Priority: Priorities have 3 options: !l, !m, and !h, which means !low,"
      + " !medium and !high respectively.\n"
      + "\n***** KEYBOARD SHORTCUTS *****\nCTRL+SPACE: Hide/Unhiding."
      + "\nF1/F2: Toggle between display views."
      + "\nTAB: Autocomplete names, or changes names to ID for EDIT/DELETE/DONE."
      + "\nUP/DOWN: Cycles through previously executed commands.\n";

  //============
  // Add command
  //============
  //@@author A0112213E
  
  public static final String ADD_FLOATING = "add ?[my_task] #tag !priority";
  public static final String ADD_FLOATING_DESC = "Adds a task without deadline";
  public static final String ADD_SPECIFIC = "add ?[my_task] by/on ?[end_date] ?[end_time] "
      + "#tag !priority";
  public static final String ADD_SPECIFIC_DESC = "Adds a task with a deadline. "
      + "If no time is given, it will be set to 11.59 pm";
  public static final String ADD_EVENT_SAME_DATE = "add ?[my_task] by/on ?[end_date] "
      + "?[start_time] to ?[end_time] #tag !priority";
  public static final String ADD_EVENT_SAME_DATE_DESC = "Adds an event for that "
      + "period of time";
  public static final String ADD_EVENT_DIFFERENT_DATE = "add ?[my_task] from ?[start_date] "
      + "?[start_time] to ?[end_date] ?[end_time] #tag !priority";
  public static final String ADD_EVENT_DIFFERENT_DATE_DESC = "Adds an event that "
      + "spans more than 1 day";

  public static final String ADD_BATCH = "add ?[task_1] ; ?[task_2] ; ?[task_3] ...";
  public static final String ADD_BATCH_DESC = "Adds multiple tasks followed "
      + "by details of the tasks";

  public static final String ADD_INVALID_DATE = "?Please enter a valid time format";
  public static final String ADD_INVALID_ID = "?Invalid task id(s) found. Please "
      + "enter only task id that appears on the screen";

  //=============
  // Edit command
  //=============

  public static final String EDIT_TASK = "edit ?[task_id] ?[changes]";
  public static final String EDIT_TASK_DESC = "Edit any task given its task id";
  
  //================
  // Display command
  //================

  public static final String DISPLAY = "display all | completed | overdue | floating";
  public static final String DISPLAY_DESC = "Show tasks based on different criteria";

  //================
  // Done command
  //================

  public static final String DONE = "done ?[task_id] ?[task_id] ... #tag";
  public static final String DONE_DESC = "marks a task as done given the task id or tag";

  //================
  // Delete command
  //================

  public static final String DELETE = "delete ?[task_id] ?[task_id] .... #tag";
  public static final String DELETE_DESC = "deletes a task that no longer needed to be "
      + "completed";

  //================
  // Undo and redo command
  //================

  public static final String UNDO = "undo";
  public static final String REDO = "redo";
  public static final String UNDO_DESC = "Undo previous command. You can also use "
      + "Ctrl + Z to undo";
  public static final String REDO_DESC = "Redo previous command. You can also use "
      + "Ctrl + Y to undo";

  //================
  // search command
  //================

  public static final String SEARCH = "search ?[keyword] ?[keyword] ... #tag !priority";
  public static final String SEARCH_DESC = "searches all pending tasks that "
      + "matches given keywords";
  
  
  //================
  // set command
  //================

  public static final String SET = "set ?[directory_to_store_data]";
  public static final String SET_DESC = "Specifies directory to store your data";

  //================
  // keyboard shortcuts for help
  //================
  //@@author A0129650E
  public static final String KEY_UNDO_HELP = "Ctrl + Z";
  public static final String KEY_UNDO_HELP_DESC = "Keyboard shortcut for Undo";
  
  public static final String KEY_REDO_HELP = "Ctrl + Y";
  public static final String KEY_REDO_HELP_DESC = "Keyboard shortcut for Redo";
   
  public static final String KEY_CLEAR_HELP = "Ctrl + R";
  public static final String KEY_CLEAR_HELP_DESC = "Keyboard shortcut for Clear";
  
  public static final String KEY_COPY_HELP = "Ctrl + C";
  public static final String KEY_COPY_HELP_DESC = "Keyboard shortcut for Copy";
  
  public static final String KEY_CUT_HELP = "Ctrl + X";
  public static final String KEY_CUT_HELP_DESC = "Keyboard shortcut for Cut";
 
  public static final String KEY_PASTE_HELP = "Ctrl + V";
  public static final String KEY_PASTE_HELP_DESC = "Keyboard shortcut for Paste";
  
  public static final String KEY_TAB_HELP = "tab";
  public static final String KEY_TAB_HELP_DESC = "Press to autocomplete your command";

  public static final String KEY_VIEW_DOWN_HELP = "F2";
  public static final String KEY_VIEW_DOWN_HELP_DESC = "Keyboard shortcut for to "
  		+ "toggle down between all, today, tomorrow and future pending tasks";
  
  public static final String KEY_VIEW_UP_HELP = "F1";
  public static final String KEY_VIEW_UP_HELP_DESC = "Keyboard shortcut for to "
	  		+ "toggle up between all, today, tomorrow and future pending tasks";
   
  public static final String KEY_MINMAX_HELP = "Alt + M";
  public static final String KEY_MINMAX_HELP_DESC = "To switch between simple and advanced mode";
 
  public static final String SCROLL_UP_HELP = "Pg Up";
  public static final String SCROLL_UP_HELP_DESC = "To scroll up the display panel";

  public static final String SCROLL_DOWN_HELP = "Pg Down";
  public static final String SCROLL_DOWN_HELP_DESC = "To scroll down the display panel";

  //@@author A0112213E
 //===========================================================================
  // Keyboard shortcuts
  //===========================================================================
  
  public static final KeyCodeCombination KEY_UNDO = new KeyCodeCombination(KeyCode.Z,   //Undo shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_REDO = new KeyCodeCombination(KeyCode.Y,   //Redo shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_CLEAR = new KeyCodeCombination(KeyCode.R,  //Clear shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_COPY = new KeyCodeCombination(KeyCode.C,  //Copy shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_CUT = new KeyCodeCombination(KeyCode.X,  //Cut shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_PASTE = new KeyCodeCombination(KeyCode.V,  //Paste shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_TAB = new KeyCodeCombination(KeyCode.ENTER);

  public  static final KeyCodeCombination KEY_VIEW_DOWN = new KeyCodeCombination(KeyCode.F2);

  public  static final KeyCodeCombination KEY_VIEW_UP = new KeyCodeCombination(KeyCode.F1);

  public  static final KeyCodeCombination KEY_MINMAX = new KeyCodeCombination(KeyCode.M, 
      KeyCodeCombination.ALT_DOWN);

  public  static final KeyCodeCombination SCROLL_UP = new KeyCodeCombination(KeyCode.PAGE_UP);

  public  static final KeyCodeCombination SCROLL_DOWN = new KeyCodeCombination(KeyCode.PAGE_DOWN);

  //===========================================================================
  // Autocomplete 
  //===========================================================================
  
  private static final LocalDate today = LocalDate.now();
  private static final LocalDate tomorrow = LocalDate.now().plusDays(1L);

  /*Different default views supported*/
  public static enum View {

    INBOX(Constants.DISPLAY_ALL, null), 
    TODAY("Today", new DateTime(today, today)), 
    TOMORROW("Tomorrow", new DateTime(tomorrow, tomorrow)),
    FUTURE("Future", new DateTime(today.plusDays(2L), today.plusYears(1L)));

    private String message;
    private DateTime dateTime;
    
    private View(String message, DateTime dateTime) {
      this.message = message;
      this.dateTime = dateTime;
    }
    
    public String getMessage() {
      return message;
    }

    public DateTime getDateTime() {
      return dateTime;
    }
  }
}