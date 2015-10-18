package raijin.logic.parser;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class SearchParser {
  
  private ParsedInput.ParsedInputBuilder builder;
  private String[] wordsOfInput;
  private int id;
  private String keywords;
 
  public SearchParser(String[] wordsOfInput) {
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.SEARCH);
    this.wordsOfInput = wordsOfInput;
    keywords = "";
  }
  
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    if (wordsOfInput.length < 2) {
      throw new IllegalCommandArgumentException("Please specify keywords to search!",
                                                Constants.CommandParam.NAME);
    }
    
    for (int i = 1; i < wordsOfInput.length; i++) {
      try {
        id = Integer.parseInt(wordsOfInput[i]);
        keywords += wordsOfInput[i] + " ";
      } catch (NumberFormatException e) {
        keywords += wordsOfInput[i] + " ";
      }
    }
    
    return builder.id(id).name(keywords.trim());
  }
}
