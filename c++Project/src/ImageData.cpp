//
// Created by 5dv115 on 5/19/17.
//

#include "ImageData.h"

ImageData::ImageData(int id) {
    this->id = id;

}

ImageData::ImageData() {

}

int ImageData::getId() const {
    return id;
}

void ImageData::setId(int id) {
    ImageData::id = id;
}

const std::string &ImageData::getPath() const {
    return path;
}

void ImageData::setPath(const std::string &path) {
    ImageData::path = path;
}

FeaturePoints *ImageData::getFeaturePoints() const {
    return featurePoints;
}

void ImageData::setFeaturePoints(FeaturePoints *featurePoints) {
    ImageData::featurePoints = featurePoints;
}

const std::string &ImageData::getPgm_path() const {
    return pgm_path;
}

void ImageData::setPgm_path(const std::string &pgm_path) {
    ImageData::pgm_path = pgm_path;
}



