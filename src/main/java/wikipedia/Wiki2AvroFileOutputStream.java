package wikipedia;

import org.apache.avro.Schema;
import org.apache.avro.file.CodecFactory;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

/**
 * Wiki2AvroFileOutputStream
 */
public class Wiki2AvroFileOutputStream<T> implements Wiki2AvroOutputStream<T> {
  private DataFileWriter<T> dataFileWriter;

  public Wiki2AvroFileOutputStream(String outputFile,
                                   Class avroClass,
                                   Schema avroSchema,
                                   CodecFactory codec)
      throws IOException {
    @SuppressWarnings("unchecked") DatumWriter<T> datumWriter = new SpecificDatumWriter<>(avroClass);
    dataFileWriter = new DataFileWriter<>(datumWriter);
    dataFileWriter.setCodec(codec);
    dataFileWriter = dataFileWriter.create(avroSchema, new File(outputFile));
  }

  @Override
  public void append(T elem) throws IOException {
    dataFileWriter.append(elem);
  }

  @Override
  public void close() throws IOException {
    if (dataFileWriter != null) {
      dataFileWriter.close();
    }
  }
}
