package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class NoSuchTaskException extends RaijinException {

 private int taskID;

 public NoSuchTaskException(String message, int id) {
    super(message, Constants.Error.NoSuchTask);
    taskID = id;
 }
 
 public int getID() {
   return taskID;
 }

}
