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

public class SearchCommandRunner extends CommandRunner {

  private Task currentTask;
  private String inputPriority;
  private static final int MAX_TAGS = 3;            //Maximum amount of tags displayed
  private static final int MAX_KEYWORDS = 6;        //Maximum keyword of tags displayed
  private static final String DISPLAY_MESSAGE = "Search results: %d found";
  private static final String INITIAL_FEEDBACK_MESSAGE = "Search keywords: ";
  private static final String MESSAGE_TEMPLATE = "\"%s\", ";

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

  boolean isMatchedKeywords(ArrayList<String> source, Task task) {
    if (source.get(0) == "") {                          //Check if keyword is empty   
      return matchOnlyTags(task, currentTask.getTags());
    } else if (currentTask.getTags().isEmpty()){
      return matchOnlyKeyword(source, task);
    } else {
      return matchOnlyKeyword(source, task) && matchOnlyTags(task, currentTask.getTags());
    }

  }

  public boolean matchOnlyTags(Task task, TreeSet<String> tags) {
    return handlePriority(task) && CollectionUtils.intersection(
        tags, task.getTags()).size() == tags.size();
  }

  public boolean matchOnlyKeyword(ArrayList<String> source, Task task) {
    ArrayList<String> target = task.getKeywords();
    return handlePriority(task) && CollectionUtils.intersection(
        target,source).size() == source.size();
  }

  boolean isInvalidInput(ParsedInput input) {
    return input.getNames().isEmpty() && input.getTags().isEmpty();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    inputPriority = input.getPriority();            //Update priority
    List<Task> filtered = new ArrayList<Task>();
    if (isInvalidInput(input)) {
      return new Status("No argument given to search");
    }
    HashMap<Integer, Task> pendingTasks = tasksManager.getPendingTasks();
    createTask(input);
    ArrayList<Task> tempList = (ArrayList<Task>) TaskUtils.getTasksList(pendingTasks);
    filtered = getTasksWithMatchedKeyword(tempList);
    eventbus.post(new SetCurrentDisplayEvent(filtered, 
        String.format(DISPLAY_MESSAGE,filtered.size())));
    return createSuccessfulStatus(currentTask.getKeywords());
  }


  List<Task> getTasksWithMatchedKeyword(ArrayList<Task> pendingTasks) {
    ArrayList<String> keywords = currentTask.getKeywords();
    List<Task> filtered = pendingTasks.stream().filter(
        x -> isMatchedKeywords(keywords, x)).collect(Collectors.toList());
    return filtered;
  }

  boolean handlePriority(Task task) {
    if (inputPriority == null) {
      return true;
    } else {
      return task.getPriority().equals(inputPriority);
    }
  }


}
