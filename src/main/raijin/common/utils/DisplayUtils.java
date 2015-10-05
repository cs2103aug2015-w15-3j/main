package raijin.common.utils;

import java.util.List;
import java.util.stream.Collectors;

import raijin.common.datatypes.Task;

public class DisplayUtils {

  public static List<String> filterName(List<Task> tasks) {
    return tasks.stream().map(Task::getName).collect(Collectors.toList());
  }
}
