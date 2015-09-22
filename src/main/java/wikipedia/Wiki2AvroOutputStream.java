package wikipedia;

import java.io.IOException;

/**
 * Wiki2AvroOutputStream
 */
public interface Wiki2AvroOutputStream<T> {
  void append(T elem) throws IOException;
  void close() throws IOException;
}
