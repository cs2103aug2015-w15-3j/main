package raijin.util;

public class UserInput {
  
  private String command;
  private String[] keywords;
  
  public UserInput(String command, String[] keywords) {
    this.command = command;
    this.keywords = keywords;
  }

  public String getCommand() {
    return this.command;
  }

  public String[] getKeywords() {
    return this.keywords;
  }

}
