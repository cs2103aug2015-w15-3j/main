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
  public static final String FEEDBACK_ADD_SUCCESS = "Added \"%s\" successfully";
  public static final String FEEDBACK_ADD_FAILURE = "Task \"%s\" already exists";
  public static final String FEEDBACK_EDIT_SUCCESS = "Task ID %d edited successfully.";
  public static final String FEEDBACK_DELETE_SUCCESS = "You have just deleted \"%s\" !";
  public static final String FEEDBACK_DELETE_FAILURE = "Failed to delete. Task(s) don't exist!";
  public static final String FEEDBACK_DONE_FAILURE = "Failed to mark as done. Task(s) don't exist!";
  public static final String FEEDBACK_UNDO_SUCCESS = "Undo successfully";
  public static final String FEEDBACK_REDO_SUCCESS = "Redo successfully";
  public static final String FEEDBACK_ERROR_FAILEDPARSING = "Failed to parse \"%s\"";
  public static final String FEEDBACK_ERROR_FAILEDCOMMAND = "Failed to execute \"%s\" command";
  public static final String FEEDBACK_DONE_SUCCESS = "Nicely done! You have completed the task "
      + "\"%s\". Give yourself a pat on the back!";
  
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
  
  //===========================================================================
  // Parser Constants
  //===========================================================================

  // Regex for recognizing date patterns. Available test cases at: http://fiddle.re/56t2j6
  public static final String DATE_PATTERN = "^(0?[1-9]|[12][0-9]|3[01])(\\/|-|\\.)((0?[1-9]|1[012])"
      + "|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)((\\/|-|\\.)(\\d{4}|\\d{2}))?$";

  /*  // Loopholes: 28/29/30 October. Tried to account for leap year.
  public static final String DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]"
      + "|(?:jan|mar|may|jul|aug|oct|dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]"
      + "|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
      + "|^(?:29(\\/|-|\\.)(?:0?2|(?:feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]"
      + "|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$"
      + "|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep))"
      + "|(?:1[0-2]|(?:oct|nov|dec)))(\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2}))?$";
      */
  
  public static final String DATE_START_PREPOSITION = "by|at|on|during|from";
  public static final String DATE_END_PREPOSITION = "to|till|until";
  
  // Regex for recognizing a date operator. Used for splitting into String array.
  public static final String DATE_OPERATOR = "(\\/|-|\\.)";
  
  // Regex for recognizing priority/subtask/tag operator.
  public static final String PREFIXES = "^[!|@|#|$].*";

  // Regex for recognizing 24hr time patterns. Available test cases at: http://fiddle.re/bc9mj6
  public static final String TIME_PATTERN = "^([01]?[0-9]|2[0-3])[0-5][0-9]$";
  public static final String[] MONTHS = new String[]{"jan","feb","mar","apr","may","jun","jul",
      "aug","sep","oct","nov","dec"};
  
  //===========================================================================
  // Command related constants
  //===========================================================================

  public enum CommandParam {
   ID, NAME, DATETIME, PRIORITY, HELPEROPTION, SUBTASKOF
  }

  public static enum Command { 
    ADD, EDIT, DISPLAY, SEARCH, DELETE, DONE, EXIT, UNDO, REDO, HELP,
    SET
  }
  
  public static final String HELP_MESSAGE = "\n<----==== Raijin to the Rescue! ====---->"
      + "\nADD <Task Name>\nAdds a task with a specified name.\n"
      + "ADD <Task Name> <DateTime>\nAdds a task with a specified name and timeline*.\n"
      + "\nEDIT <Task ID> <Task Name>\nEdits a task's name based on its associated ID.\n"
      + "EDIT <Task ID> <DateTime>\nEdits a task's timeline* based on its associated ID.\n"
      + "EDIT <Task ID> <Task Name> <DateTime>\nEdits both the task's name and timeline*.\n"
      + "\n*DateTime: Can be input in flexible formats. Below are relevant examples:\n"
      + "by 18/3\ton 18/3 1800\tfrom 18/3 1800 to 2000\tfrom 18/3 1800 till 19/3 2000\n"
      + "\nDONE <Task ID>\nMark a task as done.\n"
      + "\nDELETE <Task ID>\nDelete a task.\n"
      + "\nDISPLAY <Date> or <Type>\nDisplay list of tasks depending on type or date specified.\n"
      + "Type: \"c\" for completed tasks, \"p\" for pending tasks. Default: Pending tasks.\n"
      + "\nUNDO/REDO\nUndo a command you previously did, or redo a command you undid.";

  
  //===========================================================================
  // Keyboard shortcuts
  //===========================================================================
  
  public static final KeyCodeCombination KEY_UNDO = new KeyCodeCombination(KeyCode.Z,   //Undo shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_REDO = new KeyCodeCombination(KeyCode.R,   //Redo shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_CLEAR = new KeyCodeCombination(KeyCode.C,  //Clear shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_PASTE = new KeyCodeCombination(KeyCode.V,  //Paste shortcut
      KeyCombination.CONTROL_DOWN);

  public static final KeyCodeCombination KEY_TAB = new KeyCodeCombination(KeyCode.ENTER);

  public  static final KeyCodeCombination KEY_SPACE = new KeyCodeCombination(KeyCode.SPACE);

  public  static final KeyCodeCombination KEY_VIEW_DOWN = new KeyCodeCombination(KeyCode.F2);

  public  static final KeyCodeCombination KEY_VIEW_UP = new KeyCodeCombination(KeyCode.F1);

  //===========================================================================
  // Autcomplete 
  //===========================================================================
  
  private static final LocalDate today = LocalDate.now();

  /*Different default views supported*/
  public static enum View {

    INBOX("Inbox", null), 
    TODAY("Today", new DateTime(today, null)), 
    TOMORROW("Tomorrow", new DateTime(today.plusDays(1L), null)),
    NEXT_WEEK("Next week", new DateTime(today.plusDays(2L), today.plusWeeks(1L)));

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