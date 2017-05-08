//
// Created by 5dv115 on 4/26/17.
//
#include <string>
#include <vector>
#include "KeyPoint.h"
#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class FeaturePoints {
public:
    FeaturePoints(void);


    void calculatePoints(char const *path);

private:
    std::vector<KeyPoint> keyPoints;
    std::vector<std::vector<uint16_t>> descriptors;
    void writeKeyPoints(char *name);
    void writeDescriptors(char *name);
};
#endif //C_PROJECT_FEATUREPOINTS_H
