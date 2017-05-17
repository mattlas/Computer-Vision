 
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
    std::cout <<  "Loading files from folder" << imaq::jpgFolder<<std::endl ;
    std::cout <<  "Files loaded to jpgFileNames" << std::endl ;
    for(std::string file_name : imaq::jpgFileNames){
        std::cout <<  file_name << std::endl ;
    }

//    int ct = 0;
//    std::string fname=imaq::jpgFolder;
//
//    fname.append("test%d.jpg");                    // To read from folder: "{src}/test1.jpg", etc in sequence
//
//    VideoCapture cap(fname);
//    while( cap.isOpened() )
//    {
//        std::stringstream ss;
//
//
//        std::string name = "testPGM_";
//        std::string type = ".pgm";
//        std::string filename = ss.str();
//
//        ss<<folderName<<"/"<<name<<(ct + 1)<<type;
//
//        std::string fullPath = ss.str();
//        ss.str("");
//
//
//        Mat img;
//        cap.read(img);
//        std::cout <<  "Image opened" << std::endl ;
//
//        //imaq::image=readImage(file_name);
//        imaq::grayImage=rgb2gray(img);
//        std::cout <<  imaq::grayImage << std::endl ;
//
//    //    std::cout <<  "Converted to Grayscale";
//        write2pgm(img,fullPath);
//        ct++;
//        if (ct>2)
//        {break;}
//    }

}

void imaq::addDirectory(std::string dir) {
    directoryList.push_back(dir);
    std::cout <<  "Input directory added" << std::endl ;
    std::cout<<dir<<std::endl;
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
    std::cout <<  "Conversion to PGM started" << std::endl ;
    int i=0;
    for(std::string file_name : imaq::jpgFileNames){
//        std::cout <<  "Converting " <<file_name<< std::endl ;
        imaq::image=imread(file_name,CV_LOAD_IMAGE_GRAYSCALE);
//        std::cout <<  imaq::image<< std::endl ;
//        cvtColor(imaq::image,imaq::grayImage,COLOR_RGB2GRAY);
//        imaq::grayImage=rgb2gray(imaq::image);

        std::stringstream ss;
        std::string name = "testPGM_";
        std::string type = ".pgm";
        std::string filename = ss.str();
        ss<<op_dir<<'/'<<name<<(i + 1)<<type;

        std::string fullPath = ss.str();
        ss.str("");

        write2pgm(imaq::grayImage,fullPath);
        i++;
    }
    std::cout <<  "Converted " <<i<< " files" <<std::endl ;
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
    std::cout <<  "Writing file to folder" <<file_name<<std::endl ;
    imwrite(file_name, img_data, pgm_params);
}

Mat imaq::rgb2gray(Mat img_data)                            // Input img_data in RGB format
{
    Mat new_gray;
    cvtColor(img_data,new_gray,COLOR_RGB2GRAY);
    return new_gray;
}

