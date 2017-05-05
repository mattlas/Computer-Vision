//
// Created by 5dv115 on 4/26/17.
//
#include <string>
#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class FeaturePoints {
public:
    FeaturePoints(void);
    void testClass();
<<<<<<< HEAD
    std::vector<KeyPoint> keyPoints;
    std::vector<std::vector<uint8_t>> descriptors;

    void writeKeyPoints();

    void calculatePoints(char const *path);

=======
    void calculatePoints(std::string);
>>>>>>> c63c811949615e290290249c405c5b28c4623e75
};
#endif //C_PROJECT_FEATUREPOINTS_H
