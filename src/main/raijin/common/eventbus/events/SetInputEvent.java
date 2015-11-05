//@@author A0112213E

package raijin.common.eventbus.events;

/**
 * Set text of input bar. Used for autocomplete
 * @author papa
 *
 */
public class SetInputEvent {

  public String output;
  
  public SetInputEvent(String output) {
    this.output = output;
  }

}
