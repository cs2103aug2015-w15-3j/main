//@@author A0112213E

package raijin.logic.realtime;

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
import raijin.common.datatypes.Task;
import raijin.common.eventbus.MainSubscriber;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.ChangeViewEvent;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetCurrentDisplayEvent;
import raijin.common.eventbus.events.SetGuideEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.events.SetTimeSlotEvent;
import raijin.common.exception.FailedToParseException;
import raijin.common.exception.IllegalCommandArgumentException;
import raijin.common.utils.RaijinLogger;
import raijin.common.utils.TaskUtils;
import raijin.logic.parser.ParsedInput;
import raijin.logic.parser.SimpleParser;
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
  private SetTrie commandList;                              //list of commands
  private SetTrie tagList;                                  //list of tags
  private SetTrie taskList;                                 //list of task names
  private TasksManager tasksManager;
  private SimpleParser parser;
  private Logger logger;
  private RaijinEventBus eventbus;
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
    this.eventbus = RaijinEventBus.getInstance();
    this.logger = RaijinLogger.getLogger();
    this.parser = new SimpleParser();
    suggestions = new ArrayList<String>();
    selectedTasks = new ArrayList<Task>();

    updateWithStorage();
    handleKeyEvent();
    handleTabEvent();
  }

  //===========================================================================
  // Helper
  //===========================================================================

  String getArguments(String input) {
    int index = input.trim().indexOf(" ");
    return input.substring(index + 1, input.length());
  }

  String getLastWord(String[] tokens) {
    if (tokens.length == 0) {
      return "";
    } else {
      return tokens[tokens.length - 1];
    }
  }

  String[] getTokens(String userInput) {
    return userInput.trim().split(" ");
  }


  //===========================================================================
  // Updates
  //===========================================================================

  /* Update to latest pending task names */
  void updateTasks() {
    taskNames = TaskUtils.getTaskNames(tasksManager.getPendingTasks());
  }

  /* Update any to any recently updated tags */
  void updateTags() {
    tags = TaskUtils.getTags(tasksManager.getPendingTasks());
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
  /* Overall update with states of application */
  void updateWithStorage() {
    updateTags();
    updateTasks();
    setupList(tags, taskNames);
  }

  boolean isValidCommand(String input) {
    String command = input.toUpperCase(); // To create enum command
    try {
      return Arrays.asList(Constants.Command.values()).contains(Constants.Command.valueOf(command));
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Update task name suggestions 
   * @param input           keywords thats will be added to suggestions
   */
  void updateTaskSuggestion(String input) {
    String[] tokens = getTokens(input);
    String command = tokens[0];

    /*Update only if command is supported*/
    if (isValidCommand(command)) {
      selectedTasks =
          TaskUtils.filterTaskWithName(tasksManager.getPendingTasks(), 
              getArguments(input));
      suggestions =
          taskList.getSuggestions(getArguments(input)).stream().map(
              str -> command + " " + str).collect(Collectors.toList());
    }
  }

  /**
   * Update tag suggestions given input
   * @param input           tags that will be added to suggetions
   */
  void updateTagSuggestion(String input) {
    String[] tokens = getTokens(input);
    String prefix = getLastWord(tokens);
    int lastTagIndex = input.lastIndexOf("#");
    String previousString = input.substring(0, lastTagIndex - 1);
    String tag = prefix.substring(1, prefix.length()); // Get tag from last word

    suggestions =
        tagList.getSuggestions(tag).stream().map(str -> previousString + " #" + str)
            .collect(Collectors.toList());
  }

  /*Checks if there is at least one word in input bar*/
  boolean isCommand(String[] tokens) {
    return tokens.length == 1;
  }

  /*Checks for presence of tag in user input*/
  boolean isTag(String[] tokens) {
    return tokens.length > 1 && getLastWord(tokens).substring(0, 1).equals("#");
  }

  /*Update suggestions lists as user is typing*/
  void updateSuggestions(String input) {
    updateWithStorage();
    String[] tokens = getTokens(input);
    String prefix = getLastWord(tokens);

    if (isCommand(tokens)) { // Get suggestions from commandList
      suggestions = commandList.getSuggestions(prefix);
      eventbus.post(new SetGuideEvent(false));
    } else if (isTag(tokens)) { // Get suggestions from tagList
      updateTagSuggestion(input);
    } else { // Get suggestions from task
      updateTaskSuggestion(input);
    }

  }

  /*Update display view with implicit search result for certain commands*/
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

  /**
   * Checks if the given user input contains command that needs ID field
   * 
   * @return true if command uses ID field
   */
  public boolean isCommandWithID(String userInput) {
    String[] tokens = userInput.trim().split(" ");
    if (isValidCommand(tokens[0])) {
      handleHelpForCommand(tokens[0], userInput);
      return tokens[0].equals("done") || tokens[0].equals("edit") || tokens[0].equals("delete");
    }
    return false;

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

  //===========================================================================
  // Handlers
  //===========================================================================

  /**
   *  Invoke changes and perform update on suggestions
   */
  void handleKeyEvent() {
    MainSubscriber<KeyPressEvent> updateSuggestion = new MainSubscriber<
        KeyPressEvent>(eventbus.getEventBus()) {

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

  /**
   * Set input bar with suggestion when TAB is pressed
   */
  public void handleTabEvent() {
    MainSubscriber<KeyEvent> completeOnPress = new MainSubscriber<
        KeyEvent>(eventbus.getEventBus()) {

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

  /*Handles different variations of add command format*/
  String[] handleAddHelpCommand(String userInput) {
    String[] result = new String[2];
    result[0] = Constants.ADD_SPECIFIC;
    result[1] = Constants.ADD_SPECIFIC_DESC;
    if (userInput.contains(";")) {
      result[0] = Constants.ADD_BATCH;
      result[1] = Constants.ADD_BATCH_DESC;
    }

    if (userInput.contains("from")) {
      result[0] = Constants.ADD_EVENT_DIFFERENT_DATE;
      result[1] = Constants.ADD_EVENT_DIFFERENT_DATE_DESC;
    }

    if (userInput.contains("to")) {
      result[0] = Constants.ADD_EVENT_SAME_DATE;
      result[1] = Constants.ADD_EVENT_SAME_DATE_DESC;
    }


    return result;
  }

  /*Checks for date error so feedback can provided to user at realtime*/
  boolean isInvalidDate(FailedToParseException e) {
    return e.getCause() instanceof IllegalCommandArgumentException
        && ((IllegalCommandArgumentException) e.getCause()).getArgument().equals(
            Constants.CommandParam.DATETIME);
  }

  /*Checks for id error so feedback can provided to user at realtime*/
  boolean isInvalidId(ParsedInput input) {
    if (input.getId() != 0) {

      for (int displayedId : input.getIds()) {

        try {
          eventbus.getDisplayedTasks().get(displayedId - 1).getId();
        } catch (IndexOutOfBoundsException e) {
          return true;
        }

      }
    }
    return false;
  }

  /**
   * Trigger occupied time slots if end date exists
   * @param parsed
   */
  public void handleTimeSlot(ParsedInput parsed) {
    if (parsed.getDateTime() != null && parsed.getDateTime().getEndDate() != null) {
      TimeSlot slots = new TimeSlot(parsed.getDateTime()
          .getEndDate(), TaskUtils.getTasksList(tasksManager.getPendingTasks()));
      eventbus.post(new SetTimeSlotEvent(slots.getOccupiedSlots()));
    } else {
      eventbus.post(new SetTimeSlotEvent(false));
    }
  }

  /**
   * Populate help bar with information relevant to command and user input
   * @param command         
   * @param userInput       
   */
  void handleHelpForCommand(String command, String userInput) {
    Constants.Command inputCommand = Constants.Command.valueOf(command.toUpperCase());
    String commandFormat = "";
    String description = "";

    switch (inputCommand) {

      case ADD:
        String[] result = handleAddHelpCommand(userInput);
        commandFormat = result[0];
        description = result[1];
        break;

      case DELETE:
        commandFormat = Constants.DELETE;
        description = Constants.DELETE_DESC;
        break;

      case DISPLAY:
        commandFormat = Constants.DISPLAY;
        description = Constants.DISPLAY_DESC;
        break;

      case DONE:
        commandFormat = Constants.DONE;
        description = Constants.DONE_DESC;
        break;

      case EDIT:
        commandFormat = Constants.EDIT_TASK;
        description = Constants.EDIT_TASK_DESC;
        break;

      case REDO:
        commandFormat = Constants.REDO;
        description = Constants.REDO_DESC;
        break;

      case SEARCH:
        commandFormat = Constants.SEARCH;
        description = Constants.SEARCH_DESC;
        break;

      case SET:
        commandFormat = Constants.SET;
        description = Constants.SET_DESC;
        break;

      case UNDO:
        commandFormat = Constants.UNDO;
        description = Constants.UNDO_DESC;
        break;

      default:
        break;

    }

    if (!commandFormat.equals("")) { // Only trigger help for certain commands
      try {
        ParsedInput parsed = parser.parse(userInput);

        if (isInvalidId(parsed)) {
          description = Constants.ADD_INVALID_ID;
        }

        handleTimeSlot(parsed);

      } catch (FailedToParseException e) {
        if (isInvalidDate(e)) {
          description = Constants.ADD_INVALID_DATE;
        }
      }
      eventbus.post(new SetGuideEvent(commandFormat, description));
    }

  }





}
