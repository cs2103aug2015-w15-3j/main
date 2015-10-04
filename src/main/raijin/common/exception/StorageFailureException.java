package raijin.common.exception;

@SuppressWarnings("serial")
public class StorageFailureException extends RuntimeException {

  public StorageFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
