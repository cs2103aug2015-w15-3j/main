package raijin.logic.parser;

import raijin.common.datatypes.DateTime;

/**
 * 
 * @author papa
 * Setters set to allow only package visibility 
 */
public final class Command {
  
  private int id;
  private String name;
  private DateTime dateTime;
  private String keyword;
  private char displayOption;

  private Command() {}

  /*Most simple command consists of only id. Used for exit, delete, mark as done*/
  public static Command buildSimple(int id) {
    Command command = new Command();
    command.setId(id);
    return command;
  }

  /*Creates command that is undoable such as add, edit*/
  public static Command buildUndoable(int id, String name, DateTime dateTime) {
    Command command = new Command();
    command.setId(id);          //Set id to 0 for add command
    command.setName(name);
    command.setDateTime(dateTime);
    return command;
  }

  public int getId() {
    return id;
  }
  
  void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  public DateTime getDateTime() {
    return dateTime;
  }

  void setDateTime(DateTime dateTime) {
    this.dateTime = dateTime;
  }

  public String getKeyword() {
    return keyword;
  }

  void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public char getDisplayOption() {
    return displayOption;
  }

  void setDisplayOption(char displayOption) {
    this.displayOption = displayOption;
  }
  
  
  

}
