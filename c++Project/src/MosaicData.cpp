//
// Created by 5dv115 on 5/8/17.
//

#include "MosaicData.h"
#include "DirectoryReader.h"
#include <cstring>
#include <iostream>
#include <thread>

#define NUM_THREADS 3

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
    //createThreads();
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

void MosaicData::extractFeaturePointsThreaded(std::vector<std::string> pgmFileNames , std::vector<FeaturePoints> featurePointList) {
    std::vector<std::string> tempList = pgmFileNames; //shares reference, should be copied.
    std::mutex readMutex;
    std::mutex writeMutex;
    while(!tempList.empty()){
        readMutex.lock();
        std::cout << "locked" << std::endl;
        std::string name = (std::string) tempList.back();
        tempList.pop_back();
        readMutex.unlock();
        std::cout << "unlocked" << std::endl;
        FeaturePoints *point = new FeaturePoints(name,1);
        point->calculatePoints();
        writeMutex.lock();
        featurePointList.push_back(*point);
        writeMutex.unlock();
    }

}

void MosaicData::createThreads() {
    std::vector<std::thread> threads;

    int rc;
    int i;

    for( i=0; i < NUM_THREADS; i++ ){
        std::cout << "main() : creating thread, " << i << std::endl;
        std::thread t1(extractFeaturePointsThreaded,pgmFileNames,featurePointList);
        threads.push_back(std::move(t1));

        /*rc = pthread_create(&threads[i], NULL, &extractFeaturePointsThreaded, (void*)i);

        if (rc){
            std::cout << "Error:unable to create thread," << rc << std::endl;
            exit(-1);
        }*/
    }
    for(int j =0; j < threads.size(); j++){
        std::thread t{std::move(threads.at(j))};
        t.join();
    }


}
