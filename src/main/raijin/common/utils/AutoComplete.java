package raijin.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.SetTrie;
import raijin.common.datatypes.Task;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.storage.api.TasksManager;

/**
 * Supports auto-completion of several elements such as commands, task names, tags, projects
 * 
 * @author papa
 *
 */
public class AutoComplete {

  private int tabCount = 0;
  private int viewCount = 0;
  private SetTrie commandList; // List of commands
  private SetTrie tagList; // List of tags
  private SetTrie taskList; // List of task names
  private TasksManager tasksManager;
  private Logger logger;
  private com.google.common.eventbus.EventBus eventbus;
  private TreeSet<String> taskNames;
  private TreeSet<String> tags;
  public List<String> suggestions;
  public List<Task> selectedTasks;

  public AutoComplete(TasksManager tasksManager) {
    commandList = new SetTrie();
    tagList = new SetTrie();
    taskList = new SetTrie();
    tags = new TreeSet<String>();
    taskNames = new TreeSet<String>();
    this.tasksManager = tasksManager;
    this.eventbus = RaijinEventBus.getEventBus();
    this.logger = RaijinLogger.getLogger();
    suggestions = new ArrayList<String>();
    selectedTasks = new ArrayList<Task>();

    updateWithStorage();
    handleKeyEvent();
    handleTabEvent();
  }

  /**
   * Setup database for auto completion
   * 
   * @param tagList
   * @param tasks
   */
  public void setupList(TreeSet<String> tagList, TreeSet<String> tasks) {
    loadCommandList();
    loadTagList(tagList);
    loadTaskList(tasks);
  }

  public SetTrie getTaskList() {
    return taskList;
  }

  void loadCommandList() {
    for (Constants.Command cmd : Constants.Command.values()) {
      commandList.add(cmd.toString().toLowerCase()); // Convert enum to string and lowercase
    }
  }

  void loadTagList(TreeSet<String> tagList) {
    this.tagList.addAll(tagList);
  }

  void loadTaskList(TreeSet<String> tasks) {
    this.taskList.addAll(tasks);
  }

  void handleKeyEvent() {
    MainSubscriber<KeyPressEvent> updateSuggestion = new MainSubscriber<KeyPressEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyPressEvent event) {
        updateDisplayView(event.keyEvent);
        if (event.keyEvent.getCode() != KeyCode.TAB && event.keyEvent.getCode() != KeyCode.SPACE) {
          tabCount = 0;
          String userInput = event.currentUserInput;
          updateSuggestions(userInput);
          if (isCommandWithID(userInput)) {
            updateDisplayWithTasks(userInput);
          }
        }

      }

    };
  }

  void updateDisplayWithTasks(String userInput) {
    String command = userInput.trim().split(" ")[0];
    String prefix = getArguments(userInput);
    List<Task> filtered =
        tasksManager.getPendingTasks().values().stream()
            .filter(t -> t.getName().startsWith(prefix)).collect(Collectors.toList());
    if (!filtered.isEmpty()) {
      suggestions =
          IntStream.rangeClosed(1, filtered.size())
              .mapToObj(i -> command + " " + Integer.toString(i)).collect(Collectors.toList());
      eventbus.post(new SetCurrentDisplayEvent(filtered, "Search results:"));
    }
  }

  String getLastWord(String[] tokens) {
    if (tokens.length == 0) {
      return "";
    } else {
      return tokens[tokens.length - 1];
    }
  }

  String[] getTokens(String userInput) {
    return userInput.toLowerCase().trim().split(" ");
  }

  void updateSuggestions(String input) {
    updateWithStorage();
    String[] tokens = getTokens(input);
    String prefix = getLastWord(tokens);

    if (isCommand(tokens)) { // Get suggestions from commandList
      suggestions = commandList.getSuggestions(prefix);
    } else if (isTag(tokens)) { // Get suggestions from tagList
      updateTagSuggestion(input);
    } else { // Get suggestions from task
      updateTaskSuggestion(input);
    }

  }

  /**
   * Checks if the given user input contains command that needs ID field
   * 
   * @return true if command uses ID field
   */
  public boolean isCommandWithID(String userInput) {
    String[] tokens = userInput.trim().split(" ");
    if (isValidCommand(tokens[0])) {
      return tokens[0].equals("done") || tokens[0].equals("edit") || tokens[0].equals("delete");
    }
    return false;

  }

  boolean isCommand(String[] tokens) {
    return tokens.length == 1;
  }

  boolean isTag(String[] tokens) {
    return tokens.length > 1 && getLastWord(tokens).substring(0, 1).equals("#");
  }

  void updateTaskSuggestion(String input) {
    String[] tokens = getTokens(input);
    String command = tokens[0];

    if (isValidCommand(command)) {
      selectedTasks =
          TaskUtils.filterTaskWithName(tasksManager.getPendingTasks(), getArguments(input));
      suggestions =
          taskList.getSuggestions(getArguments(input)).stream().map(str -> command + " " + str)
              .collect(Collectors.toList());
    }
  }

  void updateTagSuggestion(String input) {
    String[] tokens = getTokens(input);
    String prefix = getLastWord(tokens);
    int lastTagIndex = input.lastIndexOf("#");
    String previousString = input.substring(0, lastTagIndex - 1);
    String tag = prefix.substring(1, prefix.length()); // Get tag from last word

    System.out.println(tag);
    suggestions =
        tagList.getSuggestions(tag).stream().map(str -> previousString + " #" + str)
            .collect(Collectors.toList());
    System.out.println(suggestions.toString());
  }

  boolean isValidCommand(String input) {
    String command = input.toUpperCase(); // To create enum command
    try {
      return Arrays.asList(Constants.Command.values()).contains(Constants.Command.valueOf(command));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  String getArguments(String input) {
    int index = input.trim().indexOf(" ");
    return input.substring(index + 1, input.length());
  }

  /* Update to latest pending task names */
  void updateTasks() {
    taskNames = TaskUtils.getTaskNames(tasksManager.getPendingTasks());
  }

  /* Update any to any recently updated tags */
  void updateTags() {
    tags = TaskUtils.getTags(tasksManager.getPendingTasks());
  }

  /* Overall update with states of application */
  void updateWithStorage() {
    updateTags();
    updateTasks();
    setupList(tags, taskNames);
  }

  /**
   * Real time update of display view with suggestions to current pending tasks
   */
  void updateDisplayView(KeyEvent event) {
    int next = 0;
    if (Constants.KEY_VIEW_DOWN.match(event)) {
      next = Math.floorMod((++viewCount), Constants.View.values().length);
      Constants.View view = Constants.View.values()[next];
      eventbus.post(new ChangeViewEvent(TaskUtils.getTasksList(tasksManager.getPendingTasks()),
          view));
    } else if (Constants.KEY_VIEW_UP.match(event)) {
      next = Math.floorMod((--viewCount), Constants.View.values().length);
      Constants.View view = Constants.View.values()[next];
      eventbus.post(new ChangeViewEvent(TaskUtils.getTasksList(tasksManager.getPendingTasks()),
          view));
    }
  }

  public void handleTabEvent() {
    MainSubscriber<KeyEvent> completeOnPress = new MainSubscriber<KeyEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyEvent event) {
        try {

          if (event.getCode() == KeyCode.TAB) {
            String result = suggestions.get((tabCount++) % suggestions.size());
            eventbus.post(new SetInputEvent(result));
          }

        } catch (ArithmeticException e) {
          logger.error(e.getMessage());
        }

      }
    };
  }

}
