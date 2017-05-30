//
// Created by 5dv115 on 5/12/17.
//

#include <opencv/cv.hpp>
#include "MatchPoints.h"

MatchPoints::MatchPoints(FeaturePoints point1, FeaturePoints point2)
        : point1(point1), point2(point2) {
    numberMatches = 0;
    findMatches();
    createHomography();

    std::cout << "M = "<< std::endl << " "  << homography << std::endl << std::endl;

}



void MatchPoints::findMatches() {
    //For all the keypoints in point1
    for(ulong index1 = 0 ; index1 < point1.getKeyPoints().size(); index1++){
        KeyPoint key1 = point1.getKeyPoints().at(index1);
        int32_t closestDistance = INT32_MAX;
        int32_t secondClosestDistance = INT32_MAX;
        ulong bestMatch = ULONG_MAX;
        KeyPoint bestKey;
        double threshold = 1.5;

        //For all the keypoints in point2
        for(ulong index2 = 0 ; index2 < point2.getKeyPoints().size(); index2++){
            KeyPoint key2 = point2.getKeyPoints().at(index2);
            //Get squared distance
            int distSquared = squaredDistance(index1, index2);
            //If closer than closest distance save new distance.
            if(distSquared < closestDistance ){
                secondClosestDistance = closestDistance;
                closestDistance = distSquared;
                bestMatch = index2;
                bestKey = key2;
            }
        }
        //If two closest distances are in threshold and there is a best match.
        if (((threshold * closestDistance) < secondClosestDistance) && (bestMatch != ULONG_MAX)){
            std::vector<int> match;
            match1.push_back(cv::Point2f(key1.getX(),key1.getY()));
            match2.push_back(cv::Point2f(bestKey.getX(),bestKey.getY()));
            match.insert(match.begin(),(int)index1);
            match.insert(match.begin() +1 ,(int) bestMatch);
            matchedIndex.push_back(match);
            numberMatches++;
        }
    }
}
//Calculates the squared distance for all the descriptors in point 1 and 2.
int MatchPoints::squaredDistance(ulong index1, ulong index2) {
    int dif = 0;
    int distsq = 0;
    std::vector<uint16_t> desc1 = point1.getDescriptors().at(index1);
    std::vector<uint16_t> desc2 = point2.getDescriptors().at(index2);

    for (ulong i = 0; i < 128; ++i) {
        dif = (int) desc1.at(i) - (int) desc2.at(i);
        distsq += dif * dif;
    }
    return distsq;
}

void MatchPoints::createHomography() {
    //Creates homography with ransac.
    //TODO se how to make faster, Really slow now.
    homography = cv::findHomography(match1,match2,cv::RANSAC,3,cv::noArray(),200,0.995);

}

const cv::Mat &MatchPoints::getHomography() const {
    return homography;
}

void MatchPoints::setHomography(const cv::Mat &homography) {
    MatchPoints::homography = homography;
}





