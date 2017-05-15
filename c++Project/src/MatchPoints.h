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
public:
    MatchPoints();

    MatchPoints(FeaturePoints *point1, FeaturePoints *point2);
};


#endif //TREEMARKUPTOOLBOX_MATCHPOINTS_H
