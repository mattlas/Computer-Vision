//
// Created by 5dv115 on 5/8/17.
//

#include "MosaicData.h"
#include "DirectoryReader.h"
#include "MatchPoints.h"
#include <thread>
#include <opencv2/imgproc.hpp>
#include <opencv2/features2d.hpp>

#define NUM_THREADS 4

MosaicData::MosaicData(std::string input_path, std::string pgm_path) {
    this->input_path = input_path;
    this->pgm_path = pgm_path;
    im = new imaq();
}

void MosaicData::startProcess() {
    readFiles(input_path);
    convertToPGM(pgm_path);
    readPGMFromFolder();
    createImages();
    extractFeaturePoints();
    createNeighbours();
    //createThreads();
    findPath();
    calculateHomographies(homographyList);
    stitchImages();
    clearData();

    emit finished();
}

void MosaicData::readFiles(std::string ip_path) {
    im->addDirectory(ip_path);
    im->readJPGfromFolder();
    /*for(std::string directory : directoryList){
        std::vector<std::string> temp = DirectoryReader::readDirectory(directory);
        fileNames.insert(std::end(fileNames), std::begin(temp), std::end(temp));
    }*/
}

void MosaicData::convertToPGM(std::string op_path) {

    im->convertToPGM(op_path);
    pgmFolder = op_path;
}

void MosaicData::readPGMFromFolder() {
    pgmFileNames = DirectoryReader::readDirectory(pgmFolder);
    std::sort(pgmFileNames.begin(), pgmFileNames.end());
}

void MosaicData::createImages() {
    int tempID = 0;
    for (int i = 0; i < pgmFileNames.size(); ++i) {

        ImageData *imageData = new ImageData(i);
        imageData->setPgm_path(pgmFileNames.at(i));
        imageData->setPath(im->getJpgFileNames().at(i));

	    imageData->setInfo(exif::read(imageData->getPath()));
        imageList.insert(imageList.begin()+i,imageData);
        tempID++;
    }
    referenceImage = imageList.at(imageList.size()/2);

}

void MosaicData::createNeighbours() {
    imagePairs = neighbours::pairs(imageList);
}

void MosaicData::extractFeaturePoints() {

    int id = 0;
    for(std::string file : pgmFileNames){
        FeaturePoints *points = new FeaturePoints(file,id);
        points->calculatePoints();
        featurePointList.push_back(*points);
        imageList.at(id)->setFeaturePoints(points);

        id++;
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

void MosaicData::addDirectory(std::string dir) {
    directoryList.push_back(dir);
}

void MosaicData::stitchImages() {
    //todo final stitching of images

}

void MosaicData::findPath() {
    int recursionDepth = 0;
    //This can be used to initialize all the images to the homographyList. This caused problems
    //because not all images had a path to the referenceimage which caused nullpointers.

    /*for (int j = 0; j < imagePairs.size(); ++j) {
        homographyList.push_back((HomographyData*) imagePairs.at(j).at(0));
    }*/

    std::vector<HomographyData*> startPairs = (std::vector<HomographyData*>) imagePairs.at(referenceImage->getId());
    HomographyData *startNode = startPairs.at(0);
    homographyList.push_back(startNode);
    cv::Mat startMat = Mat::eye(3, 3, CV_64F);
    startNode->setHomography(startMat);
    startNode->setRecursionDepth(recursionDepth);
    startNode->setPrevNode(NULL);
    for (int i = 0; i < startPairs.size()-1; ++i) {
        HomographyData *node = startPairs.at(i+1);
        calculatePairs(imagePairs.at(node->getImageData()->getId()),startNode,recursionDepth+1);
    }


}

void MosaicData::calculatePairs(std::vector<HomographyData*> currentlist, HomographyData *prevNode, int recursionDepth ) {
    HomographyData* currentNode = currentlist.at(0);
    if(recursionDepth < currentNode->getRecursionDepth()){
        homographyList.push_back(currentNode);
        currentNode->setRecursionDepth(recursionDepth);
        currentNode->setPrevNode(prevNode);
        for (int i = 0; i < currentlist.size()-1; ++i) {
            HomographyData* newNode = currentlist.at(i+1);
            calculatePairs(imagePairs.at(newNode->getImageData()->getId()),currentNode,recursionDepth+1);
        }
    }
}

void MosaicData::calculateHomographies(std::vector<HomographyData*> finalHomList) {
    for(HomographyData* data: finalHomList){
        recurseHomographies(data);
    }
}

void MosaicData::recurseHomographies(HomographyData* data) {
    if(data->getPrevNode() != NULL){
        MatchPoints *matcher = new MatchPoints(*data->getPrevNode()->getImageData()->getFeaturePoints(),
                                               *data->getImageData()->getFeaturePoints());
        cv::Mat homography = matcher->getHomography();
        if(data->getPrevNode()->getHomography().empty()){
            recurseHomographies(data->getPrevNode());
        }
        homography = homography * data->getPrevNode()->getHomography();
        data->setHomography(homography);
    }
}

void MosaicData::clearData() {
    delete im;
    //TODO clear memory.
   /* while(!homographyList.empty()){
        HomographyData *data = homographyList.back();
        homographyList.pop_back();
        delete data;
    }

    for(std::vector<HomographyData*> dataVector: imagePairs){
        while(!dataVector.empty()){
            HomographyData *data = dataVector.back();
            dataVector.pop_back();
            delete(data);
        }
    }
    while(!imageList.empty()){
        ImageData* data = imageList.back();
        imageList.pop_back();
        delete data;
    }

    delete referenceImage;

    while(!featurePointList.empty()){
        FeaturePoints data = featurePointList.back();
        featurePointList.pop_back();
        //delete data;
    }*/
}


