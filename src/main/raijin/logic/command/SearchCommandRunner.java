package raijin.logic.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.events.SetCurrentTasksEvent;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.common.utils.TaskUtils;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class SearchCommandRunner extends CommandRunner {

  private Task currentTask;

  void createTask(ParsedInput input) {
    currentTask = new Task(input.getName(), idManager.getId(), input);
  }

  Status createSuccessfulStatus(int numberOfMatched) {
    return new Status(String.format("Result(%d) matched found", numberOfMatched));
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
    return !CollectionUtils.intersection(tags, task.getTags()).isEmpty();
  }

  public boolean matchOnlyKeyword(ArrayList<String> source, Task task) {
    ArrayList<String> target = task.getKeywords();
    return CollectionUtils.intersection(target,source).size() == source.size();
  }

  boolean isInvalidInput(ParsedInput input) {
    return input.getNames().isEmpty() && input.getTags().isEmpty();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    List<Task> filtered = new ArrayList<Task>();
    if (isInvalidInput(input)) {
      return new Status("No argument given to search");
    }
    HashMap<Integer, Task> pendingTasks = tasksManager.getPendingTasks();
    createTask(input);
    ArrayList<Task> tempList = new ArrayList<Task>(pendingTasks.values());
    filtered = getTasksWithMatchedKeyword(tempList);
    eventbus.post(new SetCurrentTasksEvent(filtered));
    return createSuccessfulStatus(filtered.size());
  }


  List<Task> getTasksWithMatchedKeyword(ArrayList<Task> pendingTasks) {
    ArrayList<String> keywords = currentTask.getKeywords();
    List<Task> filtered = pendingTasks.stream().filter(
        x -> isMatchedKeywords(keywords, x)).collect(Collectors.toList());
    return filtered;
  }

}
