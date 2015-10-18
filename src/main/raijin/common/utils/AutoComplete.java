package raijin.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.scene.input.KeyEvent;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.SetTrie;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.events.SetInputEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.storage.api.TasksManager;

/**
 * Supports auto-completion of several elements such as
 * commands, task names, tags, projects 
 * @author papa
 *
 */
public class AutoComplete {

  private int tabCount = 0;
  private SetTrie commandList;          //List of commands
  private SetTrie tagList;              //List of tags
  private SetTrie taskList;             //List of task names
  private TasksManager tasksManager;
  private Logger logger;
  private com.google.common.eventbus.EventBus eventbus;
  public List<String> suggestions;
  
  public AutoComplete(TasksManager tasksManager) {
    commandList = new SetTrie();
    tagList = new SetTrie();
    taskList = new SetTrie();
    this.tasksManager =  tasksManager;
    this.eventbus = RaijinEventBus.getEventBus();
    this.logger = RaijinLogger.getLogger();
    suggestions = new ArrayList<String>();
    
    TreeSet<String> tagList  = TaskUtils.getTags(tasksManager.getPendingTasks());
    TreeSet<String> taskList  = TaskUtils.getTaskNames(tasksManager.getPendingTasks());
    setupList(tagList, taskList);
    handleKeyEvent();
  }
  
  /**
   * Setup database for auto completion 
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
      commandList.add(cmd.toString().toLowerCase());    //Convert enum to string and lowercase
    }
  }
  
  void loadTagList(TreeSet<String> tagList) {
    this.tagList.addAll(tagList);
  }

  void loadTaskList(TreeSet<String> tasks) {
    this.taskList.addAll(tasks);
  }
  
  void handleKeyEvent() {
    MainSubscriber<KeyPressEvent> completeOnPress = new MainSubscriber<KeyPressEvent>(eventbus) {

      @Subscribe
      @Override
      public void handleEvent(KeyPressEvent event) {
        if (Constants.KEY_SPACE.match(event.keyEvent)) {
          tabCount = 0;
        } else if (Constants.KEY_TAB.match(event.keyEvent)) {
          String suggestion = suggestions.get((tabCount++)%suggestions.size());
          eventbus.post(new SetInputEvent(suggestion));
          event.keyEvent.consume();
        } else {
          String userInput = event.currentUserInput;
          updateSuggestions(userInput);
        }
        
      }};
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
    String[] tokens = getTokens(input);
    String prefix = getLastWord(tokens);
    
    if (isCommand(tokens)) {                    //Get suggestions from commandList
      suggestions = commandList.getSuggestions(prefix);
    } else if (isTag(tokens)) {                 //Get suggestions from tagList
      suggestions = tagList.getSuggestions(prefix.substring(1, prefix.length()));
    } else {                                    //Get suggestions from task
      suggestions = taskList.getSuggestions(input.trim());
    }

  }

  boolean isCommand(String[] tokens) {
    return tokens.length == 1;
  }

  boolean isTag(String[] tokens) {
    return tokens.length > 1 && getLastWord(tokens).substring(0, 1).equals("#");
  }

  public static void main(String[] args) {
    AutoComplete autoComplete = new AutoComplete(TasksManager.getManager());
  }

}
