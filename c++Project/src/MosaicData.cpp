//
// Created by 5dv115 on 5/8/17.
//

#include "MosaicData.h"
#include "DirectoryReader.h"
#include <thread>

#define NUM_THREADS 4

MosaicData::MosaicData(void) {
    im = new imaq();
    if(!(im == new imaq)){
        std::cout << "out of memmory ERROR" << std::endl;
    }
}




void MosaicData::startProcess(const char *ip_path, const char *op_path) {
    readFiles(ip_path);
    convertToPGM(op_path);
    delete im;
    //readPGMFromFolder();
    //extractFeaturePoints();
    createThreads();
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

void MosaicData::readFiles(const char *ip_path) {
    im->addDirectory(ip_path);
    im->readJPGfromFolder();
    /*for(std::string directory : directoryList){
        std::vector<std::string> temp = DirectoryReader::readDirectory(directory);
        fileNames.insert(std::end(fileNames), std::begin(temp), std::end(temp));
    }*/
}

void MosaicData::convertToPGM(const char *op_path) {
    im->convertToPGM(op_path);
    pgmFolder = op_path;
    //pgmFolder = directoryList.at(0); //replace this line with actual folder once its implemented
}

void MosaicData::extractFeaturePointsThreaded() {
    while(!pgmFileNames.empty()){
        readMutex.lock();
        std::string name = (std::string) pgmFileNames.back();
        pgmFileNames.pop_back();
        readMutex.unlock();
        FeaturePoints *point = new FeaturePoints(name,1);
        point->calculatePoints();

        writeMutex.lock();
        featurePointList.push_back(*point);
        writeMutex.unlock();

    }

}

void MosaicData::createThreads() {
    std::vector<std::thread> threads;
    std::vector<std::string> tempList = pgmFileNames;

    int rc;
    int i;
    MosaicData* mosaicData = this;
    for( i=0; i < NUM_THREADS; i++ ){
        std::thread t1(classWrapper,mosaicData);
        threads.push_back(std::move(t1));

    }
    for(int j =0; j < threads.size(); j++){
        std::thread t{std::move(threads.at(j))};
        t.join();
    }
    pgmFileNames = tempList;

}

void MosaicData::classWrapper(MosaicData *mosaicData) {
    mosaicData->extractFeaturePointsThreaded();
}

void MosaicData::readPGMFromFolder() {
    pgmFileNames = DirectoryReader::readDirectory(pgmFolder);
}

void MosaicData::addDirectory(std::string dir) {
    directoryList.push_back(dir);
}
