//
// Created by 5dv115 on 5/8/17.
//

#include "MosaicData.h"
#include <cstring>
#include <iostream>
#include <vector>
#include <memory>
#include <dirent.h>

MosaicData::MosaicData(void) {

}


void MosaicData::readPGMFromFolder() {
    for(std::string directory : directoryList){
        pgmFileNames = readDirectoryFiles(directory);
        for ( std::string file : pgmFileNames) {
            std::cout << file << std::endl;
        }
    }

}

std::vector<std::string> MosaicData::readDirectoryFiles(const std::string &dir) {
    std::vector<std::string> files;
    std::shared_ptr<DIR> directory_ptr(opendir(dir.c_str()), [](DIR *dir) { dir && closedir(dir); });
    struct dirent *dirent_ptr;
    if (!directory_ptr) {
        std::cout << "Error opening : " << std::strerror(errno) << dir << std::endl;
        return files;
    }

    while ((dirent_ptr = readdir(directory_ptr.get())) != nullptr) {
        if((std::string(dirent_ptr->d_name)).front() != '.'){
            std::string pathName = dir;
            if(pathName.back() != '/'){
                pathName.append("/");
            }
            pathName.append(std::string(dirent_ptr->d_name));
            files.push_back(pathName);
        }
    }
    return files;
}

void MosaicData::addDirectory(std::string dir) {
    directoryList.push_back(dir);
}

void MosaicData::startProcess() {
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
