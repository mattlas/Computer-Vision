#!/bin/bash

#This script is to convert an entire folder of images into pgm format
#so it can be used with sift.
#Requires GM (sudo apt install gm)
#Usage  convert input_directory [output_directory]


indir=$1
outdir=${2:-output}
mkdir "$outdir"
for file in "$indir"/*
do
  filename=$(basename "$file")
  filename="${filename%.*}"
  filename="$outdir/$filename.pgm"
  echo "$filename"
  gm convert "$file" "$filename"
done
