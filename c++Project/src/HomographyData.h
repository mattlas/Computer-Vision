//
// Created by 5dv115 on 5/26/17.
//

#ifndef TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
#define TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H


#include <opencv2/core/mat.hpp>
#include "ImageData.h"
#include <limits>
#include <cstddef>

/**
 * Class used to store all the data needed to calculate the homographies.
 *
 * imageData: A pointer to the imageData of the object
 *
 * distance: Not used. supposed to be used to get the shortest path between
 * image and reference.
 *
 * prevNode: a pointer to the previous node which is supposed to be
 * the path with the least iterations to the reference image.
 *
 * recursionDepth: Used to know what path has the least iterations to reference image.
 */
class HomographyData {
private:
    ImageData *imageData;
    float distance; //not used atm.
    cv::Mat homography;
    HomographyData* prevNode;
    int recursionDepth;




public:

    HomographyData(ImageData imageData, float range,
                   cv::Mat homography, HomographyData *prevNode) : imageData(&imageData),
                                                     distance(range),
                                                     homography(homography),
                                                     prevNode(prevNode){}

    HomographyData(void) : recursionDepth(std::numeric_limits<int>::max()){}

    virtual ~HomographyData() {
        delete imageData;
        delete prevNode;
    }


    float getDistance() const {
        return distance;
    }

    void setDistance(float distance) {
        HomographyData::distance = distance;
    }

    const cv::Mat &getHomography() const {
        return homography;
    }

    void setHomography(const cv::Mat &homography) {
        HomographyData::homography = homography;
    }

    HomographyData *getPrevNode() const {
        return prevNode;
    }

    void setPrevNode(HomographyData *prevNode) {
        HomographyData::prevNode = prevNode;
    }

    int getRecursionDepth() const {
        return recursionDepth;
    }

    void setRecursionDepth(int recursionDepth) {
        HomographyData::recursionDepth = recursionDepth;
    }

    ImageData *getImageData() const {
        return imageData;
    }

    void setImageData(ImageData *imageData) {
        HomographyData::imageData = imageData;
    }




};


#endif //TREEMARKUPTOOLBOX_HOMOGRAPHYDATA_H
