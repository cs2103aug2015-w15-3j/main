package raijin.logic.api;

import raijin.common.datatypes.Status;
import raijin.logic.parser.ParsedInput;
import raijin.storage.api.History;
import raijin.storage.api.Memory;

public interface CommandRunner {

  public Memory memory = Memory.getMemory();
  public History history = History.getHistory();
  public Status execute(ParsedInput input);
}
