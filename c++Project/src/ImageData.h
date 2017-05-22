//
// Created by 5dv115 on 5/19/17.
//

#ifndef TREEMARKUPTOOLBOX_IMAGEDATA_H
#define TREEMARKUPTOOLBOX_IMAGEDATA_H

#include <string>
#include "KeyPoint.h"
#include "FeaturePoints.h"
#include <memory>


class ImageData {


private:
    int id;
    std::string path;
    //std::vector<std::unique_ptr<KeyPoint>> files;
    std::vector<FeaturePoints> featurePoints;


public:
    ImageData(int id);



};


#endif //TREEMARKUPTOOLBOX_IMAGEDATA_H
