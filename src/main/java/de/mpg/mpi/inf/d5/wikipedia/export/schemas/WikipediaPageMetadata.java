/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package de.mpg.mpi.inf.d5.wikipedia.export.schemas;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class WikipediaPageMetadata extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"WikipediaPageMetadata\",\"namespace\":\"de.mpg.mpi.inf.d5.wikipedia.export.schemas\",\"fields\":[{\"name\":\"title\",\"type\":[\"null\",\"string\"]},{\"name\":\"ns\",\"type\":[\"null\",\"int\"]},{\"name\":\"id\",\"type\":[\"null\",\"long\"]},{\"name\":\"redirect\",\"type\":[\"null\",\"string\"]}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence title;
  @Deprecated public java.lang.Integer ns;
  @Deprecated public java.lang.Long id;
  @Deprecated public java.lang.CharSequence redirect;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public WikipediaPageMetadata() {}

  /**
   * All-args constructor.
   */
  public WikipediaPageMetadata(java.lang.CharSequence title, java.lang.Integer ns, java.lang.Long id, java.lang.CharSequence redirect) {
    this.title = title;
    this.ns = ns;
    this.id = id;
    this.redirect = redirect;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return title;
    case 1: return ns;
    case 2: return id;
    case 3: return redirect;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: title = (java.lang.CharSequence)value$; break;
    case 1: ns = (java.lang.Integer)value$; break;
    case 2: id = (java.lang.Long)value$; break;
    case 3: redirect = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'title' field.
   */
  public java.lang.CharSequence getTitle() {
    return title;
  }

  /**
   * Sets the value of the 'title' field.
   * @param value the value to set.
   */
  public void setTitle(java.lang.CharSequence value) {
    this.title = value;
  }

  /**
   * Gets the value of the 'ns' field.
   */
  public java.lang.Integer getNs() {
    return ns;
  }

  /**
   * Sets the value of the 'ns' field.
   * @param value the value to set.
   */
  public void setNs(java.lang.Integer value) {
    this.ns = value;
  }

  /**
   * Gets the value of the 'id' field.
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.Long value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'redirect' field.
   */
  public java.lang.CharSequence getRedirect() {
    return redirect;
  }

  /**
   * Sets the value of the 'redirect' field.
   * @param value the value to set.
   */
  public void setRedirect(java.lang.CharSequence value) {
    this.redirect = value;
  }

  /** Creates a new WikipediaPageMetadata RecordBuilder */
  public static de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder newBuilder() {
    return new de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder();
  }
  
  /** Creates a new WikipediaPageMetadata RecordBuilder by copying an existing Builder */
  public static de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder newBuilder(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder other) {
    return new de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder(other);
  }
  
  /** Creates a new WikipediaPageMetadata RecordBuilder by copying an existing WikipediaPageMetadata instance */
  public static de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder newBuilder(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata other) {
    return new de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder(other);
  }
  
  /**
   * RecordBuilder for WikipediaPageMetadata instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<WikipediaPageMetadata>
    implements org.apache.avro.data.RecordBuilder<WikipediaPageMetadata> {

    private java.lang.CharSequence title;
    private java.lang.Integer ns;
    private java.lang.Long id;
    private java.lang.CharSequence redirect;

    /** Creates a new Builder */
    private Builder() {
      super(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ns)) {
        this.ns = data().deepCopy(fields()[1].schema(), other.ns);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.id)) {
        this.id = data().deepCopy(fields()[2].schema(), other.id);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.redirect)) {
        this.redirect = data().deepCopy(fields()[3].schema(), other.redirect);
        fieldSetFlags()[3] = true;
      }
    }
    
    /** Creates a Builder by copying an existing WikipediaPageMetadata instance */
    private Builder(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata other) {
            super(de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.SCHEMA$);
      if (isValidValue(fields()[0], other.title)) {
        this.title = data().deepCopy(fields()[0].schema(), other.title);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.ns)) {
        this.ns = data().deepCopy(fields()[1].schema(), other.ns);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.id)) {
        this.id = data().deepCopy(fields()[2].schema(), other.id);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.redirect)) {
        this.redirect = data().deepCopy(fields()[3].schema(), other.redirect);
        fieldSetFlags()[3] = true;
      }
    }

    /** Gets the value of the 'title' field */
    public java.lang.CharSequence getTitle() {
      return title;
    }
    
    /** Sets the value of the 'title' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder setTitle(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.title = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'title' field has been set */
    public boolean hasTitle() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'title' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder clearTitle() {
      title = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'ns' field */
    public java.lang.Integer getNs() {
      return ns;
    }
    
    /** Sets the value of the 'ns' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder setNs(java.lang.Integer value) {
      validate(fields()[1], value);
      this.ns = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'ns' field has been set */
    public boolean hasNs() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'ns' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder clearNs() {
      ns = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'id' field */
    public java.lang.Long getId() {
      return id;
    }
    
    /** Sets the value of the 'id' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder setId(java.lang.Long value) {
      validate(fields()[2], value);
      this.id = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'id' field has been set */
    public boolean hasId() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'id' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder clearId() {
      id = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'redirect' field */
    public java.lang.CharSequence getRedirect() {
      return redirect;
    }
    
    /** Sets the value of the 'redirect' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder setRedirect(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.redirect = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'redirect' field has been set */
    public boolean hasRedirect() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'redirect' field */
    public de.mpg.mpi.inf.d5.wikipedia.export.schemas.WikipediaPageMetadata.Builder clearRedirect() {
      redirect = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    public WikipediaPageMetadata build() {
      try {
        WikipediaPageMetadata record = new WikipediaPageMetadata();
        record.title = fieldSetFlags()[0] ? this.title : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.ns = fieldSetFlags()[1] ? this.ns : (java.lang.Integer) defaultValue(fields()[1]);
        record.id = fieldSetFlags()[2] ? this.id : (java.lang.Long) defaultValue(fields()[2]);
        record.redirect = fieldSetFlags()[3] ? this.redirect : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}