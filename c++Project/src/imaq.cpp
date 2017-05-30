 
#include <opencv2/opencv.hpp>
#include "imaq.h"
#include "DirectoryReader.h"
#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>
#include "opencv2/core/utility.hpp"
#include "opencv2/imgproc.hpp"
#include <iostream>
#include <string>
#include <vector>
#include<sstream>


using namespace cv;
void imaq::readJPGfromFolder() {                      // Read all image file names from a folder and store it in a variable

    imaq::jpgFileNames = DirectoryReader::readDirectory(imaq::jpgFolder);
    std::sort(jpgFileNames.begin(),jpgFileNames.end());
}

void imaq::addDirectory(std::string dir) {
    directoryList.push_back(dir);
    jpgFolder=dir;

}

void imaq::createDIR(std::string fname) {
    std::string folderCreateCommand;
    if(fname=='\0'){
        std::string folderName = "PGMdir";
        folderCreateCommand = "mkdir " + folderName;
    }
    else{
        folderCreateCommand = "mkdir " + fname;
    }
    system(folderCreateCommand.c_str());
}

void imaq::convertToPGM(std:: string op_dir)
{
    int i=0;
    for(std::string file_name : imaq::jpgFileNames){
        imaq::image=imread(file_name,CV_LOAD_IMAGE_GRAYSCALE);
        std::stringstream ss;
        std::string name = "testPGM_";
        std::string type = ".pgm";
        ss<<op_dir<<'/'<<name<<(i + 1)<<type;
        std::string fullPath = ss.str();
        ss.str("");

        write2pgm(imaq::image,fullPath);
        i++;
    }
}
Mat imaq::readImage(std::string img_name)
{
    imaq::image = imread(img_name, IMREAD_COLOR);           // Read a image from a file
    if ( !imaq::image.data )                                // If image cannot be found
    {
        Mat B = Mat_<std::complex<double> >(3, 3);
        std::cout <<  "Could not open or find the image" << std::endl ;
        return B;
    }
}

void imaq::displayImage(Mat img_name)
{
    imaq::image=img_name;
    if ( !imaq::image.data )                                // If image cannot be found
    {
        std::cout <<  "Could not open or find the image" << std::endl ;

    }
    namedWindow( "Display window", WINDOW_AUTOSIZE );       // Create a new window to display the image
    imshow( "Display window", imaq::image );                // Display the image
    waitKey(0);                                             // Wait for a keystroke in the window
}

void imaq::write2pgm(Mat img_data,std::string file_name)
{
    std::vector<int> pgm_params;
    pgm_params.push_back((int &&) CV_IMWRITE_PXM_BINARY);
    pgm_params.push_back(1);
    imwrite(file_name, img_data, pgm_params);
}

Mat imaq::rgb2gray(Mat img_data)                            // Input img_data in RGB format
{
    Mat new_gray;
    cvtColor(img_data,new_gray,COLOR_RGB2GRAY);
    return new_gray;
}

const std::vector<std::string> &imaq::getJpgFileNames() const {
    return jpgFileNames;
}

void imaq::setJpgFileNames(const std::vector<std::string> &jpgFileNames) {
    imaq::jpgFileNames = jpgFileNames;
}

Mat imaq::cropImage(Mat img) {
    int offset_x = 0.2*img.rows;
    int offset_y = 0.2*img.cols;

    cv::Rect roi;
    roi.x = offset_x;
    roi.y = offset_y;
    roi.width = 0.6*img.size().width;
    roi.height = 0.6*img.size().height;
    cv::Mat croppedImage = img(roi);
    return croppedImage;
}

