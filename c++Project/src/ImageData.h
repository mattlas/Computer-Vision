//
// Created by 5dv115 on 5/19/17.
//

#ifndef TREEMARKUPTOOLBOX_IMAGEDATA_H
#define TREEMARKUPTOOLBOX_IMAGEDATA_H

#include <string>
#include "FeaturePoints.h"
#include <memory>


class ImageData {


private:
    int id;
    std::string path;
    FeaturePoints* featurePoints;


public:
    ImageData();

    ImageData(int id);

    int getId() const;

    void setId(int id);

    const std::string &getPath() const;

    void setPath(const std::string &path);

    FeaturePoints *getFeaturePoints() const;

    void setFeaturePoints(FeaturePoints *featurePoints);

};


#endif //TREEMARKUPTOOLBOX_IMAGEDATA_H