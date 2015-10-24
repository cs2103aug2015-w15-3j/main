package raijin.logic.parser;

import java.util.Collection;
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
  private TreeSet<Integer> id = new TreeSet<Integer>();
  private TreeSet<String> name = new TreeSet<String>();
  private DateTime dateTime;
  private String displayOptions;                  
  private TreeSet<String> tags = new TreeSet<String>();
  private int subTaskOf = 0;                                     //Default to sentinel value 0
  private String priority;              
  private String helperOption;                                   //Stores argument for helper command
  private String project;

  //===========================================================================
  // Private constructor
  //===========================================================================
  
  /*Private contructor because only used by nested Builder class*/
  private ParsedInput(
    final Constants.Command command,
    final TreeSet<Integer> id,
    final TreeSet<String> name,
    final DateTime dateTime, 
    final String displayOptions, 
    final TreeSet<String> tags,
    final int subTaskOf,
    final String priority,
    final String helperOption,
    final String project) {

    this.command = command;
    this.id = id;
    this.name = name;
    this.dateTime = dateTime;
    this.displayOptions = displayOptions;
    this.tags = tags;
    this.subTaskOf = subTaskOf;
    this.priority = priority;
    this.helperOption = helperOption;
    this.project = project;
  }

  //===========================================================================
  // Getters 
  //===========================================================================

  public Constants.Command getCommand() {
    return command;
  }

  public int getId() {
    if (id.isEmpty()) {
      return 0;                     // If not assigned, default to 0
    }
    return id.first();
  }
  
  /*Returns a group of ids for bulk processing*/
  public TreeSet<Integer> getIds() {
    return id;
  }

  public void setId(TreeSet<Integer> ids) {
    this.id = ids;
  }

  public String getName() {
    if (name.isEmpty()) {           // If not assigned, default to empty string
      return "";
    }
    return name.first();
  }

  /*Returns a group of names for bulk processing*/
  public TreeSet<String> getNames() {
    return name;
  }

  public DateTime getDateTime() {
    return dateTime;
  }

  public String getDisplayOptions() {
    return displayOptions;
  }
  
  public void setTags(TreeSet<String> tags) {
    this.tags = tags;
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
  
  public String getHelperOption() {
    return helperOption;
  }

  public String getProject() {
    return project;
  }

  //===========================================================================
  // Builder Class
  //===========================================================================

  public static class ParsedInputBuilder {
    private final Constants.Command command;
    private TreeSet<Integer> id = new TreeSet<Integer>();
    private TreeSet<String> name = new TreeSet<String>();
    private DateTime dateTime;
    private String displayOptions;
    private TreeSet<String> tags = new TreeSet<String>();
    private int subTaskOf;
    private String priority = Constants.PRIORITY_MID;
    private String helperOption;
    private String project;
    
    public ParsedInputBuilder(final Constants.Command command) {
      this.command = command;
    }
    
    public ParsedInputBuilder id(final int id) {
      this.id.add(id);
      return this;
    }
    
    public ParsedInputBuilder id(final Collection<Integer> ids) {
      this.id.addAll(ids);
      return this;
    }

    public ParsedInputBuilder name(final String name) {
      this.name.add(name);
      return this;
    }
    
    public ParsedInputBuilder name(final Collection<String> names) {
      this.name.addAll(names);
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

    public ParsedInputBuilder helperOption(final String helperOption) {
      this.helperOption = helperOption;
      return this;
    }

    public ParsedInputBuilder project(final String project) {
      this.project = project;
      return this;
    }

    /*Generate parsedinput when client is done building the object*/
    public ParsedInput createParsedInput() {
      return new ParsedInput(command, id, name, dateTime, displayOptions, 
                             tags, subTaskOf, priority, helperOption,
                             project);
    }

  }

}
