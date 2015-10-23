package raijin.common.eventbus.events;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.google.common.collect.Multiset;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Task;
import raijin.common.utils.TaskUtils;
import raijin.common.utils.filter.DateFilter;
import raijin.storage.api.Session;
import raijin.storage.api.TasksManager;

public class TasksChangedEvent {

  public List<Task> pendingToday;                 
  public List<Task> pendingTomorrow;             
  public List<Task> pendingNextWeek;              
  public List<Task> overdue;                     
  public Multiset<String> tags;        //Stores number of pending tasks associated with a tag
  public String storageDirectory;
  DateFilter dateFilter;
  
  public List<Task> pendingTasks;
  public List<Task> completedTasks;
  
  public TasksChangedEvent() {
    pendingTasks = TaskUtils.getTasksList(TasksManager.getManager().getPendingTasks());
    completedTasks = TaskUtils.getTasksList(TasksManager.getManager().getCompletedTasks());
    dateFilter = new DateFilter(pendingTasks);
    update();
  }
  
  void update() {
    pendingToday = dateFilter.filter(pendingTasks, Constants.View.TODAY);
    pendingTomorrow = dateFilter.filter(pendingTasks, Constants.View.TOMORROW);
    pendingNextWeek= dateFilter.filter(pendingTasks, Constants.View.NEXT_WEEK);
    //Changes limit for overdue task
    DateTime current = new DateTime(LocalDate.now(), LocalTime.now(), null, null);
    dateFilter.setDateTime(current);
    overdue = dateFilter.filter(pendingTasks);
    tags = TaskUtils.getTagMultiSet(pendingTasks);
    storageDirectory = Session.getSession().storageDirectory;
  }

}
