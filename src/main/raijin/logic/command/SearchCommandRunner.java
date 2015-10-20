package raijin.logic.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import raijin.common.datatypes.Status;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.events.SetCurrentTasksEvent;
import raijin.common.exception.UnableToExecuteCommandException;
import raijin.logic.api.CommandRunner;
import raijin.logic.parser.ParsedInput;

public class SearchCommandRunner extends CommandRunner {

  private Task currentTask;

  void createTask(ParsedInput input) {
    currentTask = new Task(input.getName(), idManager.getId());
  }

  Status createSuccessfulStatus(int numberOfMatched) {
    return new Status(String.format("Result(%d) matched found", numberOfMatched));
  }

  boolean isMatchedKeywords(ArrayList<String> source, ArrayList<String> target) {
    String targetKeywords = target.toString();
    for (String keyword : source) {
      if (targetKeywords.contains(keyword)) {
        return true;
      }
    }
    return false;
  }

  boolean isInvalidInput(ParsedInput input) {
    return input.getNames().isEmpty() && input.getTags().isEmpty();
  }

  @Override
  protected Status processCommand(ParsedInput input) throws UnableToExecuteCommandException {
    if (isInvalidInput(input)) {
      return new Status("No argument given to search");
    }
    HashMap<Integer, Task> pendingTasks = tasksManager.getPendingTasks();
    createTask(input);
    ArrayList<String> keywords = currentTask.getKeywords();
    ArrayList<Task> tempList = new ArrayList<Task>(pendingTasks.values());
    List<Task> filtered = tempList.stream().filter(
        x -> isMatchedKeywords(keywords, x.getKeywords())).collect(Collectors.toList());
    //eventBus.setCurrentTasks(filtered);
    eventbus.post(new SetCurrentTasksEvent(filtered));
    return createSuccessfulStatus(filtered.size());
  }

}
