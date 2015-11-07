//@@author A0112213E

package raijin.common.eventbus.events;

import java.util.List;

import com.google.common.collect.Multiset;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.Task;
import raijin.common.filter.DateFilter;
import raijin.common.filter.TypeFilter;
import raijin.common.utils.TaskUtils;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public class TasksChangedEvent {

  public List<Task> pendingToday;                 
  public List<Task> pendingTomorrow;             
  public List<Task> pendingNextWeek;              
  public List<Task> overdue;                     
  public Multiset<String> tags;                                                 //tags that exist in storage
  public String storageDirectory;
  DateFilter dateFilter;
  TypeFilter overdueFilter;
  
  public List<Task> pendingTasks;
  public List<Task> completedTasks;
  
  public TasksChangedEvent() {
    pendingTasks = TaskUtils.getTasksList(TasksManager.getManager().getPendingTasks());
    completedTasks = TaskUtils.getTasksList(TasksManager.getManager().getCompletedTasks());
    dateFilter = new DateFilter(pendingTasks);
    overdueFilter = new TypeFilter(Constants.TYPE_TASK.OVERDUE);
    init();
  }
  
  /**
   * Initialise different categories of tasks
   */
  void init() {
    pendingToday = dateFilter.filter(pendingTasks, Constants.View.TODAY);
    pendingTomorrow = dateFilter.filter(pendingTasks, Constants.View.TOMORROW);
    pendingNextWeek= dateFilter.filter(pendingTasks, Constants.View.FUTURE);
    overdue = overdueFilter.filter(pendingTasks);
    tags = TaskUtils.getTagMultiSet(pendingTasks);
    storageDirectory = Session.getSession().storageDirectory;
  }

}
