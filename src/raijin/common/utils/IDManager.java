package raijin.common.utils;

import java.util.LinkedList;

public class IDManager {
  private static IDManager instance = new IDManager();
  private int seed = 50; // Initial available IDs
  private LinkedList<Integer> pool;

  private IDManager() {
    pool = new LinkedList<Integer>();
    for (int i = 0; i < seed; i++) {
      pool.add(i);
    }
  }

  public static IDManager getInstance(){
    return instance;
  }

  public int getId() {
    return instance.pool.poll();
  }

  public void returnId(int id) {
    instance.pool.add(id);
  }


}
