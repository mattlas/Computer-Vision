 
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


using namespace cv;
void imaq::readJPGfromFolder() {                      // Read all images from a folder
    jpgFileNames = DirectoryReader::readDirectory(jpgFolder);
    std::cout <<  "Files loaded to jpgFileNames" << std::endl ;
    //std::cout <<  (std::string)jpgFileNames << std::endl ;
}

void imaq::addDirectory(std::string dir) {
    directoryList.push_back(dir);
    std::cout <<  "Input directory added" << std::endl ;
}

void imaq::convertToPGM()
{
    std::cout <<  "Conversion to PGM started" << std::endl ;
    int i=0;
    for(std::string file_name : jpgFileNames){
        std::cout <<  "Conversion to PGM started" << std::endl ;
        imaq::image=readImage(file_name);
        imaq::grayImage=rgb2gray(image);

        std::string name = "testPGM";
        name.append(".pgm");
        write2pgm(grayImage,name);
        i++;
    }
}
Mat imaq::readImage(String img_name)
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
    pgm_params.push_back(CV_IMWRITE_PXM_BINARY);
    pgm_params.push_back(1);
    imwrite(file_name, img_data, pgm_params);
}

Mat imaq::rgb2gray(Mat img_data)                            // Input img_data in RGB format
{
    cvtColor(img_data,imaq::grayImage,COLOR_RGB2GRAY);
}

