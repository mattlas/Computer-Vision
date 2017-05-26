#!/bin/bash
#usageage  convert input_directory [output_directory]
#Takes all pgm in input directory and writes descriptors as binfile as output

indir=$1
mkdir "$indir"/descr
for file in "$indir"/*
do
    sift --descriptors=bin://"$indir"/descr/%.descr --first-octave=4 --levels=5 "$file"
done

