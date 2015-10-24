package raijin.logic.parser;

import java.util.TreeSet;

import raijin.common.datatypes.Constants;
import raijin.common.exception.IllegalCommandArgumentException;

public class SearchParser {
  
  private ParsedInput.ParsedInputBuilder builder;
  private String[] wordsOfInput;
  private TreeSet<Integer> ids;
  private TreeSet<String> tags;
  private String keywords;
 
  public SearchParser(String[] wordsOfInput) {
    builder = new ParsedInput.ParsedInputBuilder(Constants.Command.SEARCH);
    this.wordsOfInput = wordsOfInput;
    ids = new TreeSet<Integer>();
    tags = new TreeSet<String>();
    keywords = "";
  }
  
  public ParsedInput.ParsedInputBuilder process() throws IllegalCommandArgumentException {
    if (wordsOfInput.length < 2) {
      throw new IllegalCommandArgumentException(Constants.FEEDBACK_NO_KEYWORDS,
          Constants.CommandParam.NAME);
    }
    
    for (int i = 1; i < wordsOfInput.length; i++) {
      if (wordsOfInput[i].matches("!h|!l|!m")) {
        builder.priority(wordsOfInput[i].substring(1));
      } else if (wordsOfInput[i].indexOf("#") == 0) {
        tags.add(wordsOfInput[i].substring(1));
      } else { 
        try {
          ids.add(Integer.parseInt(wordsOfInput[i]));
          keywords += wordsOfInput[i] + " ";
        } catch (NumberFormatException e) {
          keywords += wordsOfInput[i] + " ";
        }
      }
    }
    
    return builder.name(keywords.trim()).id(ids).tag(tags);
  }
}
