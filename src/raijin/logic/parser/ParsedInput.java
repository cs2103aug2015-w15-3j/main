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
  
  /*Common properties among all commands*/
  private Constants.Command command;            //Type of command from user

  /*Properties used by Add and Edit*/
  private String name;                          //Description of task
  private DateTime dateTime;
  
  /*Display properties*/
  private char displayOptions;                  //Pending or completed view of tasks
  
  /*Delete, done and edit properties*/
  private int id;                               //Id associated with a task

  //===========================================================================
  // Private constructor
  //===========================================================================
  
  /*Private contructor because only used by nested Builder class*/
  private ParsedInput(
    final Constants.Command command,
    final int id,
    final String name,
    final DateTime dateTime, 
    final char displayOptions) {

    this.command = command;
    this.id = id;
    this.name = name;
    this.dateTime = dateTime;
    this.displayOptions = displayOptions;
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

  public String getName() {
    return name;
  }

  public char getDisplayOptions() {
    return displayOptions;
  }

  public DateTime getDateTime() {
    return dateTime;
  }
  
  //===========================================================================
  // Builder Class
  //===========================================================================

  public static class ParsedInputBuilder {
    private final Constants.Command command;
    private int id;
    private String name;
    private DateTime dateTime;
    private char displayOptions;
    
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
    
    public ParsedInputBuilder displayOptions(final char displayOptions) {
      this.displayOptions = displayOptions;
      return this;
    }
    
    /*Generate parsedinput when client is done building the object*/
    public ParsedInput createParsedInput() {
      return new ParsedInput(command, id, name, dateTime, displayOptions);
    }

  }

}
