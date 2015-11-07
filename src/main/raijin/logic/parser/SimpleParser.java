/**
 * Class that defines the basic parsing unit that reads and process the user input.
 * 
 * @author LingJie
 */
//@@author A0124745E
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandException;
import raijin.common.exception.IllegalCommandArgumentException;
import raijin.common.exception.FailedToParseException;

public class SimpleParser implements ParserInterface {
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  /**
   * Parses the user's input and creates corresponding ParsedInput object.
   * 
   * @param userInput   String of user input into the system.
   * @return            ParsedInput object based on user input.
   * @throws Exception  When invalid input is detected.
   */
  public ParsedInput parse(String userInput) throws FailedToParseException {
    wordsOfInput = userInput.trim().split("\\s+");
    
    try {
      if (isFirstWord("add|\\+")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.ADD);
        builder = new AddParser(builder, wordsOfInput, 0).process();
      } else if (isFirstWord("edit|change")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EDIT);
        builder = new EditParser(builder, wordsOfInput).process();
      } else if (isFirstWord("delete|del|remove|rem|trash")) {
        builder = new DeleteParser(wordsOfInput).process();
      } else if (isFirstWord("done|finish|fin|mark")) {
        builder = new DoneParser(wordsOfInput).process();
      } else if (isFirstWord("display|show")) {
        builder = new DisplayParser(wordsOfInput).process();
      } else if (isFirstWord("help")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.HELP);
      } else if (isFirstWord("undo")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.UNDO);
      } else if (isFirstWord("redo")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.REDO);
      } else if (isFirstWord("exit|zao")) {
        builder = new ParsedInput.ParsedInputBuilder(Constants.Command.EXIT);
      } else if (isFirstWord("set")) {
        builder = new SetParser(wordsOfInput).process();
      } else if (isFirstWord("search|find")) {
        builder = new SearchParser(wordsOfInput).process();
      } else {
        throw new IllegalCommandException(Constants.FEEDBACK_INVALID_CMD, wordsOfInput[0]);
      }
    } catch (IllegalCommandException e1) {
      throw new FailedToParseException(e1.getMessage(), userInput, e1);
    } catch (IllegalCommandArgumentException e2) {
      throw new FailedToParseException(e2.getMessage(), userInput, e2);
    }
     
    return builder.createParsedInput();
  }
  
  /**
   * Method that checks first String of String array for a particular word.
   * 
   * @param word    Word to check for in the first element of array.
   * @return        boolean true or false whether it contains the word.
   */
  public boolean isFirstWord(String word) {
    return wordsOfInput[0].toLowerCase().matches(word);
  }
  
}
