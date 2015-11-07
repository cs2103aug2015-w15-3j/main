//@@author A0112213E

package raijin.logic.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.TaskUtils;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

/**
 * Searches tasks based on keywords, tags and priority
 * @author papa
 *
 */
public class SearchCommandRunner extends CommandRunner {

  private Task currentTask;
  private String inputPriority;
  private static final String NO_ARGUMENT = "No argument(s) given to search";
  private static final String DISPLAY_MESSAGE = "Search results: %d found";
  private static final String INITIAL_FEEDBACK_MESSAGE = "Search keywords: ";
  private static final String MESSAGE_TEMPLATE = "\"%s\", ";

  /**
   * Additional filter to search. If no priority given, default to true
   * @param task
   * @return
   */
  boolean handlePriority(Task task, String priority) {
    if (priority == null) {
      return true;
    } else {
      return task.getPriority().equals(inputPriority);
    }
  }


  void createTask(ParsedInput input) {
    currentTask = new Task(input.getName(), idManager.getId(), input);
  }

  Status createSuccessfulStatus(ArrayList<String> keywords) {
    StringBuilder strBuilder = new StringBuilder(INITIAL_FEEDBACK_MESSAGE);

    for (String keyword : keywords) {
      strBuilder.append(String.format(MESSAGE_TEMPLATE, keyword));
    }

    String feedback = strBuilder.toString();
    feedback = feedback.substring(0, feedback.length()-2);
    return new Status(feedback);
  }

  /*checks if the task matches tag query*/
  public boolean matchOnlyTags(Task task, TreeSet<String> tags) {
    return handlePriority(task, inputPriority) && CollectionUtils.intersection(
        tags, task.getTags()).size() == tags.size();
  }

  /*checks if the task matched keyword(s) query*/
  public boolean matchOnlyKeyword(ArrayList<String> source, Task task) {
    ArrayList<String> target = task.getKeywords();
    target = (ArrayList<String>) target.stream().map(k -> k.toLowerCase())
        .collect(Collectors.toList());
    source = (ArrayList<String>) source.stream().map(k -> k.toLowerCase())
        .collect(Collectors.toList());
    return handlePriority(task, inputPriority) && CollectionUtils.intersection(
        target,source).size() == source.size();
  }

  boolean isMatchedKeywords(ArrayList<String> source, Task task) {
    if (source.get(0) == "") {                                                  //Checks if keywords are empty
      return matchOnlyTags(task, currentTask.getTags());                        
    } else if (currentTask.getTags().isEmpty()) {                               //Checks if tags are empty
      return matchOnlyKeyword(source, task);
    } else {
      return matchOnlyKeyword(source, task) && matchOnlyTags(task, currentTask.getTags());
    }

  }

  List<Task> getTasksWithMatchedKeyword(ArrayList<Task> pendingTasks) {
    ArrayList<String> keywords = currentTask.getKeywords();
    List<Task> filtered = pendingTasks.stream().filter(
        x -> isMatchedKeywords(keywords, x)).collect(Collectors.toList());
    return filtered;
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    inputPriority = input.getPriority();            //Update priority
    List<Task> filtered = new ArrayList<Task>();

    HashMap<Integer, Task> pendingTasks = tasksManager.getPendingTasks();
    createTask(input);
    ArrayList<Task> tempList = (ArrayList<Task>) TaskUtils.getTasksList(pendingTasks);
    filtered = getTasksWithMatchedKeyword(tempList);
    eventbus.post(new SetCurrentDisplayEvent(filtered, 
        String.format(DISPLAY_MESSAGE,filtered.size())));
    return createSuccessfulStatus(currentTask.getKeywords());
  }



}
