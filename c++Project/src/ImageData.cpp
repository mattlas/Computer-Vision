//
// Created by 5dv115 on 5/8/17.
//

#include "ImageData.h"
#include <cstring>
#include <iostream>
#include <vector>
#include <memory>
#include <dirent.h>

ImageData::ImageData(void) {

}


void ImageData::readPGMFromFolder() {
    for(std::string directory : directories){
        pgmFileNames = GetDirectoryFiles(directory);
        for ( std::string file : pgmFileNames) {
            std::cout << file << std::endl;
        }
    }

}

std::vector<std::string> ImageData::GetDirectoryFiles(const std::string& dir) {
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

void ImageData::addDirectory(std::string dir) {
    directories.push_back(dir);
}

void ImageData::startProcess() {
    readPGMFromFolder();
    //todo call FeaturePoints for each file in pgmFileNames
}
