#!/usr/bin/env bash

wiki_id=$1
dump_date=$2

dump_dir=/home/ealmansi/data/${wiki_id}/${dump_date}/dump/
avro_dir=/home/ealmansi/data/${wiki_id}/${dump_date}/avro/

page_metadata_dir=$avro_dir/page_metadata/
revision_metadata_dir=$avro_dir/revision_metadata/
revision_wikilinks_dir=$avro_dir/revision_wikilinks/

mkdir -p $avro_dir
mkdir -p $page_metadata_dir
mkdir -p $revision_metadata_dir
mkdir -p $revision_wikilinks_dir

for file_path in $dump_dir/*.bz2
do
  # Get clean filename.
  filename_bz2=$(basename "$file_path")   # Remove path.
  filename_xml="${filename_bz2%.*}"       # Remove bz2 extension.
  filename="${filename_xml%.*}"           # Remove xml extension.
  # Create output files.
  page_metadata_file=$page_metadata_dir/$filename.p.avro
  revision_metadata_file=$revision_metadata_dir/$filename.r.avro
  revision_wikilinks_file=$revision_wikilinks_dir/$filename.w.avro
  touch $page_metadata_file
  touch $revision_metadata_file
  touch $revision_wikilinks_file
  # Run export job.
  (echo "Starting part: ${filename}" && \
    cat $file_path | bunzip2 | ./avro-exporter.sh \
      -p $page_metadata_file \
      -r $revision_metadata_file \
      -w $revision_wikilinks_file \
    && echo "Done with part: ${filename}") &
  disown
done


