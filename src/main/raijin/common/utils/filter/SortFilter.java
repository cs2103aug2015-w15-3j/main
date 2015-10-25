package raijin.common.utils.filter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;

public class SortFilter extends TaskFilter {

  //Sort by comparing deadlines
  Comparator<Task> byDeadline = (t1, t2) -> t1.getDateTime().compareTo(
      t2.getDateTime());

  //Sort by comparing number of tags
  Comparator<Task> byTag = (t1, t2) -> t1.getTags().size() - t2.getTags().size();

  //Sort lexicographically sort case insensitive
  Comparator<Task> byName = (t1, t2) -> t1.getName().toLowerCase().compareTo(
      t2.getName().toLowerCase());

  //Sort by priority
  Comparator<Task> byPriority = (t1, t2) -> comparePriority(t2.getPriority()
      , t1.getPriority());


  Comparator<Task> sortCriteria;

  boolean isReversed = false;

  public SortFilter(Constants.SORT_CRITERIA criteria) {
     switch(criteria) {

      case DEADLINE:
        sortCriteria = byDeadline;
        break;

      case NAME:
        sortCriteria = byName;
        break;

      case PRIORITY:
        sortCriteria = byPriority;
        break;

      case TAG:
        sortCriteria = byTag;
        break;

      default:
        sortCriteria = byDeadline;          //Resolve to sorting by deadline 
        break;
       
     }
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    List<Task> sorted = tasks.stream().sorted(sortCriteria).collect(Collectors.toList());
    if (isReversed) {
      Collections.reverse(sorted);
    }
    return sorted;
  }
  
  //Returns reverse order 
  public void setReverse() {
    isReversed = true;
  }

  int getPriorityValue(String priority) {
    int result = 1;
    if (priority.equals("m")) {
      return result;
    } else {
      result = priority.equals("l") ? 0 : 2;
      return result;
    }
  }
  
  int comparePriority(String source, String target) {
    int sourceVal = getPriorityValue(source);
    int targetVal = getPriorityValue(target);
    return sourceVal - targetVal;
  }

}
