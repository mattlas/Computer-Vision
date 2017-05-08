#include <string>
#ifndef C_PROJECT_FEATUREPOINTS_H
#define C_PROJECT_FEATUREPOINTS_H

class imaq
{
private:
    Mat image;
    Mat grayImage;
public:
    Mat readImage(String);
    void displayImage(Mat);
    void write2pgm(Mat);
    Mat rgb2gray(Mat);
};