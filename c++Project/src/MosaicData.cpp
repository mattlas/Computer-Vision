//
// Created by 5dv115 on 5/8/17.
//

#include "MosaicData.h"
#include "DirectoryReader.h"
#include <cstring>
#include <iostream>
#include <memory>

MosaicData::MosaicData(void) {

}


void MosaicData::readPGMFromFolder() {
    pgmFileNames = DirectoryReader::readDirectory(pgmFolder);
}

void MosaicData::addDirectory(std::string dir) {
    directoryList.push_back(dir);
}

void MosaicData::startProcess() {
    readFiles();
    convertToPGM();
    readPGMFromFolder();
    extractFeaturePoints();
    ubcMatch();
}

void MosaicData::extractFeaturePoints() {
    int id = 0;
    for(std::string file : pgmFileNames){
        FeaturePoints *points = new FeaturePoints(file,id);
        points->calculatePoints();
        featurePointList.push_back(*points);
        id++;
    }
}

void MosaicData::ubcMatch() {

    //TODO Match points with ubc match
}

void MosaicData::readFiles() {
    for(std::string directory : directoryList){
        std::vector<std::string> temp = DirectoryReader::readDirectory(directory);
        fileNames.insert(std::end(fileNames), std::begin(temp), std::end(temp));
    }
}

void MosaicData::convertToPGM() {
    //TODO convert files, now located in the vector fileNames to a pgmfolder and save folder path to variable pgmFolder
    pgmFolder = directoryList.at(0); //replace this line with actual folder once its implemented
}
