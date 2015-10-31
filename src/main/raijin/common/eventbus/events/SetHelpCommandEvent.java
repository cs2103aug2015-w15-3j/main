package raijin.common.eventbus.events;

public class SetHelpCommandEvent {

  public String commandFormat;                  //Displayed command format
  public String description;                    //Description of the command
  
  public SetHelpCommandEvent(String commandFormat, String description) {
    this.commandFormat = commandFormat;
    this.description = description;
  }
}
