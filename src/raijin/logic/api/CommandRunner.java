package raijin.logic.api;

import raijin.common.datatypes.Status;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.Memory;

public interface CommandRunner {

  public Memory memory = Memory.getMemory();
  public Status execute(ParsedInput input);
}
