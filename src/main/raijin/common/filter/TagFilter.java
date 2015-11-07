//@@author A0112213E

package raijin.common.filter;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import raijin.common.datatypes.Task;

/**
 * Filter tasks based on tags specified 
 * @author papa
 *
 */
public class TagFilter extends TaskFilter {

  private TreeSet<String> limitTags;

  public TagFilter(TreeSet<String> tags) {
    limitTags = tags;
  }

  @Override
  public List<Task> filter(List<Task> tasks) {
    return tasks.stream().filter(
        task -> CollectionUtils.intersection(limitTags, task.getTags()).
        size() == limitTags.size()).collect(Collectors.toList());
  }

}
