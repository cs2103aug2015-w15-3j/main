//@@author A0112213E

package raijin.common.eventbus.events;

/**
 * Set feedback bar with output message
 * @author papa
 *
 */
public class SetFeedbackEvent {

  public String output;

  public SetFeedbackEvent(String output) {
    this.output = output;
  }

}
