//@@author A0112213E

package raijin.common.exception;

import raijin.common.datatypes.Constants;

@SuppressWarnings("serial")
public class NoSuchTaskException extends RaijinException {

 private int taskID;

 public NoSuchTaskException(String message, int id) {
    super(message, Constants.Error.NoSuchTask);
    taskID = id;                                //Task id that does not exists
 }
 
 public int getID() {
   return taskID;
 }

}
