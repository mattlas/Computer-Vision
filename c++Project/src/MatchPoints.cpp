//
// Created by 5dv115 on 5/12/17.
//

#include "MatchPoints.h"

MatchPoints::MatchPoints(FeaturePoints point1, FeaturePoints point2)
        : point1(point1), point2(point2) {
    numberMatches = 0;
    findMatches();
}

void MatchPoints::findMatches() {
    for(ulong index1 = 0 ; index1 < point1.getKeyPoints().size(); index1++){
        KeyPoint key1 = point1.getKeyPoints().at(index1);
        int32_t closestDistance = INT32_MAX;
        int32_t secondClosestDistance = INT32_MAX;
        ulong bestMatch = 0;
        double threshold = 1.1;


        for(ulong index2 = 0 ; index2 < point2.getKeyPoints().size(); index2++){
            KeyPoint key2 = point2.getKeyPoints().at(index2);
            int distSquared = squaredDistance(index1, index2);
            if(distSquared < closestDistance ){
                secondClosestDistance = closestDistance;
                closestDistance = distSquared;
                bestMatch = index2;
            }
        }
        if ((threshold * closestDistance) < secondClosestDistance && bestMatch != 0){
            std::vector<int> match;
            match.insert(match.begin(),(int)index1);
            match.insert(match.begin() +1 ,(int) bestMatch);
            matchedIndex.push_back(match);

            /*
            std::vector<KeyPoint> pointMatch;
            pointMatch.insert(pointMatch.begin(),point1.getKeyPoints().at(index1));
            pointMatch.insert(pointMatch.begin() +1 ,point2.getKeyPoints().at(bestMatch));
            matchedPoints.push_back(*pointMatch);*/

            numberMatches++;
        }
    }
    int fin =0;
}

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
