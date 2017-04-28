//============================================================================
// Name        : TreeMarkupToolbox.cpp
// Author      : Machine Vision UMU
// Version     : 1.0
// Copyright   : GNU
// Description : Method for positioning field measured trees relative to image data for building a 3D model.
//============================================================================

#include <opencv2/core.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/highgui.hpp>

#include <iostream>
<<<<<<< HEAD
#include "FeaturePoints.h"
=======
#include <string>

using namespace cv;
using namespace std;

Mat readimage(String imagename)
{
    Mat image;
    image = imread( imagename, IMREAD_COLOR );          // Read the Image file
    if( image.empty() )                         
    {
        cout <<  "Could not open or find the image" << std::endl ;
    }
    return(image);
    
}
    
void displayimage(Mat img)
{
    namedWindow( "Display window", WINDOW_AUTOSIZE ); // Create a new window to display the image
    imshow( "Display window", img );                // Display the image
    waitKey(0); // Wait for a keystroke in the window
}
>>>>>>> 753e8cf16b71d1e69ddbf30831a75c0d0c6782fc

extern "C"{
#include <vl/generic.h>
}

<<<<<<< HEAD
int main(){
    VL_PRINT ("vlfeat loaded properly\n");
    FeaturePoints* points = new FeaturePoints();
    points->testClass();
    points->calculatePoints("filnamn");
    return 0;
=======
int main( int argc, char** argv )
{
    String imageName;
    Mat image;
    try
    {
        if( argc > 1)
        {
            imageName = argv[1];
        }
    }
    catch(...)
    {
        cout<<"Please specify a Image filename to read"<< std::endl;
    }
    
    image=readimage(imageName);
    displayimage(image);
>>>>>>> 753e8cf16b71d1e69ddbf30831a75c0d0c6782fc
}
        
    