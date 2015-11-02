//@@author A0112213E

package raijin.common.eventbus.events;

import java.util.List;

import raijin.common.datatypes.DateTime;

public class SetTimeSlotEvent {
  
  public List<DateTime> busySlots;
  public boolean isVisible = true;

  public SetTimeSlotEvent(List<DateTime> busySlots) {
    this.busySlots = busySlots;
    if (busySlots.size() == 0) {
      isVisible = false;
    }
  }
  
  public SetTimeSlotEvent(boolean isVisible) {
    this.isVisible = isVisible;
  }

}
