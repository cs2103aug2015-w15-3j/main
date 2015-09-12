package raijin.storage.api;

import java.util.HashMap;

import com.google.common.collect.TreeMultimap;

import raijin.common.datatypes.Task;
import raijin.storage.handler.StorageHandler;

public class Memory {

  private static Memory memory = new Memory();
  private HashMap<Integer, Task> taskMap;
  private TreeMultimap<String, Integer> searchMap;

  private Memory() {
    taskMap = new HashMap<Integer, Task>();
  }
  
  public static Memory getMemory() {
    return memory;
  }

}
