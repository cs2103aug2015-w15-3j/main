package raijin.common.utils;

import java.util.HashMap;
import java.util.TreeSet;

import raijin.common.datatypes.Task;

public class IDManager {

  private static IDManager idManager = new IDManager();
  private int MAX_ID = 200;     //Set maximum number of todos
  private TreeSet<Integer> idPool = new TreeSet<Integer>();
  
  public IDManager() {
    initIdPool(1);
  }
  
  public static IDManager getIdManager(){
    return idManager;
  }

  /*Used with care because refreshes all id pools*/
  public void flushIdPool(){
    idPool.clear();
    initIdPool(1);
  }

  //@TODO catch no ids error
  public int getId() {
    if (idPool.isEmpty()) {                         //If id is depleted then add more
      increaseLimit();
    }
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
    if (pendingTasks.size() >= MAX_ID) {
      int initial = MAX_ID;
      MAX_ID += pendingTasks.size();
      initIdPool(initial+1);
    }

    for (int id : pendingTasks.keySet()) {
      idPool.remove(id);
    }
  }

  void initIdPool(int initial) {
    /*Id starts from 1 because usually 0 is reserved for root process*/
    for (int i = initial; i <= MAX_ID; i++) {
      idPool.add(i);
    }
  }

  void increaseLimit() {
    int OLD_MAX_ID = MAX_ID;
    MAX_ID += MAX_ID*2;
    initIdPool(OLD_MAX_ID+1);
  }

  void setMaxId(int MAX_ID) {
    this.MAX_ID = MAX_ID;
  }

  int getMaxId() {
    return MAX_ID;
  }

}
