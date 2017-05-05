 
#include <opencv2/opencv.hpp>
#include "imaq.h"
#include <iostream>
#include <string>

using namespace cv;

Mat imaq::readImage(String img_name)
{
    imaq::image = imread(img_name, IMREAD_COLOR);           // Read a image from a file
    if ( !imaq::image.data )                                // If image cannot be found
    {
        cout <<  "Could not open or find the image" << std::endl ;
        return -1;
    }
}

void imaq::displayImage(Mat img_name)
{
    imaq::image=img_name;
    if ( !imaq::image.data )                                // If image cannot be found
    {
    cout <<  "Could not open or find the image" << std::endl ;
    return -1;
    }
    namedWindow( "Display window", WINDOW_AUTOSIZE );       // Create a new window to display the image
    imshow( "Display window", imaq::image );                // Display the image
    waitKey(0);                                             // Wait for a keystroke in the window
}

void imaq::write2pgm(Mat img_data)
{
    imwrite("test_img.pgm", img_data, CV_IMWRITE_PXM_BINARY);
}

Mat imaq::rgb2gray(Mat img_data)                            // Input img_data in RGB format
{
    cvtColor(img_data,imaq::grayImage,COLOR_RGB2GRAY);
}