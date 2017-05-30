//
// Created by 5dv115 on 5/12/17.
//

#ifndef TREEMARKUPTOOLBOX_MATCHPOINTS_H
#define TREEMARKUPTOOLBOX_MATCHPOINTS_H


#include "FeaturePoints.h"
#include "KeyPoint.h"
#include "opencv2/core.hpp"

/**
 * This class is used to calculate the matches between two images. The class calculate the matches
 * and runs the openCV function findHomography which uses ransac to find the homography between the
 * two images. The important variable is homography which can be accessed by a getter.
 */
class MatchPoints {

private:
    std::vector<std::vector<uint16_t>> descriptor1;
    std::vector<std::vector<uint16_t>> descriptor2;


    std::vector<std::vector<int>> matchedIndex;
    std::vector<cv::Point2f> match1;
    std::vector<cv::Point2f> match2;
    FeaturePoints point1;
    FeaturePoints point2;
    cv::Mat homography;
private:
    int numberMatches;

public:

    /**
     * Constructor. takes two FeaturePoints and runs the matching.
     * @param point1, FeaturePoint1
     * @param point2, FeaturePoints2
     */
    MatchPoints(FeaturePoints point1, FeaturePoints point2);

    void findMatches();

    int squaredDistance(ulong point, ulong keyPoint);

    void createHomography();

    const cv::Mat &getHomography() const;

    void setHomography(const cv::Mat &homography);

};


#endif //TREEMARKUPTOOLBOX_MATCHPOINTS_H
