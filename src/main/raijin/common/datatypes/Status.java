//@@author A0112213E

package raijin.common.datatypes;

public class Status {
  
  private boolean isSuccess = true;         //indicates success of a command execution
  private String displayText;               //text that will be shown on main display
  private String feedback;                  //feedback after execution of a command

  public Status(String feedback){
    this.feedback = feedback;
  }

  public Status(String feedback, String displayText){
    this.feedback = feedback;
    this.displayText = displayText;
  }

  public Status(String feedback, boolean isSuccess) {
    this.feedback = feedback;
    this.isSuccess = isSuccess;
  }

  public String getDisplayText() {
    return displayText;
  }

  public String getFeedback() {
    return feedback;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

}
