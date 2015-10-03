package raijin.common.datatypes;

import java.util.ArrayList;

public class ListDisplayContainer implements DisplayContainer {

  private ArrayList<Task> tasks = new ArrayList<Task>();

  public int getRealId(int index) throws IndexOutOfBoundsException {
    return tasks.get(index - 1).getId();
  }

  public boolean isEmpty() {
    return tasks.isEmpty();
  }


}
