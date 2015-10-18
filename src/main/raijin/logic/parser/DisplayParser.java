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
   * Currently allows for "p", "c", "a", "o" or dates for its options.
   * 
   * @returns   ParsedInputBuilder                  Appropriate ParsedInputBuilders accordingly.
   * @throws    IllegalCommandArgumentException
   */
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    String displayType = "p";
    for (int i = 1; i < wordsOfInput.length; i++) {
      if (wordsOfInput[i].matches(datePattern)) {
        builder.dateTime(new DateTime(dtFormat.formatDate(wordsOfInput[i], 1)));
      } else if (wordsOfInput[i].matches("p|c|a|o")){
        displayType = wordsOfInput[i];
      }
    }
    return builder.displayOptions(displayType);
  }
}
