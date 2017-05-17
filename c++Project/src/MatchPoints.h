//
// Created by 5dv115 on 5/12/17.
//

#ifndef TREEMARKUPTOOLBOX_MATCHPOINTS_H
#define TREEMARKUPTOOLBOX_MATCHPOINTS_H
#define THRESHOLD 0.6;


#include "FeaturePoints.h"
#include "KeyPoint.h"


extern "C" {
//#include <toolbox/sift/vl_ubcmatch.c>
}
class MatchPoints {

private:
    std::vector<std::vector<uint16_t>> descriptor1;
    std::vector<std::vector<uint16_t>> descriptor2;
    FeaturePoints point1;
    FeaturePoints point2;

    std::vector<std::vector<int>> matchedIndex;
    //std::vector<std::vector<KeyPoint>> matchedPoints;
    int numberMatches;

public:
    MatchPoints();

    MatchPoints(FeaturePoints point1, FeaturePoints point2);

    void findMatches();

    int squaredDistance(ulong point, ulong keyPoint);
};


#endif //TREEMARKUPTOOLBOX_MATCHPOINTS_H
