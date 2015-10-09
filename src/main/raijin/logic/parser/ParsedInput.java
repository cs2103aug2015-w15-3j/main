package raijin.logic.parser;

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
  private String tag;
  private char priority = 'm';              //Default to medium priority

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
    final String tag,
    final char priority) {

    this.command = command;
    this.id = id;
    this.name = name;
    this.dateTime = dateTime;
    this.displayOptions = displayOptions;
    this.tag = tag;
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
  
  public String getTag() {
    return tag;
  }

  public char getPriority() {
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
    private String tag;
    private char priority;
    
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
    
    public ParsedInputBuilder tag(final String tag) {
      this.tag = tag;
      return this;
    }
    
    public ParsedInputBuilder priority(final char priority) {
      this.priority = priority;
      return this;
    }

    /*Generate parsedinput when client is done building the object*/
    public ParsedInput createParsedInput() {
      return new ParsedInput(command, id, name, dateTime, displayOptions, 
                             tag, priority);
    }

  }

}
