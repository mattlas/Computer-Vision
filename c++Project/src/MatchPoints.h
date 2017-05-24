//
// Created by 5dv115 on 5/12/17.
//

#ifndef TREEMARKUPTOOLBOX_MATCHPOINTS_H
#define TREEMARKUPTOOLBOX_MATCHPOINTS_H


#include "FeaturePoints.h"
#include "KeyPoint.h"
#include "opencv2/core.hpp"
//#include "opencv2/xfeatures2d.hpp"
//#include "opencv2/features2d.hpp"

extern "C" {
//#include <toolbox/sift/vl_ubcmatch.c>
}
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
    //std::vector<std::vector<KeyPoint>> matchedPoints;
    int numberMatches;

public:

    MatchPoints();

    MatchPoints(FeaturePoints point1, FeaturePoints point2);

    void findMatches();

    int squaredDistance(ulong point, ulong keyPoint);

    void createHomography();

    const cv::Mat &getHomography() const;

    void setHomography(const cv::Mat &homography);

    void flennMatch();
};


#endif //TREEMARKUPTOOLBOX_MATCHPOINTS_H
