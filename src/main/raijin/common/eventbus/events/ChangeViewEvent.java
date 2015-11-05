//@@author A0112213E

package raijin.common.eventbus.events;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.utils.filter.DateFilter;

/**
 * Indicates change of view to fixed set of supported views.
 * Supported views include today, tomorrow, future, all
 * @author papa
 *
 */
public class ChangeViewEvent {

  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy");
  private static final String HEAD_FORMAT = "%s (%s)";
  private LocalDate today;
  private RaijinEventBus eventbus = RaijinEventBus.getInstance();
  public List<Task> focusView;                                                  //list of tasks to be displayed
  public Constants.View typeOfView;                                             //designated view to be displayed
  public String viewMessage;                                                    //heading of current view

  public ChangeViewEvent(List<Task> pendingTasks, Constants.View view) {

    DateFilter dateFilter = new DateFilter(pendingTasks);
    today = LocalDate.now();
    typeOfView = view;

    switch (view) {

      case INBOX:
        viewMessage = view.getMessage();
        break;

      case TODAY:
        dateFilter.setDateTime(new DateTime(null, today));
        viewMessage = String.format(HEAD_FORMAT, view.getMessage(), view
            .getDateTime().getEndDate().format(dateFormatter));
        break;

      case TOMORROW:
        dateFilter.setDateTime(new DateTime(null, today.plusDays(1)));
        viewMessage = String.format(HEAD_FORMAT, view.getMessage(), view
            .getDateTime().getEndDate().format(dateFormatter));
        break;

      case FUTURE:
        dateFilter.setDateTime(new DateTime(today.plusDays(2), null));
        viewMessage = view.getMessage();
        break;

      default:
        dateFilter.setDateTime(new DateTime(null, today));
        viewMessage = String.format(HEAD_FORMAT, view.getMessage(), view
            .getDateTime().getEndDate().format(dateFormatter));
        break;

    }
    focusView = dateFilter.filter(pendingTasks);
    eventbus.setDisplayedTasks(focusView);                                      //Update displayed tasks
  }

}
