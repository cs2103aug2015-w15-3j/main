package raijin.util;

public class Status {
  public static final String ERROR_UNKNOWN_COMMAND = "Unknown Command\n";
  public static final String ERROR_EMPTY_COMMAND = "Empty Command\n";
  public static final String INFO_SUCCESS = "sucessful\n";         //When no feedback for user
  public static final String INFO_ADDED_SUCCESS = "added %s sucessfully\n";
  public static final String INFO_DELETED_SUCCESS = "deleted %s sucessfully\n";
  public static final String INFO_EDITED_SUCCESS = "edited from %s to %s sucessfully\n";
  public static final String INFO_WELCOME = "Welcome to Raijin.\n";
  public static final String INFO_PROMPT = "Command: ";
  
  public static Status instance = new Status();
  
  private boolean isRunning = true; //Determines when to quit program
  
  private Status(){}
  
  public static Status getStatus(){
    return instance;
  }

  public boolean isRunning() {
    return isRunning;
  }

  public void setRunning(boolean isRunning) {
    this.isRunning = isRunning;
  }
}
