/**
 * Class that handles DISPLAY parsing.
 * @author LingJie
 */
package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.datatypes.DateTime;
import raijin.common.exception.IllegalCommandArgumentException;

public class DisplayParser {

  private static final String datePattern = Constants.DATE_PATTERN;
  private static final DateTimeFormat dtFormat = new DateTimeFormat();
  
  private String[] wordsOfInput;
  private ParsedInput.ParsedInputBuilder builder;
  
  public DisplayParser(String[] wordsOfInput) {
    this.wordsOfInput = wordsOfInput;
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.DISPLAY);
  }
  
  /**
   * Method that parses display type input by user and responds accordingly.
   * Currently allows for "p", "c", "a", "o", "f" or dates for its options.
   * 
   * @returns   ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {  

    int indexOfDate = 0;
    String displayType = "p";
    boolean containsDate = false;
    
    for (int i = 0; i < wordsOfInput.length; i++) {   
      if (wordsOfInput[i].matches(datePattern) && !containsDate) {
        indexOfDate = i;
        containsDate = true;
      } else if (wordsOfInput[i].matches("completed|done|c")) {
        displayType = "c";
      } else if (wordsOfInput[i].matches("all|everything|a")) {
        displayType = "a";
      } else if (wordsOfInput[i].matches("due|expired|o|overdue")) {
        displayType = "o";
      } else if (wordsOfInput[i].matches("floating|f")) {
        displayType = "f";
      } 
    }
    
    // If date input exists, add date preposition inside so as to make use of AddParser's parsing.
    if (indexOfDate > 0) {
      addDateStartPreposition(indexOfDate);
    }
    
    ParsedInput parsed = new AddParser(builder, wordsOfInput, 2).process().createParsedInput();
    
    return builder.displayOptions(displayType).dateTime(parsed.getDateTime());
  }
  
  public void addDateStartPreposition(int indexOfDate) {
    String[] newWordsOfInput = new String[wordsOfInput.length+1];
    newWordsOfInput[0] = "display";
    for (int i = 1; i < indexOfDate; i++) {
      newWordsOfInput[i] = wordsOfInput[i];
    }
    
    newWordsOfInput[indexOfDate] = "by";
    
    for (int i = indexOfDate+1; i < newWordsOfInput.length; i++) {
      newWordsOfInput[i] = wordsOfInput[i-1];
    }
    wordsOfInput = newWordsOfInput;
  }
}
