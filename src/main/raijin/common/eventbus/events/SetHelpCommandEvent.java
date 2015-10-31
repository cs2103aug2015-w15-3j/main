package raijin.common.eventbus.events;

public class SetHelpCommandEvent {

  public String commandFormat;                  //Displayed command format
  public String description;                    //Description of the command
  public boolean isVisible = true;
  
  public SetHelpCommandEvent(String commandFormat, String description) {
    this.commandFormat = commandFormat;
    this.description = description;
  }
  
  public SetHelpCommandEvent(boolean isVisible) {
    this.isVisible = isVisible;
  }
}
