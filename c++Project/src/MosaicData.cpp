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
    std::cout << "readFiles" << std::endl;
    readFiles(input_path);
    std::cout << "convert to pgm " << std::endl;
    convertToPGM(pgm_path);
    std::cout << "read pgm" << std::endl;
    readPGMFromFolder();
    std::cout << "create images" << std::endl;
    createImages();
    extractFeaturePoints();
    std::cout << "create neighbours" << std::endl;
    createNeighbours();

    //delete im;
    std::cout << "extracting featurepoints" << std::endl;
    std::cout << "create threads" << std::endl;
    //createThreads();
    std::cout << "ubc match" << std::endl;
    findShortestPath();
    ubcMatch();
    std::cout << "ubc match done" << std::endl;
    stitchImages();
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
    //fileNames = DirectoryReader::readDirectory(input_path);
}

void MosaicData::createImages() {
    int tempID = 0;
    for (int i = 0; i < pgmFileNames.size(); ++i) {

    //}
    //for(std::string file : pgmFileNames){
        ImageData *imageData = new ImageData(i);
        imageData->setPgm_path(pgmFileNames.at(i));
        //imageData->setPath(fileNames.at(i));
        imageData->setPath(im->getJpgFileNames().at(tempID));

	    imageData->setInfo(exif::read(imageData->getPath()));
        imageList.insert(imageList.begin()+i,*imageData);
        tempID++;
    }
    referenceImage = &imageList.at(imageList.size()/2);

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
        imageList.at(id).setFeaturePoints(points);

        id++;
    }
}

void MosaicData::ubcMatch() {
    for (int i = 0; i < imagePairs.size() ; ++i) {
        for (int j = 0; j < imagePairs.at(i).size()-1; ++j) {
            MatchPoints *matcher = new MatchPoints(
                    *imagePairs.at(i).at(0).getImageData()->getFeaturePoints(),
                    *imagePairs.at(i).at(j+1).getImageData()->getFeaturePoints());
            //todo how to save homography
            matcher->getHomography();

        }
    }




   /* cv::Mat image1 = imread(imageList.at(0).getPath());
    cv::Mat image2 = imread(imageList.at(1).getPath());
    image1 = im->cropImage(image1);
    image2 = im->cropImage(image2);

    cv::Mat mosaic = Mat::zeros(1000,1000,CV_8UC1);


    Mat im_out;
    cv::warpPerspective(image1,im_out,matcher->getHomography(),mosaic.size());

    // Display images


    imshow("Source Image", image1);

    imshow("Destination Image", image2);

    imshow("Warped Source Image", im_out);



    waitKey(0);*/
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

void MosaicData::findShortestPath() {
    std::vector<HomographyData> startPairs = (std::vector<HomographyData> &&) imagePairs.at(referenceImage->getId());
    HomographyData startNode = startPairs.at(0);

    cv::Mat startMat = Mat::eye(3, 3, CV_64F);
    for (int i = 0; i < startPairs.size()-1; ++i) {
        HomographyData node = startPairs.at(i+1);
        calculatePairs(imagePairs.at(node.getImageData()->getId()), startNode, startMat);
    }


}

void MosaicData::calculatePairs(std::vector<HomographyData> &currentlist, HomographyData previousNode, Mat prevMat) {
    HomographyData currentNode = currentlist.at(0);
    MatchPoints *matcher = new MatchPoints(*currentNode.getImageData()->getFeaturePoints(),
                                           *previousNode.getImageData()->getFeaturePoints());
    cv::Mat homography = matcher->getHomography();
    homography = homography * prevMat;
    HomographyData *homographyData = new HomographyData(*currentNode.getImageData(),
                                                        currentNode.getDistance(),
                                                        homography,
                                                        previousNode.getImageData()->getId());
    homographyList.push_back(*homographyData);
    checkHomList.insert(currentNode.getImageData()->getId());
    for (int i = 0; i < currentlist.size()-1; ++i) {
        HomographyData newNode = currentlist.at(i+1);
        if(checkHomList.find(newNode.getImageData()->getId()) != checkHomList.end()){
            calculatePairs(imagePairs.at(newNode.getImageData()->getId()), currentNode, Mat());
        }
    }
}


