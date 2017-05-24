//
// Created by 5dv115 on 5/19/17.
//

#ifndef TREEMARKUPTOOLBOX_IMAGEDATA_H
#define TREEMARKUPTOOLBOX_IMAGEDATA_H

#include <string>
#include <memory>
#include "FeaturePoints.h"
#include "exif.h"


class ImageData {


private:
    int id;
    std::string path;
    std::string pgm_path;
    exif::EXIFInfo info;
public:
    const exif::EXIFInfo &getInfo() const;

private:
    FeaturePoints* featurePoints;


public:
    ImageData();

    ImageData(int id);

    int getId() const;

    void setId(int id);

    void setInfo(exif::EXIFInfo info);

    const std::string &getPath() const;

    void setPath(const std::string &path);

    FeaturePoints *getFeaturePoints() const;

    void setFeaturePoints(FeaturePoints *featurePoints);

    const std::string &getPgm_path() const;
    void setPgm_path(const std::string &pgm_path);


};


#endif //TREEMARKUPTOOLBOX_IMAGEDATA_H
