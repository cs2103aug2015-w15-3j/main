package raijin.logic.parser;

import raijin.common.exception.FailedToParseException;

public interface ParserInterface {

  public ParsedInput parse(String userInput) throws FailedToParseException;

}
