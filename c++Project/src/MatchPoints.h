//
// Created by 5dv115 on 5/12/17.
//

#ifndef TREEMARKUPTOOLBOX_MATCHPOINTS_H
#define TREEMARKUPTOOLBOX_MATCHPOINTS_H



#include "FeaturePoints.h"


extern "C" {
//#include <toolbox/sift/vl_ubcmatch.c>
}
class MatchPoints {

private:
    std::vector<std::vector<uint16_t>> descriptor1;
    std::vector<std::vector<uint16_t>> descriptor2;
    FeaturePoints *point1;
    FeaturePoints *point2;

public:
    MatchPoints();

    MatchPoints(FeaturePoints *point1, FeaturePoints *point2);

    void findMatches();

    void squaredDistance();
};


#endif //TREEMARKUPTOOLBOX_MATCHPOINTS_H
