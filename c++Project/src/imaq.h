#include <string>
#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>

#include <iostream>

using namespace cv;
class imaq
{
private:
    Mat image;
    Mat grayImage;
    std::vector<std::string> directoryList;
    std::vector<std::string> jpgFileNames;
    std::string jpgFolder;
public:
    Mat readImage(std::string);
    void displayImage(Mat);
    void write2pgm(Mat,std::string );
    Mat rgb2gray(Mat);
    void readJPGfromFolder();
    void addDirectory(std::string);
    void convertToPGM(std::string);
    void createDIR(std::string);
};