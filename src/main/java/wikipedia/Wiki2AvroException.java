package wikipedia;

/**
 * Wiki2AvroException
 */
public class Wiki2AvroException extends Exception {
  public Wiki2AvroException(String message) {
    super(message);
  }

  public Wiki2AvroException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
