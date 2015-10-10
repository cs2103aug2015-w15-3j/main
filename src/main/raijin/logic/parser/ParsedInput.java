package raijin.logic.parser;

import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.datatypes.Status;

/**
 * 
 * @author papa
 */
public class ParsedInput {
  
  //===========================================================================
  // Properties of Parsed Input 
  //===========================================================================
  
  private Constants.Command command;            
  private int id = 0;                              
  private String name;                             
  private DateTime dateTime;
  private String displayOptions;                  
  private TreeSet<String> tags = new TreeSet<String>();
  private int subTaskOf = 0;                    //Default to sentinel value 0
  private String priority = "m";              //Default to medium priority

  //===========================================================================
  // Private constructor
  //===========================================================================
  
  /*Private contructor because only used by nested Builder class*/
  private ParsedInput(
    final Constants.Command command,
    final int id,
    final String name,
    final DateTime dateTime, 
    final String displayOptions, 
    final TreeSet<String> tags,
    final int subTaskOf,
    final String priority) {

    this.command = command;
    this.id = id;
    this.name = name;
    this.dateTime = dateTime;
    this.displayOptions = displayOptions;
    this.tags = tags;
    this.subTaskOf = subTaskOf;
    this.priority = priority;
  }

  //===========================================================================
  // Getters 
  //===========================================================================

  public Constants.Command getCommand() {
    return command;
  }

  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public DateTime getDateTime() {
    return dateTime;
  }

  public String getDisplayOptions() {
    return displayOptions;
  }
  
  public TreeSet<String> getTags() {
    return tags;
  }

  public int getSubTaskOf() {
    return subTaskOf;
  }

  public String getPriority() {
    return priority;
  }

  //===========================================================================
  // Builder Class
  //===========================================================================

  public static class ParsedInputBuilder {
    private final Constants.Command command;
    private int id;
    private String name;
    private DateTime dateTime;
    private String displayOptions;
    private TreeSet<String> tags;
    private int subTaskOf;
    private String priority;
    
    public ParsedInputBuilder(final Constants.Command command) {
      this.command = command;
    }
    
    public ParsedInputBuilder id(final int id) {
      this.id = id;
      return this;
    }
    
    public ParsedInputBuilder name(final String name) {
      this.name = name;
      return this;
    }
    
    public ParsedInputBuilder dateTime(final DateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }
    
    public ParsedInputBuilder displayOptions(final String displayOptions) {
      this.displayOptions = displayOptions;
      return this;
    }
    
    public ParsedInputBuilder tag(final TreeSet<String> tags) {
      this.tags = tags;
      return this;
    }
    
    public ParsedInputBuilder subTaskOf(final int subTaskOf) {
      this.subTaskOf = subTaskOf;
      return this;
    }

    public ParsedInputBuilder priority(final String priority) {
      this.priority = priority;
      return this;
    }

    /*Generate parsedinput when client is done building the object*/
    public ParsedInput createParsedInput() {
      return new ParsedInput(command, id, name, dateTime, displayOptions, 
                             tags, subTaskOf, priority);
    }

  }

}
