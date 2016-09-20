#!/usr/bin/env bash

JAR_FILE=/home/ealmansi/dev/wiki-2-avro/target/Wiki2Avro-1.0-SNAPSHOT-jar-with-dependencies.jar
MAIN_CLASS=de.mpg.mpi.inf.d5.wikipedia.export.AvroExporter

java -cp  $JAR_FILE $MAIN_CLASS "$@"
