//@@author A0112213E

package raijin.common.exception;

@SuppressWarnings("serial")
public class StorageFailureException extends RuntimeException {

  public StorageFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
