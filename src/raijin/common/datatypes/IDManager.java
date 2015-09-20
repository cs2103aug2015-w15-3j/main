package raijin.common.datatypes;

import java.util.TreeSet;

public class IDManager {

  private static IDManager idManager = new IDManager();
  private static final int MAX_ID = 50;     //Set maximum number of todos
  private TreeSet<Integer> idPool = new TreeSet<Integer>();
  
  public IDManager() {
    initIdPool();
  }
  
  /**
   * 
   * @param MAX_ID 
   * creates initial pool of ids to be associated with tasks
   */
  private void initIdPool() {
    /*Id starts from 1 because usually 0 is reserved for root process*/
    for (int i = 1; i <= MAX_ID; i++) {
      idPool.add(i);
    }
  }

  public static IDManager getIdManager(){
    return idManager;
  }

  public void flushIdPool(){
    idPool.clear();
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

}
