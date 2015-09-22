package wikipedia;

import java.io.IOException;
import java.util.List;

/**
 * Wiki2AvroMockOutputStream
 */
public class Wiki2AvroMockOutputStream<T> implements Wiki2AvroOutputStream<T> {
  private final List<T> outputList;

  public Wiki2AvroMockOutputStream(List<T> outputList) {
    this.outputList = outputList;
  }

  @Override
  public void append(T elem) throws IOException {
    outputList.add(elem);
  }

  @Override
  public void close() throws IOException {
  }
}
