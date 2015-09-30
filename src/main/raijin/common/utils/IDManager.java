package raijin.common.utils;

import java.util.HashMap;
import java.util.TreeSet;

import raijin.common.datatypes.Task;

public class IDManager {

  private static IDManager idManager = new IDManager();
  private static final int MAX_ID = 50;     //Set maximum number of todos
  private TreeSet<Integer> idPool = new TreeSet<Integer>();
  
  public IDManager() {
    initIdPool();
  }
  
  void initIdPool() {
    /*Id starts from 1 because usually 0 is reserved for root process*/
    for (int i = 1; i <= MAX_ID; i++) {
      idPool.add(i);
    }
  }

  public static IDManager getIdManager(){
    return idManager;
  }

  /*Used with care because refreshes all id pools*/
  public void flushIdPool(){
    idPool.clear();
    initIdPool();
  }

  //@TODO catch no ids error
  public int getId() {
    return idPool.pollFirst();
  }

  /**
   * 
   * @param id
   * recycle unused id into the idPool
   */
  public void returnId(int id) {
    idPool.add(id);
  }


  public TreeSet<Integer> getIdPool(){
    return idPool;
  }
  
  public void setIdPool(TreeSet<Integer> idPool) {
    this.idPool = idPool;
  }

  public void updateIdPool(HashMap<Integer, Task> pendingTasks) {
    for (int id : pendingTasks.keySet()) {
      idPool.remove(id);
    }
  }

  int getMaxId() {
    return MAX_ID;
  }

}