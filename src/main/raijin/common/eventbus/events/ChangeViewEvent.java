package raijin.common.eventbus.events;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.filter.DateFilter;

public class ChangeViewEvent {

  static int count = 0; // Counter used to cycle through different views
  LocalDate today;
  public List<Task> focusView; // View that will be displayed to user
  public Constants.View typeOfView;
  public String viewMessage;
  private static final String HEAD_FORMAT = "%s (%s)";
  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy");

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


  }

}
