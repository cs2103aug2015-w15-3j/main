package raijin.component.parser;

import java.util.Arrays;

import raijin.util.Task.UserInput;

public class SimpleParser implements ParserInterface{

  private static SimpleParser instance = new SimpleParser();

  private SimpleParser() {}

  public static SimpleParser getParser(){
   return instance; 
  }

  public String[] getTokens(String userInput) {
    return userInput.trim().split(" ");     //Sanitize user input before splitting
  }

  @Override
  public UserInput getParsedInput(String userInput) {
    String[] tokens = getTokens(userInput);
    return new UserInput(tokens[0], Arrays.copyOfRange(
        tokens, 1, tokens.length), userInput);
  }
  


}
