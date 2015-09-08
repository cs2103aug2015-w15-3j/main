package raijin.common.datatypes;

public class UserInput {
  
  private String command;
  private String[] keywords;
  private String rawInput;
  
  public UserInput(String command, String[] keywords, 
      String rawInput) {
    this.command = command;
    this.keywords = keywords;
    this.rawInput = rawInput;
  }

  public String getCommand() {
    return this.command;
  }

  public String[] getKeywords() {
    return this.keywords;
  }

  public String getRawInput() {
    return rawInput;
  }

}
