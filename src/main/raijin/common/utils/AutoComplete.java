package raijin.common.utils;

import java.util.List;
import java.util.TreeSet;

import com.google.common.eventbus.Subscribe;

import javafx.scene.input.KeyEvent;
import raijin.common.datatypes.Constants;
import raijin.common.datatypes.SetTrie;
import raijin.common.eventbus.RaijinEventBus;
import raijin.common.eventbus.events.KeyPressEvent;
import raijin.common.eventbus.subscribers.MainSubscriber;
import raijin.storage.api.TasksManager;

/**
 * Supports auto-completion of several elements such as
 * commands, task names, tags, projects 
 * @author papa
 *
 */
public class AutoComplete {

  private SetTrie commandList;          //List of commands
  private SetTrie tagList;              //List of tags
  private SetTrie taskList;             //List of task names
  private TasksManager tasksManager;
  private com.google.common.eventbus.EventBus eventbus;
  
  public AutoComplete(TasksManager tasksManager) {
    commandList = new SetTrie();
    tagList = new SetTrie();
    taskList = new SetTrie();
    this.tasksManager =  tasksManager;
    this.eventbus = RaijinEventBus.getEventBus();
    
    TreeSet<String> tagList  = tasksManager.getTags();
    TreeSet<String> taskList = tasksManager.getTaskNames();
    setupList(tagList, taskList);
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
        String userInput = event.currentUserInput;
        updateSuggestions(userInput);
        
        if (Constants.KEY_TAB.match(event.keyEvent)) {
          
        }
        
      }};
  }

  String getLastWord(String userInput) {
    String[] tokens = userInput.toLowerCase().trim().split(" ");    //Sanitize string and retrieve tokens
    if (tokens.length == 0) {
      return "";
    } else {
      return userInput.substring(userInput.length() - 1, userInput.length());
    }
  }

  void updateSuggestions(String input) {
    String prefix = getLastWord(input);
  }

  public static void main(String[] args) {
    AutoComplete autoComplete = new AutoComplete(TasksManager.getManager());
  }

}
