//@@author A0112213E

package raijin.common.eventbus.events;

public class SetGuideEvent {

  public String commandFormat;                  //Displayed command format
  public String description;                    //Description of the command
  public boolean isVisible = true;
  public boolean isError = false;
  
  public SetGuideEvent(String commandFormat, String description) {
    this.commandFormat = commandFormat;
    this.description = description;
  }
  
  public SetGuideEvent(boolean isVisible) {
    this.isVisible = isVisible;
  }
}
