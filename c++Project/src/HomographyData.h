//
// Created by 5dv115 on 5/26/17.
//

#ifndef TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
#define TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H


#include <opencv2/core/mat.hpp>
#include "ImageData.h"

class HomographyData {
private:
    ImageData *imageData;
    float distance;
    cv::Mat homography;
    int pairID;



public:

    HomographyData(ImageData imageData, float range,
                   cv::Mat homography, int pairID) : imageData(&imageData),
                                                     distance(range),
                                                     homography(homography),
                                                     pairID(pairID){}
    HomographyData();



    float getDistance() const {
        return distance;
    }

    void setDistance(float range) {
        HomographyData::distance = range;
    }

    const cv::Mat &getHomography() const {
        return homography;
    }

    void setHomography(const cv::Mat &homography) {
        HomographyData::homography = homography;
    }

    int getPairID() const {
        return pairID;
    }

    void setPairID(int pairID) {
        HomographyData::pairID = pairID;
    }

    ImageData *getImageData() const {
        return imageData;
    }

    void setImageData(ImageData *imageData) {
        HomographyData::imageData = imageData;
    }


};


#endif //TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
