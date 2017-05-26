//
// Created by 5dv115 on 5/26/17.
//

#ifndef TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
#define TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H


#include <opencv2/core/mat.hpp>
#include "ImageData.h"

class HomographyData {
private:
    ImageData imageData;
    float range;
    cv::Mat homography;
public:

    HomographyData(ImageData imageData, float range, cv::Mat homography) : imageData(imageData),
                                                                           range(range),
                                                                           homography(homography){}

    const ImageData &getImageData() const {
        return imageData;
    }

    void setImageData(const ImageData &imageData) {
        HomographyData::imageData = imageData;
    }

    float getRange() const {
        return range;
    }

    void setRange(float range) {
        HomographyData::range = range;
    }

    const cv::Mat &getHomography() const {
        return homography;
    }

    void setHomography(const cv::Mat &homography) {
        HomographyData::homography = homography;
    }


};


#endif //TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
