package raijin.common.datatypes;

public class Constants {
  
  public enum CommandParam {
   ID, NAME, DATETIME
  }
  public static enum Priority { LOW, MID, HIGH }

  public static enum Command { 
    ADD, EDIT, DISPLAY, DELETE, DONE, EXIT, UNDO, HELP
  }
  
  public static enum Error {
    NoSuchTask, IllegalCommand, IllegalCommandArgument, UnableToExecuteCommand
  }

  public static final String FORMAT_DATE = "dd/MM/yyyy";
  public static final String FORMAT_TIME = "HHmm";
  public static final String NAME_USER_DATA = "/data.json";      //Default name used to store data 
  public static final String NAME_USER_CONFIG = "/config.json";  //Default name used to store user config
  public static final String NAME_USER_FOLDER = "/data";         //Default folder name for storage
  public static final String NAME_BASE_CONFIG = "/base.cfg";     //Base config which stores location of storage
  public static final String NAME_TEMP_DATA = "RaijinData";
  public static final String FEEDBACK_INFO_SUCCESS = "Operation is successful.";
  public static final String FEEDBACK_ERROR_ILLEGALCOMMAND = "Illegal command.";
  public static final String FEEDBACK_ADD_SUCCESS = "Added %s successfully.";
  public static final String FEEDBACK_EDIT_SUCCESS = "Task ID %d edited successfully.";
  public static final String EXCEPTION_NONEXISTENTTASK = "Task ID %d does not exist.";
  
  public static final String HELP_MESSAGE = "\nADD <Task Name>\nAdds a task with a specified name.\n"
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

  // Regex for recognizing date patterns. Available test cases at: http://fiddle.re/56t2j6
  public static final String DATE_PATTERN = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]"
      + "|(?:jan|mar|may|jul|aug|oct|dec)))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]"
      + "|(?:jan|mar|apr|may|jun|jul|aug|sep|oct|nov|dec))\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
      + "|^(?:29(\\/|-|\\.)(?:0?2|(?:feb))\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]"
      + "|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$"
      + "|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:jan|feb|mar|apr|may|jun|jul|aug|sep))"
      + "|(?:1[0-2]|(?:oct|nov|dec)))(\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2}))?$";
  
  public static final String DATE_START_PREPOSITION = "by|at|on|during|from";
  public static final String DATE_END_PREPOSITION = "to|till|until";
  
  // Regex for recognizing a date operator. Used for splitting into String array.
  public static final String DATE_OPERATOR = "(\\/|-|\\.)";

  // Regex for recognizing 24hr time patterns. Available test cases at: http://fiddle.re/bc9mj6
  public static final String TIME_PATTERN = "^([01]?[0-9]|2[0-3])[0-5][0-9]$";
  public static final String[] MONTHS = new String[]{"jan","feb","mar","apr","may","jun","jul",
      "aug","sep","oct","nov","dec"};
}
