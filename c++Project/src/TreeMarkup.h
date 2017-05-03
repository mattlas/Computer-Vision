#include <string>
#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class FeaturePoints {
public:
    FeaturePoints(void);
    void testClass();
    void calculatePoints(std::string);
};
#endif //C_PROJECT_FEATUREPOINTS_H

class imaq
{
public:
    Mat readimage(String);
    void showimage(Mat);
};