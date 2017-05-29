//
// Created by 5dv115 on 5/26/17.
//

#ifndef TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
#define TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H


#include <opencv2/core/mat.hpp>
#include "ImageData.h"
#include <limits>
#include <cstddef>

class HomographyData {
private:
    ImageData *imageData;
    float distance;
    cv::Mat homography;
    HomographyData prevNode;
    int recursionDepth;
public:
    int getRecursionDepth() const;

    void setRecursionDepth(int recursionDepth);


public:

    HomographyData(ImageData imageData, float range,
                   cv::Mat homography, HomographyData prevNode) : imageData(&imageData),
                                                     distance(range),
                                                     homography(homography),
                                                     prevNode(prevNode){}

    HomographyData() : recursionDepth(std::numeric_limits<int>::max()){}



    float getDistance() const {
        return distance;
    }

    const HomographyData &getPrevNode() const;

    void setPrevNode(const HomographyData &prevNode);

    void setDistance(float range) {
        HomographyData::distance = range;
    }

    const cv::Mat &getHomography() const {
        return homography;
    }

    void setHomography(const cv::Mat &homography) {
        HomographyData::homography = homography;
    }



    ImageData *getImageData() const {
        return imageData;
    }

    void setImageData(ImageData *imageData) {
        HomographyData::imageData = imageData;
    }


};


#endif //TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
