//@@author A0112213E

package raijin.logic.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import raijin.common.datatypes.Constants;
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

  private static final String DISPLAY_MESSAGE = "Search results: %d found";
  private static final String INITIAL_FEEDBACK_MESSAGE = "Search: ";
  private static final String MESSAGE_TEMPLATE = "\"%s\", ";
  String displayMessageSent;            //Search result message displayed  

  private Task currentTask;
  private String inputPriority;
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

  public void appendKeywords(ArrayList<String> keywords, StringBuilder strBuilder) {
    for (String keyword : keywords) {
      if (!keyword.equals("")) {
        strBuilder.append(String.format(MESSAGE_TEMPLATE, keyword));
      }
    }
  }

  public void appendTags(StringBuilder strBuilder) {
    for (String tag : currentTask.getTags()) {
      strBuilder.append(String.format(MESSAGE_TEMPLATE, "#" + tag));
    }
  }

  /**
   * Get full string representation of a priority
   * @param priority
   * @return
   */
  String getFullPriority(String priority) {
    String fullPriority = "Medium priority";
    if (priority.equals(Constants.PRIORITY_HIGH)) {
      fullPriority = "High priority";
    } else if (priority.equals(Constants.PRIORITY_LOW)) {
      fullPriority = "Low priority";
    }
    return fullPriority;
  }

  String appendPriority(String feedback) {
    if (inputPriority != null) {
      return feedback + " " + getFullPriority(inputPriority);
    }
    return feedback;
  }

  String createFeedbackMessage(ArrayList<String> keywords, StringBuilder strBuilder) {
    appendKeywords(keywords, strBuilder);
    appendTags(strBuilder);
    String result = strBuilder.toString();
    /*Checks if any message is added to string builder*/
    if (!result.equals(INITIAL_FEEDBACK_MESSAGE)) {
      /*Removes hanging comma*/
      result = result.substring(0, result.length()-2);
    }
    result = appendPriority(result);
    return normalizeTaskName(result);
  }


  Status createSuccessfulStatus(ArrayList<String> keywords) {
    StringBuilder strBuilder = new StringBuilder(INITIAL_FEEDBACK_MESSAGE);
    /*Generates feedback message*/
    String feedback = createFeedbackMessage(keywords, strBuilder);
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
    displayMessageSent = String.format(DISPLAY_MESSAGE,filtered.size());
    eventbus.post(new SetCurrentDisplayEvent(filtered, displayMessageSent));
    return createSuccessfulStatus(currentTask.getKeywords());
  }



}
