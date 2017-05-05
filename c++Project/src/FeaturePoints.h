//
// Created by 5dv115 on 4/26/17.
//
#include <string>
#include <cstdint>
#include <vector>
#include "KeyPoint.h"

#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class FeaturePoints {
public:
    FeaturePoints(void);
    void testClass();
    std::vector<KeyPoint> keyPoints;
    std::vector<std::vector<uint8_t>> descriptors;

    void writeKeyPoints();

    void calculatePoints(char const *path);

};
#endif //C_PROJECT_FEATUREPOINTS_H

